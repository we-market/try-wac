package com.wemarket.wac.integration.dao.impl;

import com.wemarket.wac.biz.dto.OrgnazationDTO;
import com.wemarket.wac.common.sqlsession.AbstractSqlSession;
import com.wemarket.wac.integration.dao.OrgnazationDAO;
import com.wemarket.wac.integration.dao.WacBaseDAO;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by justinli on 2019/3/5
 **/
@Repository("com.wemarket.wac.integration.OrgnazationDAO")
public class OrgnazationDAOImpl extends WacBaseDAO implements OrgnazationDAO {

    @Override
    public List<OrgnazationDTO> queryOrgnazation(OrgnazationDTO orgnazationDTO) throws SQLException{
        try (AbstractSqlSession session = getSession()) {
            OrgnazationDAO mapper = (OrgnazationDAO)session.getMapper(OrgnazationDAO.class);
            return mapper.queryOrgnazation(orgnazationDTO);
        }
    }
}
