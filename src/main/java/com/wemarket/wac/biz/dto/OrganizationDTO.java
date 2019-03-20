package com.wemarket.wac.biz.dto;

import com.wemarket.wac.common.dto.BaseDTO;

/**
 * Created by justinli on 2019/3/5
 **/
public class OrganizationDTO extends BaseDTO {
    private String orgId;
    private String orgName;
    private int loginType;
    private String emailSuffix;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public String getEmailSuffix() {
        return emailSuffix;
    }

    public void setEmailSuffix(String emailSuffix) {
        this.emailSuffix = emailSuffix;
    }
}
