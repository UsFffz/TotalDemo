package com.example.totaldemo.ex.handler;



import com.example.totaldemo.ex.ServiceCode;
import com.example.totaldemo.ex.ServiceException;
import com.example.totaldemo.web.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.net.ssl.SSLException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public JsonResult<Void> handlerServiceException(ServiceException e) {
        log.warn("开始处理ServiceException,servicecode = {},message = {}", e.getServiceCode(), e.getMessage());
        return JsonResult.fail(e);
    }

    @ExceptionHandler({FileNotFoundException.class, NullPointerException.class})
    public JsonResult<Void> handlerThrowable(Throwable e) {
        log.debug("处理throwAble");
        e.printStackTrace();
        Integer serviceCode = 98000;
        String message = "程序出现未知错误,请练习管理员或客服尽快解决";
        return JsonResult.fail(serviceCode.toString(), message);
    }

    @ExceptionHandler
    public JsonResult<Void> handleBindException(BindException e) {
        log.debug("处理BindException");

        Integer serviceCode = ServiceCode.ERR_BAD_REQUEST.getValue();

        StringBuilder messageBuilder = new StringBuilder();
        List<FieldError> fieldErrors = e.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            messageBuilder.append(fieldError.getDefaultMessage());
        }

        String message = messageBuilder.toString();
        return JsonResult.fail(serviceCode.toString(), message);
    }

    @ExceptionHandler({
            InternalAuthenticationServiceException.class,
            BadCredentialsException.class
    })
    public JsonResult<Void> handleAuthenticationException(AuthenticationException e) {
        log.debug("处理AuthenticationException");
        log.debug("异常类型：{}", e.getClass().getName());
        log.debug("异常信息：{}", e.getMessage());
        Integer serviceCode = ServiceCode.ERR_UNAUTHORIZED.getValue();
        String message = "登录失败，用户名或密码错误！";
        return JsonResult.fail(serviceCode.toString(), message);
    }

    @ExceptionHandler
    public JsonResult<Void> handleDisabledException(DisabledException e) {
        log.debug("处理DisabledException");
        Integer serviceCode = ServiceCode.ERR_UNAUTHORIZED_DISABLED.getValue();
        String message = "登录失败，此账号已经禁用！";
        return JsonResult.fail(serviceCode.toString(), message);
    }

    @ExceptionHandler
    public JsonResult<Void> handleAccessDeniedException(org.springframework.security.access.AccessDeniedException e) {
        log.debug("处理AccessDeniedException");
        Integer serviceCode = ServiceCode.ERR_FORBIDDEN.getValue();
        String message = "请求失败，当前账号无此操作权限！";
        return JsonResult.fail(serviceCode.toString(), message);
    }


    @ExceptionHandler
    public JsonResult<Void> handlerGlobalExceptionHandler(AccessDeniedException e) {
        Integer serviceCode = ServiceCode.ERR_NOTFOUNDID.getValue();
        String Message = "权限不足,不可进入";
        return JsonResult.fail(serviceCode.toString(), Message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String exceptionHandler2(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        if (result.hasErrors()) {
            return result.getAllErrors().get(0).getDefaultMessage();
        }
        return "参数不可为空！";
    }

    @ExceptionHandler(SSLException.class)
    public JsonResult<Void> SSLException(IOException e) {
        log.error("开始处理SSLException异常");
        Integer Code = 470;
        e.printStackTrace();
        String failmessage = "非https访问，已拒绝";
        return JsonResult.fail(Code.toString(),failmessage);
    }


}
