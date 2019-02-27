package com.wemarket.wac.common.dto;

import com.wemarket.wac.common.utils.BaseResponseStatus;

public class BaseMessage extends BaseDTO {

    private String code = BaseResponseStatus.SUCCESS.getCode();

    private String msg = "请求成功";

    /**
     * sequence number
     */
    private String seqNo;

    private String debugMsg;

    public void setResponseStatus(BaseResponseStatus status){
        this.code = status.getCode();
        this.msg = status.getMessage();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getDebugMsg() {
        return debugMsg;
    }

    public void setDebugMsg(String debugMsg) {
        this.debugMsg = debugMsg;
    }
}
