package com.wemarket.wac.biz.service.impl;

import com.wemarket.wac.biz.dto.QueryUserReq;
import com.wemarket.wac.biz.dto.QueryUserRsp;
import com.wemarket.wac.biz.dto.UserDTO;
import com.wemarket.wac.biz.service.UserService;
import com.wemarket.wac.integration.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by justinli on 2019/3/11
 **/
@Service("com.wemarket.wac.biz.service.UserService")
public class UserServiceImpl implements UserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDAO userDAO;

    @Override
    public QueryUserRsp queryUser(QueryUserReq request) throws Exception{
        LOGGER.info("queryUser request:{}", request);

        UserDTO qryUserDTO = new UserDTO();
        qryUserDTO.setOpenId(request.getOpenId());
        qryUserDTO.setUserName(request.getUserName());
        qryUserDTO.setOrgId(request.getOrgId());
        List<UserDTO> userDTOList = userDAO.queryUser(qryUserDTO);

        QueryUserRsp response = new QueryUserRsp();
        response.setUserList(userDTOList);

        return response;
    }

    @Override
    public void addUser(UserDTO userDTO) throws Exception{
        userDAO.addUser(userDTO);
    }

    @Override
    public void updateUser(UserDTO userDTO) throws Exception{
        userDAO.updateUser(userDTO);
    }
}
