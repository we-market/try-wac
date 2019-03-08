package com.wemarket.wac.integration.dao;

import com.wemarket.wac.biz.dto.LabelDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by justinli on 2019/3/6
 **/
public interface LabelDAO {

    List<LabelDTO> getLabel(LabelDTO labelDTO) throws SQLException;

    int addLabel(LabelDTO labelDTO) throws SQLException;
}
