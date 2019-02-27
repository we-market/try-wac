package com.wemarket.wac.web;

import com.wemarket.wac.common.controller.BaseController;
import com.wemarket.wac.common.dto.BaseDTO;
import com.wemarket.wac.common.dto.BizErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class WacBaseController extends BaseController {

    protected int timeoutMilliSeconds = 12000;

    private final static Logger LOGGER = LoggerFactory.getLogger(WacBaseController.class);

    @Autowired
    @Qualifier("frontTaskExecutor")
    private ThreadPoolTaskExecutor frontTaskExecutor;

    /*@Override
    protected ThreadPoolTaskExecutor getFrontTaskExecutor() {
        return this.frontTaskExecutor;
    }
*/
    @Override
    protected <E extends BaseDTO, T>
    void postService(E requestDto,
                     T responseDto,
                     HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse,
                     Method callerMethod, String requestUri, BizErrors errors) {
        // TODO Auto-generated method stub
    }

    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    public void hello(){

    }
}
