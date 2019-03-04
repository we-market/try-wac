package com.wemarket.wac.common.dto;


/**
 * 用于spring前端与页面前端通讯的java对象。
 *
 * @author jonyang
 *
 */
public class WebMessage<T> extends BaseResponseDTO {
    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

}