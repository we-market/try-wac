package com.wemarket.wac.common.utils.page;

import com.wemarket.wac.common.exception.SysException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.List;

public class CostomSQLParser {


    public static final String KEEP_ORDERBY = "/*keep orderby*/";

    private static final List<SelectItem> COUNT_ITEM;

    private static final Alias TABLE_ALIAS;

    static {
        COUNT_ITEM = new ArrayList<SelectItem>();
        COUNT_ITEM.add(new SelectExpressionItem(new Column("count(0)")));

        TABLE_ALIAS = new Alias("table_count");
        TABLE_ALIAS.setUseAs(false);
    }


    public static PlainSelect getSelectBody(String sql) {
        try {
            Select select = (Select) CCJSqlParserUtil.parse(sql);
            return (PlainSelect) select.getSelectBody();
        } catch (Exception e) {
            throw new SysException("sql parse error", e);
        }
    }


    public static Select getSelect(String sql) {
        try {
            return (Select) CCJSqlParserUtil.parse(sql);
        } catch (Exception e) {
            throw new SysException("sql parse error", e);
        }
    }


    /**
     * Get smart count type SQL
     *
     * @param sql raw SQL
     * @return smart SQL
     */
    public static String getSmartCountSql(String sql) {
        isSupportedSql(sql);
        //Statement stmt = null;
        if (sql.indexOf(KEEP_ORDERBY) >= 0) {
            return getSimpleCountSql(sql);
        }


        Select select = null;
        try {
            select = getSelect(sql);
        } catch (Throwable e) {
            return getSimpleCountSql(sql);
        }

        try {
            SelectBody selectBody = (PlainSelect) select.getSelectBody();
            processSelectBody(selectBody);
        } catch (Exception e) {
            return getSimpleCountSql(sql);
        }

        processWithItemsList(select.getWithItemsList());
        sqlToCount(select);
        return select.toString();
    }

    /**
     * Get simple count type SQL
     *
     * @param sql raw SQL
     * @return simple SQL
     */
    public static String getSimpleCountSql(final String sql) {
        isSupportedSql(sql);
        StringBuilder stringBuilder = new StringBuilder(sql.length() + 40);
        stringBuilder.append("select count(0) from (");
        stringBuilder.append(sql);
        stringBuilder.append(") tmp_count");
        return stringBuilder.toString();
    }

    private static void isSupportedSql(String sql) {
        if (sql.trim().toUpperCase().endsWith("FOR UPDATE")) {
            throw new SysException("SQL contains FOR UPDATE is NOT support!");
        }
    }

    /**
     * 将sql转换为count查询
     *
     * @param select
     */
    private static void sqlToCount(Select select) {
        SelectBody selectBody = select.getSelectBody();
        if (selectBody instanceof PlainSelect && isSimpleCount((PlainSelect) selectBody)) {
            ((PlainSelect) selectBody).setSelectItems(COUNT_ITEM);
        } else {
            PlainSelect plainSelect = new PlainSelect();
            SubSelect subSelect = new SubSelect();
            subSelect.setSelectBody(selectBody);
            subSelect.setAlias(TABLE_ALIAS);
            plainSelect.setFromItem(subSelect);
            plainSelect.setSelectItems(COUNT_ITEM);
            select.setSelectBody(plainSelect);
        }
    }

    /**
     * Check whether simple counting is available
     *
     * @param select
     * @return
     */
    private static boolean isSimpleCount(PlainSelect select) {
        // Unavailable when 'group by'
        if (select.getGroupByColumnReferences() != null) {
            return false;
        }

        // Unavailable when 'distinct'
        if (select.getDistinct() != null) {
            return false;
        }
        for (SelectItem item : select.getSelectItems()) {
            // Unavailable when select with parameters
            if (item.toString().contains("?")) {
                return false;
            }
            // Unavailable when function in select
            if (item instanceof SelectExpressionItem) {
                if (((SelectExpressionItem) item).getExpression() instanceof Function) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Try to ignore orderby within select body
     *
     * @param selectBody
     */
    private static void processSelectBody(SelectBody selectBody) {
        if (selectBody instanceof PlainSelect) {
            processPlainSelect((PlainSelect) selectBody);
        } else if (selectBody instanceof WithItem) {
            WithItem withItem = (WithItem) selectBody;
            if (withItem.getSelectBody() != null) {
                processSelectBody(withItem.getSelectBody());
            }
        } else {
            SetOperationList operationList = (SetOperationList) selectBody;
            if (operationList.getSelects() != null && operationList.getSelects().size() > 0) {
                List<SelectBody> plainSelects = operationList.getSelects();
                for (SelectBody plainSelect : plainSelects) {
                    processSelectBody(plainSelect);
                }
            }
            if (!orderByHashParameters(operationList.getOrderByElements())) {
                operationList.setOrderByElements(null);
            }
        }
    }

    /**
     * Handling body of plain select
     *
     * @param plainSelect
     */
    private static void processPlainSelect(PlainSelect plainSelect) {
        if (!orderByHashParameters(plainSelect.getOrderByElements())) {
            plainSelect.setOrderByElements(null);
        }
        if (plainSelect.getFromItem() != null) {
            processFromItem(plainSelect.getFromItem());
        }
        if (plainSelect.getJoins() != null && plainSelect.getJoins().size() > 0) {
            List<Join> joins = plainSelect.getJoins();
            for (Join join : joins) {
                if (join.getRightItem() != null) {
                    processFromItem(join.getRightItem());
                }
            }
        }
    }

    /**
     * Handling 'with' items
     *
     * @param withItemsList
     */
    private static void processWithItemsList(List<WithItem> withItemsList) {
        if (withItemsList != null && withItemsList.size() > 0) {
            for (WithItem item : withItemsList) {
                processSelectBody(item.getSelectBody());
            }
        }
    }

    /**
     * Handle with sub-query
     *
     * @param fromItem
     */
    private static void processFromItem(FromItem fromItem) {
        if (fromItem instanceof SubJoin) {
            SubJoin subJoin = (SubJoin) fromItem;
            if (subJoin.getJoin() != null) {
                if (subJoin.getJoin().getRightItem() != null) {
                    processFromItem(subJoin.getJoin().getRightItem());
                }
            }
            if (subJoin.getLeft() != null) {
                processFromItem(subJoin.getLeft());
            }
        } else if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            if (subSelect.getSelectBody() != null) {
                processSelectBody(subSelect.getSelectBody());
            }
        } else if (fromItem instanceof ValuesList) {

        } else if (fromItem instanceof LateralSubSelect) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
            if (lateralSubSelect.getSubSelect() != null) {
                SubSelect subSelect = lateralSubSelect.getSubSelect();
                if (subSelect.getSelectBody() != null) {
                    processSelectBody(subSelect.getSelectBody());
                }
            }
        }
    }

    /**
     * Check whether there are parameters associated to orderby, then can NOT be ignored
     *
     * @param orderByElements
     * @return
     */
    private static boolean orderByHashParameters(List<OrderByElement> orderByElements) {
        if (orderByElements == null) {
            return false;
        }
        for (OrderByElement orderByElement : orderByElements) {
            if (orderByElement.toString().contains("?")) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
//        String sql = "select title,source from tb_news_meta order by add_time desc;";
//        for (int i = 0; i < 1000; i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println(MumbleSQLParser.getSmartCountSql(sql).toLowerCase());
//                    System.out.println(MumbleSQLParser.COUNT_ITEM.size());
//                }
//            }).start();
//        }
        Select select = (Select) CCJSqlParserUtil.parse("select name from user where id = 1 limit 0, 100 for update");
        PlainSelect selectBody = (PlainSelect) select.getSelectBody();
        Limit limit =
                selectBody.getLimit();


        System.out.println(limit);
        System.out.println(limit.getRowCount());
    }
}
