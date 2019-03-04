package com.wemarket.wac.integration.dao.impl;

import com.wemarket.wac.biz.dto.LimitConfigDTO;
import com.wemarket.wac.common.sqlsession.AbstractSqlSession;
import com.wemarket.wac.integration.dao.LimitConfigDAO;
import com.wemarket.wac.integration.dao.WacBaseDAO;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by justinli on 2019/3/4
 **/
public class LimitConfigDAOImpl extends WacBaseDAO implements LimitConfigDAO {

    @Override
    public List<LimitConfigDTO> getAllLimitConfig() throws SQLException {
        try (AbstractSqlSession session = getSession()) {
            LimitConfigDAO mapper = (LimitConfigDAO)session.getMapper(LimitConfigDAO.class);
            return mapper.getAllLimitConfig();
        }
    }
}
