package com.wemarket.wac.common.utils.page;

public interface Dialect {

    public String getCountSql(String sql);

    public String getLimitSql(String sql, PageInfo pageInfo);
}
