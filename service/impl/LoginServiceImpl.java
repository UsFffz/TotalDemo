package com.example.totaldemo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.totaldemo.pojo.dto.UserLoginDTO;
import com.example.totaldemo.secrity.AdminDetails;
import com.example.totaldemo.service.LoginService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Value("${csmall.jwt.secret-key}")
    private String yan;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String userLoginService(UserLoginDTO userLoginDTO) {
        Authentication authentication =
                 new UsernamePasswordAuthenticationToken(
                         userLoginDTO.getUsername(),
                         userLoginDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authentication);
        AdminDetails adminDetails = (AdminDetails) authenticate.getPrincipal();
        Long userid  =  adminDetails.getId();
        if (redisTemplate.hasKey(userid.toString())){
            Map<String,Object> map1 = (Map<String, Object>) redisTemplate.boundValueOps(userid.toString()).get();
            String jwt = (String) map1.get("jwt");
            return jwt;
        }
        String username = adminDetails.getUsername();
        Collection<GrantedAuthority> roles = adminDetails.getAuthorities();
        String roleString = JSONObject.toJSONString(roles);
        Map<String,Object> map = new HashMap<>();
        map.put("userid",userid);
        map.put("username",username);
        map.put("authorities",roleString);
        Date expiration = new Date(System.currentTimeMillis() +  1000 * 60 * 60 * 12);
        String jwt = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                // Payload
                .setClaims(map)
                .setExpiration(expiration)
                // Signature
                .signWith(SignatureAlgorithm.HS256, yan)
                .compact();
        map.put("jwt",jwt);
        redisTemplate.boundValueOps(String.valueOf(userid)).set(map,1000 * 60 * 60 * 12, TimeUnit.MILLISECONDS);
        return jwt;
    }
}
