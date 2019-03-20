package com.wemarket.wac.biz.dto;

import com.wemarket.wac.common.dto.BaseDTO;

/**
 * Created by justinli on 2019/3/11
 **/
public class QueryUserReq extends BaseDTO {
    private String openId;
    private String userName;
    private String orgId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
