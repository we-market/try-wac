package com.wemarket.wac.biz.dto;

import com.wemarket.wac.common.dto.BaseMessage;

import java.util.List;

/**
 * Created by justinli on 2019/3/11
 **/
public class QueryUserRsp extends BaseMessage {

    List<UserDTO> userList;

    public List<UserDTO> getUserList() {
        return userList;
    }

    public void setUserList(List<UserDTO> userList) {
        this.userList = userList;
    }
}
