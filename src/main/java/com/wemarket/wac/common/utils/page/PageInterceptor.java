package com.wemarket.wac.common.utils.page;

import com.wemarket.wac.common.exception.SysException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PageInterceptor implements Interceptor {

    private static final Logger LOG = LoggerFactory.getLogger(PageInterceptor.class);
    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY =
            new DefaultObjectWrapperFactory();
    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();
    // private static final String DEFAULT_DIALECT = "mysql"; // 数据库类型(默认为mysql)
    private static final String DEFAULT_PAGE_SQL_ID = ".*Page$"; // 需要拦截的ID(正则匹配)
    // private static String dialect = DEFAULT_DIALECT; // 数据库类型(默认为mysql)
    private static String pageSqlIdPattern = ""; // 需要拦截的ID(正则匹配)

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaStatementHandler = MetaObject.forObject(statementHandler,
                DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);

        // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环可以分离出最原始的的目标类)
        while (metaStatementHandler.hasGetter("h")) {
            Object object = metaStatementHandler.getValue("h");
            metaStatementHandler = MetaObject.forObject(object,
                    DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
        }
        // 分离最后一个代理对象的目标类
        while (metaStatementHandler.hasGetter("target")) {
            Object object = metaStatementHandler.getValue("target");
            metaStatementHandler = MetaObject.forObject(object,
                    DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
        }

        Configuration configuration =
                (Configuration) metaStatementHandler.getValue("delegate.configuration");
        String dialectString = configuration.getVariables().getProperty("dialect").toUpperCase();
        DialectType dialectType = null;//
        if (null == dialectString || "".equals(dialectString)) {
            LOG.warn("Property dialect is not setted,use default 'mysql' ");
            dialectType = DialectType.MYSQL;
        } else {
            dialectType = DialectType.valueOf(dialectString);
        }

        pageSqlIdPattern = configuration.getVariables().getProperty("pageSqlId");
        if (null == pageSqlIdPattern || "".equals(pageSqlIdPattern)) {
            LOG.warn("Property pageSqlId is not setted,use default '.*Page$' ");
            pageSqlIdPattern = DEFAULT_PAGE_SQL_ID;
        }

        MappedStatement mappedStatement =
                (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
        // 只重写需要分页的sql语句。通过MappedStatement的ID匹配，默认重写以Page结尾的MappedStatement的sql
        if (mappedStatement.getId().matches(pageSqlIdPattern)) {
            BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
            Object parameterObject = boundSql.getParameterObject();
            if (parameterObject == null) {
                throw new SysException("parameterObject is null!");
            } else {
                PageInfo page = (PageInfo) metaStatementHandler
                        .getValue("delegate.boundSql.parameterObject.page");
                String sql = boundSql.getSql();
                // 去掉sql最后的分号
                sql = sql.trim();
                if (sql.endsWith(";")) {
                    sql = sql.substring(0, sql.length() - 1);
                }

                // 重写sql
                String pageSql = buildPageSql(dialectType, sql, page);
                metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
                // 采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数
                metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
                metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
                Connection connection = (Connection) invocation.getArgs()[0];
                // 重设分页参数里的总页数等
                setPageParameter(sql, connection, mappedStatement, boundSql, page);
            }
        }
        // 将执行权交给下一个拦截器
        return invocation.proceed();
    }

    /**
     * 根据数据库类型，生成特定的分页sql。
     *
     * @param dialectType 数据库类型
     * @param sql         sql語句
     * @param pageInfo    分頁信息
     * @return sql脚本
     */
    private String buildPageSql(DialectType dialectType, String sql, PageInfo pageInfo) {
        if (pageInfo != null) {
            Dialect dialect = null;
            switch (dialectType) {
                case MYSQL:
                    dialect = new MySqlDialect();
                    break;
                // case ORACLE:
                // dialect = new OracleDialect();
                //
                // break;
                default:
                    return sql;
            }
            return dialect.getLimitSql(sql, pageInfo);
        } else {
            return sql;
        }
    }

    @Override
    public Object plugin(Object target) {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    /**
     * 从数据库里查询总的记录数并计算总页数，回写进分页参数<code>PageParameter</code>,这样调用者就可用通过 分页参数
     * <code>PageParameter</code> 获得相关信息。
     *
     * @param sql             sql语句
     * @param connection      连接
     * @param mappedStatement sql声明
     * @param boundSql        boundSql
     * @param pageInfo        分页信息
     */
    private void setPageParameter(String sql, Connection connection, MappedStatement mappedStatement, BoundSql boundSql, PageInfo pageInfo)
            throws SQLException {
        if (pageInfo.getTotalPage() < 0) {
            //bugfix for TDSQL /* slave */ select 语法
            String lowCaseSql = sql.toLowerCase().trim();
            boolean startWithSelect = lowCaseSql.startsWith("select");
            String prefix = "";
            String newSql = sql;
            if (!startWithSelect) {
                int position = lowCaseSql.indexOf("select");
                if (position > -1) {
                    prefix = sql.substring(0, position);
                    newSql = sql.substring(position);
                }
            }

            // 记录总记录数
            // Use smart count SQL parser to retrieve count type SQL
//            String countSql = prefix + " select count(0) from (" + newSql + ") as total ";
            String countSql = prefix + " " + CostomSQLParser.getSmartCountSql(newSql);
            try (PreparedStatement countStmt = connection.prepareStatement(countSql)) {
                BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
                        boundSql.getParameterMappings(), boundSql.getParameterObject());

                //bugfix for 使用in 语法时，参数丢失
                @SuppressWarnings(value = "unchecked")
                Map<String, Object> additionalParameters = null;
                try {
                    additionalParameters = (Map<String, Object>) FieldUtils.readField(boundSql, "additionalParameters", true);
                } catch (IllegalAccessException e) {
                }


                setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject(), additionalParameters);
                int totalCount = 0;
                try (ResultSet rs = countStmt.executeQuery()) {
                    if (rs.next()) {
                        totalCount = rs.getInt(1);
                    }
                }

                pageInfo.setTotalCount(totalCount);
                int totalPage = totalCount / pageInfo.getPageSize()
                        + ((totalCount % pageInfo.getPageSize() == 0) ? 0 : 1);
                pageInfo.setTotalPage(totalPage);
            } catch (SQLException e) {
                throw e;
            }
        }
    }

    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement,
                               BoundSql boundSql, Object parameterObject, Map<String, Object> additionalParameters) throws SQLException {
        if (additionalParameters != null) {
            additionalParameters.entrySet().stream().forEach(entry -> {
                boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
            });
        }

        ParameterHandler parameterHandler =
                new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
        parameterHandler.setParameters(ps);


    }

    @Override
    public void setProperties(Properties properties) {
        // TODO Auto-generated method stub

    }
}
