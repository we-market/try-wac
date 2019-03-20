package com.wemarket.wac.biz.dto;

import com.wemarket.wac.common.dto.BaseDTO;

public class LimitConfigDTO extends BaseDTO {
    private String methodName;
    private int tps;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getTps() {
        return tps;
    }

    public void setTps(int tps) {
        this.tps = tps;
    }
}
