package com.wemarket.wac.common.utils;

import com.wemarket.wac.common.dto.BaseDTO;
import com.wemarket.wac.common.dto.BizErrors;

@FunctionalInterface
public interface ExecServiceTemplate<E extends BaseDTO, T> {
    public T apply(E requestDto, BizErrors bizErrors);
}