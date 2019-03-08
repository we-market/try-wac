package com.wemarket.wac.integration.dao.impl;

import com.wemarket.wac.biz.dto.OrganizationDTO;
import com.wemarket.wac.common.sqlsession.AbstractSqlSession;
import com.wemarket.wac.integration.dao.OrganizationDAO;
import com.wemarket.wac.integration.dao.WacBaseDAO;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by justinli on 2019/3/5
 **/
@Repository("com.wemarket.wac.integration.OrganizationDAO")
public class OrganizationDAOImpl extends WacBaseDAO implements OrganizationDAO {

    @Override
    public List<OrganizationDTO> queryOrganization(OrganizationDTO organizationDTO) throws SQLException{
        try (AbstractSqlSession session = getSession()) {
            OrganizationDAO mapper = (OrganizationDAO)session.getMapper(OrganizationDAO.class);
            return mapper.queryOrganization(organizationDTO);
        }
    }
}
