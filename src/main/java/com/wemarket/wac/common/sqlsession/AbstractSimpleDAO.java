package com.wemarket.wac.common.sqlsession;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;

public class AbstractSimpleDAO extends AbstractDAO implements SImpleDAOInterface {

    protected AbstractSqlSession getSession() {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.SIMPLE, true);
        return new AbstractSqlSession(sqlSession);
    }
}
