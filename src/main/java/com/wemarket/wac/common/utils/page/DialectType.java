package com.wemarket.wac.common.utils.page;

public enum DialectType {

    MYSQL("MYSQL"), ORACLE("ORACLE");

    private String value;

    DialectType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
