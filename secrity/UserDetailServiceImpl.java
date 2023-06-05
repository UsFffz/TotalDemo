package com.example.totaldemo.secrity;


import com.example.totaldemo.mapper.UserLoginMapper;
import com.example.totaldemo.pojo.entity.UserLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserLoginMapper userLoginMapper;



    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println("=================== 警告 spring security 正在自动调用 loadUserByUsername方法 ========================");
        UserLogin userLogin = userLoginMapper.userLoginUser(s);
        if (userLogin == null){
            String message = "登录失败，用户名不存在！";
            System.out.println("==============注意  " + message + "===========================");
            log.warn(message);
            throw new BadCredentialsException(message);
        }
        List<String> roleList = userLoginMapper.selectUserRole(userLogin.getUserID());
        userLogin.setPermissions(roleList);
        List<GrantedAuthority> list = new ArrayList<>();
        for (String role : roleList) {
            list.add(new SimpleGrantedAuthority(role));
        }
        AdminDetails adminDetails = new AdminDetails(userLogin.getUsername(),userLogin.getPassword(),list);
        adminDetails.setId(Long.valueOf(String.valueOf(userLogin.getUserID())));
        System.out.println("=======================注意 即将向框架返回 admin 数据==========================================");
        return adminDetails;
    }
}
