package com.wemarket.wac.web;

import com.wemarket.wac.common.controller.BaseController;
import com.wemarket.wac.common.dto.BaseDTO;
import com.wemarket.wac.common.dto.BaseResponseDTO;
import com.wemarket.wac.common.dto.BizErrors;
import com.wemarket.wac.common.dto.WebMessage;
import com.wemarket.wac.common.utils.BaseResponseStatus;
import com.wemarket.wac.common.utils.ExecServiceTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class WacBaseController extends BaseController {

    protected int timeoutMilliSeconds = 12000;

    private final static Logger LOGGER = LoggerFactory.getLogger(WacBaseController.class);

    @Autowired
    @Qualifier("frontTaskExecutor")
    private ThreadPoolTaskExecutor frontTaskExecutor;

    public void setFrontTaskExecutor(ThreadPoolTaskExecutor frontTaskExecutor) {
        this.frontTaskExecutor = frontTaskExecutor;
    }

    @Override
    protected ThreadPoolTaskExecutor getFrontTaskExecutor() {
        return this.frontTaskExecutor;
    }

    protected <E extends BaseDTO> BaseResponseDTO preService(E requestDto,
                                                             HttpServletRequest httpRequest, HttpServletResponse httpResponse, Method callerMethod,
                                                             String requestUri) {
        return super.preService(requestDto, httpRequest, httpResponse, callerMethod, requestUri);
    }

    @Override
    protected <E extends BaseDTO, T> void postService(E requestDto,
                                                      T responseDto, HttpServletRequest httpServletRequest,
                                                      HttpServletResponse httpServletResponse, Method callerMethod, String requestUri, BizErrors errors) {

    }
    protected <T> WebMessage<T> toWebMessage(T t) {
        WebMessage<T> webMessage = new WebMessage<T>();
        webMessage.setResult(t);
        webMessage.setResponseStatus(BaseResponseStatus.SUCCESS);
        return webMessage;
    }

    protected <E extends BaseDTO, T> DeferredResult<T> execute(
            HttpServletRequest httpServletRequest, HttpServletResponse httpServlvetResponse,
            E requestDto, long timeoutMilliSeconds, ExecServiceTemplate<E, T> template) {
        return this.execute(httpServletRequest, httpServlvetResponse, requestDto, timeoutMilliSeconds, template, null, null);
    }

}
