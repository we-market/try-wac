package com.wemarket.wac.integration.dao;

import com.wemarket.wac.biz.dto.OrgnazationDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by justinli on 2019/3/5
 **/
public interface OrgnazationDAO {

    List<OrgnazationDTO> queryOrgnazation(OrgnazationDTO orgnazationDTO) throws SQLException;
}
