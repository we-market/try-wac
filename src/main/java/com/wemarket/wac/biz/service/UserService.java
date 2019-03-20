package com.wemarket.wac.biz.service;

import com.wemarket.wac.biz.dto.QueryUserReq;
import com.wemarket.wac.biz.dto.QueryUserRsp;
import com.wemarket.wac.biz.dto.UserDTO;

/**
 * Created by justinli on 2019/3/11
 **/
public interface UserService {

    QueryUserRsp queryUser(QueryUserReq request) throws Exception;

    void addUser(UserDTO userDTO) throws Exception;

    void updateUser(UserDTO userDTO) throws Exception;
}
