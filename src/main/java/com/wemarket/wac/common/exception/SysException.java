package com.wemarket.wac.common.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * System exception with customize comment.
 **/
public class SysException extends NestedRuntimeException {

    private static final long serialVersionUID = 4123916665828659723L;

    /**
     * @param msg exception comment
     */
    public SysException(String msg){
        super(msg);
    }

    /**
     * @param msg exception comment
     * @param ex exception
     */
    public SysException(String msg, Throwable ex){
        super(msg, ex);
    }
}

