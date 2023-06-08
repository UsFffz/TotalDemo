package com.example.totaldemo.util;

import com.example.totaldemo.ex.ServiceCode;
import com.example.totaldemo.ex.ServiceException;
import com.example.totaldemo.secrity.AuthenticationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GetAuthenticationInfo {

    public AuthenticationInfo getUserInfo(){
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken)
                        SecurityContextHolder.getContext().getAuthentication();

        if (authenticationToken==null){
            throw new ServiceException(ServiceCode.ERR_NO_LOGIN,"请登录");
        }

        log.warn("信息:{}",authenticationToken.getCredentials());
        AuthenticationInfo authenticationInfo =
                (AuthenticationInfo) authenticationToken.getCredentials();

        return authenticationInfo;
    }

    public Long getUserID(){
        Long userid = getUserInfo().getId();
        return userid;
    }
}
