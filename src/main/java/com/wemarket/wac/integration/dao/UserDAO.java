package com.wemarket.wac.integration.dao;

import com.wemarket.wac.biz.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by justinli on 2019/3/5
 **/
public interface UserDAO {

    int addUser(UserDTO userDTO) throws SQLException;

    List<UserDTO> queryUser(UserDTO userDTO) throws SQLException;

    int updateUser(UserDTO userDTO) throws SQLException;
}
