package com.wemarket.wac.integration.dao.impl;

import com.wemarket.wac.biz.dto.LabelDTO;
import com.wemarket.wac.biz.dto.LimitConfigDTO;
import com.wemarket.wac.common.sqlsession.AbstractSqlSession;
import com.wemarket.wac.integration.dao.LabelDAO;
import com.wemarket.wac.integration.dao.LimitConfigDAO;
import com.wemarket.wac.integration.dao.WacBaseDAO;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by justinli on 2019/3/6
 **/
@Repository("com.wemarket.wac.integration.LabelDAO")
public class LabelDAOImpl extends WacBaseDAO implements LabelDAO {

    @Override
    public List<LabelDTO> getLabel(LabelDTO labelDTO) throws SQLException {
        try (AbstractSqlSession session = getSession()) {
            LabelDAO mapper = (LabelDAO)session.getMapper(LabelDAO.class);
            return mapper.getLabel(labelDTO);
        }
    }

    @Override
    public int addLabel(LabelDTO labelDTO) throws SQLException{
        try (AbstractSqlSession session = getSession()) {
            LabelDAO mapper = (LabelDAO)session.getMapper(LabelDAO.class);
            return mapper.addLabel(labelDTO);
        }
    }
}
