package com.wemarket.wac.web;

import com.wemarket.wac.biz.dto.QueryUserReq;
import com.wemarket.wac.biz.dto.QueryUserRsp;
import com.wemarket.wac.biz.service.UserService;
import com.wemarket.wac.common.dto.BizError;
import com.wemarket.wac.common.utils.BaseResponseStatus;
import io.netty.channel.unix.Errors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆流程url
 * Created by justinli on 2019/3/11
 **/
@RequestMapping("/login")
public class LoginController extends WacBaseController {

    private final static Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("queryuser")
    public DeferredResult<QueryUserRsp> queryUser(@RequestBody QueryUserReq requestBody, HttpServletRequest httpRequest, HttpServletResponse httpResponse){

        QueryUserRsp timeoutDTO = new QueryUserRsp();
        timeoutDTO.setCode(BaseResponseStatus.SYS_TIMEOUT.getCode());
        timeoutDTO.setMsg(BaseResponseStatus.SYS_TIMEOUT.getMessage());

        return this.execute(httpRequest, httpResponse, requestBody, timeoutMilliSeconds, (request, bizErrors) -> {
            try{
                return userService.queryUser(request);
            } catch (Exception e) {
                LOGGER.error("LoginController queryuser:", e);
                bizErrors.reject(BaseResponseStatus.INTERNAL_SERVER_ERROR.getCode(), null, BaseResponseStatus.INTERNAL_SERVER_ERROR.getMessage());
                return new QueryUserRsp();
            }
        }, timeoutDTO, null
        );
    }
}
