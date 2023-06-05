package com.example.totaldemo.filter;

import com.alibaba.fastjson.JSON;
import com.example.totaldemo.ex.ServiceCode;
import com.example.totaldemo.mapper.UserLoginMapper;
import com.example.totaldemo.secrity.AuthenticationInfo;
import com.example.totaldemo.util.LoginUtils;
import com.example.totaldemo.web.JsonResult;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>解析JWT的过滤器</p>
 *
 * <ol>
 * <li>首先，清除SecurityContext中的认证信息</li>
 * <li>如果客户端没有携带JWT，则放行，由后续的组件进行处理</li>
 * <li>如果客户端携带了有效的JWT，则解析，并将解析结果用于创建认证对象，最终，将认证对象存入到SecurityContext</li>
 * </ol>
 *
 * @version 0.0.2
 */
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    private UserLoginMapper userMapper;

    @Value("${csmall.jwt.secret-key}")
    private String secretKey;

    @Autowired
    private RedisTemplate redisTemplate;

    public JwtAuthorizationFilter() {
        log.warn("创建过滤器：JwtAuthorizationFilter");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.warn("处理JWT的过滤器开始执行……");

        // 清除SecurityContext中原有的认证信息
        // 避免曾经成功访问过，后续不携带JWT也能被视为“已认证”
        SecurityContextHolder.clearContext();

        // 尝试从请求头中获取JWT数据
        String jwt = request.getHeader("Authorization");
        System.out.println("尝试从请求头中获取JWT数据:" + jwt);

        // 判断客户端是否携带了有效的JWT数据，如果没有，直接放行
        if (!StringUtils.hasText(jwt) || jwt.length() < 113) {
            System.out.println("获取到的JWT被视为【无效】，过滤器执行【放行】");
            filterChain.doFilter(request, response);
            return;
        }

        // 程序执行到此处，表示客户端携带了有效的JWT，则尝试解析
        System.out.println("获取到的JWT被视为【有效】，则尝试解析……");
        Claims claims = null;

        response.setContentType("application/json; charset=utf-8");

        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("解析JWT失败：{}：{}", e.getClass().getName(), e.getMessage());
            Integer serviceCode = ServiceCode.ERR_JWT_EXPIRED.getValue();
            String message = "登录信息已过期，请重新登录！";
            JsonResult<Void> jsonResult = JsonResult.fail(serviceCode.toString(), message);
            String jsonString = JSON.toJSONString(jsonResult);
            PrintWriter writer = response.getWriter();
            writer.println(jsonString);
            writer.close();
            return;
        } catch (SignatureException e) {
            log.warn("解析JWT失败：{}：{}", e.getClass().getName(), e.getMessage());
            Integer serviceCode = ServiceCode.ERR_JWT_PARSE.getValue();
            String message = "无法获取到有效的登录信息，请重新登录！";
            JsonResult<Void> jsonResult = JsonResult.fail(serviceCode.toString(), message);
            String jsonString = JSON.toJSONString(jsonResult);
            PrintWriter writer = response.getWriter();
            writer.println(jsonString);
            writer.close();
            return;
        } catch (MalformedJwtException e) {
            log.warn("解析JWT失败：{}：{}", e.getClass().getName(), e.getMessage());
            Integer serviceCode = ServiceCode.ERR_JWT_PARSE.getValue();
            String message = "无法获取到有效的登录信息，请重新登录！";
            JsonResult<Void> jsonResult = JsonResult.fail(serviceCode.toString(), message);
            String jsonString = JSON.toJSONString(jsonResult);
            PrintWriter writer = response.getWriter();
            writer.println(jsonString);
            writer.close();
            return;
        } catch (Throwable e) {
            log.warn("解析JWT失败：{}：{}", e.getClass().getName(), e.getMessage());
            Integer serviceCode = ServiceCode.ERR_JWT_PARSE.getValue();
            String message = "无法获取到有效的登录信息，请重新登录！";
            JsonResult<Void> jsonResult = JsonResult.fail(serviceCode.toString(), message);
            String jsonString = JSON.toJSONString(jsonResult);
            PrintWriter writer = response.getWriter();
            writer.println(jsonString);
            writer.close();
            e.printStackTrace();
            return;
        }

        // 从JWT中获取数据
        Long id = claims.get("userid", Long.class);
        System.out.println("从JWT中解析得到【id】的值:" + id);
        Map<String,Object> map = null;
        if (redisTemplate.hasKey(id.toString())){
            Map<String,Object> map1 = (Map<String, Object>) redisTemplate.boundValueOps(id.toString()).get();
            String redisJWT = (String) map1.get("jwt");
            if (!redisJWT.equals(jwt)) {
                userMapper.freezeUser(Integer.valueOf(String.valueOf(id)));
                Integer serviceCode = ServiceCode.ERR_JWT_PARSE.getValue();
                String message = "警告,经系统检测,该用户信息已出现泄露,现已冻结,如需解开请联系管理员周大漂亮";
                JsonResult<Void> jsonResult = JsonResult.fail(serviceCode.toString(), message);
                String jsonString = JSON.toJSONString(jsonResult);
                PrintWriter writer = response.getWriter();
                writer.println(jsonString);
                writer.close();
                return;
            }else if (redisJWT.equals(jwt)){
                Integer state = userMapper.selectUserState(Integer.valueOf(String.valueOf(id)));
                if (state != 1){
                    Integer serviceCode = ServiceCode.ERR_JWT_PARSE.getValue();
                    String message = "警告,该用户已被冻结,如需解开请联系管理员周大漂亮";
                    JsonResult<Void> jsonResult = JsonResult.fail(serviceCode.toString(), message);
                    String jsonString = JSON.toJSONString(jsonResult);
                    PrintWriter writer = response.getWriter();
                    writer.println(jsonString);
                    writer.close();
                    return;
                }
                map = (Map<String, Object>) redisTemplate.boundValueOps(id.toString()).get();
            }
        }else {
            Integer state = userMapper.selectUserState(Integer.valueOf(String.valueOf(id)));
            if (state != 1){
                Integer serviceCode = ServiceCode.ERR_JWT_PARSE.getValue();
                String message = "警告,该用户已被冻结,如需解开请联系管理员周大漂亮";
                JsonResult<Void> jsonResult = JsonResult.fail(serviceCode.toString(), message);
                String jsonString = JSON.toJSONString(jsonResult);
                PrintWriter writer = response.getWriter();
                writer.println(jsonString);
                writer.close();
                return;
            }
            map.put("userid",id);
            map.put("username",claims.get("username",String.class));
            map.put("authorities",claims.get("authorities",String.class));
            redisTemplate.boundValueOps(String.valueOf(id)).set(map,1000 * 60 * 60 * 12, TimeUnit.MILLISECONDS);
        }
        String username = map.isEmpty() ? claims.get("username", String.class) : (String) map.get("username");
        System.out.println("======================== 警告" + "解析username值为:" + username + " =====================");
        String authorityListString = map.isEmpty() ? claims.get("authorities", String.class) : (String) map.get("authorities");
        System.out.println("======================== 警告" + "解析权限为:" + authorityListString + " ================================");
        log.warn("从JWT中解析得到【authorities】的值：{}", authorityListString);
        //先补充数据
        String remoteAddr = LoginUtils.getIpAddress(request);//如果是localhost访问会记录ipv6格式的本机地址,正常
        log.info("远程ip地址:{}",remoteAddr);
        String userAgent=request.getHeader("User-Agent");
        log.info("远程客户端:{}",userAgent);
        // 准备权限，将封装到认证信息中
        List<SimpleGrantedAuthority> authorityList
                = JSON.parseArray(authorityListString, SimpleGrantedAuthority.class);
        System.out.println(authorityList.toString());
        AuthenticationInfo authenticationInfo = new AuthenticationInfo();
        authenticationInfo.setAuthorities(authorityList);
        authenticationInfo.setUsername(username);
        authenticationInfo.setId(id);
        // 准备存入到SecurityContext的认证信息
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;
        usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                username,authenticationInfo,authorityList
        );
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // 将认证信息存入到SecurityContext中
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(usernamePasswordAuthenticationToken);
        log.warn("过滤器执行【放行】");
        filterChain.doFilter(request, response);
    }

}
