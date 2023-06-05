package com.example.totaldemo.util;

import com.example.totaldemo.ex.ServiceCode;
import com.example.totaldemo.ex.ServiceException;
import com.example.totaldemo.web.JsonResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final ObjectMapper objectMapper=new ObjectMapper();
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //在这里返回一个包含着错误信息的对象数据
        JsonResult jsonResult=JsonResult.fail(new ServiceException(ServiceCode.ERR_NO_LOGIN,"您没有登录"));
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(jsonResult));
        httpServletResponse.flushBuffer();
    }
}
