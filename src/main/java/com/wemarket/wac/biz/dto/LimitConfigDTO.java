package com.wemarket.wac.biz.dto;

public class LimitConfigDTO {
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
