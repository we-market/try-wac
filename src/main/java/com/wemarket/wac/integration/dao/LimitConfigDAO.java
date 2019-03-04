package com.wemarket.wac.integration.dao;

import com.wemarket.wac.biz.dto.LimitConfigDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by justinli on 2019/3/4
 **/
public interface LimitConfigDAO {

    List<LimitConfigDTO> getAllLimitConfig() throws SQLException;
}
