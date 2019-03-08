package com.wemarket.wac.integration.dao;

import com.wemarket.wac.biz.dto.OrganizationDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by justinli on 2019/3/5
 **/
public interface OrganizationDAO {

    List<OrganizationDTO> queryOrganization(OrganizationDTO organizationDTO) throws SQLException;
}
