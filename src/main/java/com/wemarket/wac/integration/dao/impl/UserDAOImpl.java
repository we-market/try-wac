package com.wemarket.wac.integration.dao.impl;

import com.wemarket.wac.biz.dto.UserDTO;
import com.wemarket.wac.common.sqlsession.AbstractSqlSession;
import com.wemarket.wac.integration.dao.UserDAO;
import com.wemarket.wac.integration.dao.WacBaseDAO;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by justinli on 2019/3/5
 **/
@Repository("com.wemarket.wac.integration.UserDAO")
public class UserDAOImpl extends WacBaseDAO implements UserDAO {

    @Override
    public int addUser(UserDTO userDTO) throws SQLException {
        try (AbstractSqlSession session = getSession()) {
            UserDAO mapper = (UserDAO)session.getMapper(UserDAO.class);
            return mapper.addUser(userDTO);
        }
    }

    @Override
    public List<UserDTO> queryUser(UserDTO userDTO) throws SQLException{
        try (AbstractSqlSession session = getSession()) {
            UserDAO mapper = (UserDAO)session.getMapper(UserDAO.class);
            return mapper.queryUser(userDTO);
        }
    }

    @Override
    public int updateUser(UserDTO userDTO) throws SQLException{
        try (AbstractSqlSession session = getSession()) {
            UserDAO mapper = (UserDAO)session.getMapper(UserDAO.class);
            return mapper.updateUser(userDTO);
        }
    }
}
