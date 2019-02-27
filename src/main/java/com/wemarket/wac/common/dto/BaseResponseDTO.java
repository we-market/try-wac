package com.wemarket.wac.common.dto;

import com.wemarket.wac.common.utils.BaseResponseStatus;

public class BaseResponseDTO extends BaseDTO {
    //错误码
    private String errcode;

    //错误码描述
    private String errmsg;

    public BaseResponseDTO(){

    }

    public BaseResponseDTO(BaseResponseStatus statusEnum){
        this.errcode = statusEnum.getCode();
        this.errmsg = statusEnum.getMessage();
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public void setResponseStatus(BaseResponseStatus statusEnum){
        this.errcode = statusEnum.getCode();
        this.errmsg = statusEnum.getMessage();
    }
}
