package com.example.totaldemo.config;

import com.example.totaldemo.filter.JwtAuthorizationFilter;
import com.example.totaldemo.util.MyAccessDeniedHandler;
import com.example.totaldemo.util.MyAuthenticationEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Spring Security配置类
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @Autowired
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    public SecurityConfiguration() {
        log.info("加载配置类：SecurityConfiguration");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        // 调用父类的方法得到AuthenticationManager
        return super.authenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfigurationSource source =   new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");	//同源配置，*表示任何请求都视为同源，若需指定ip和端口可以改为如“localhost：8080”，多个以“，”分隔；
        corsConfiguration.addAllowedHeader("Authorization");//header，允许哪些header，本案中使用的是token，此处可将*替换为token；
        corsConfiguration.addAllowedMethod("*");	//允许的请求方法，PSOT、GET等
        ((UrlBasedCorsConfigurationSource) source).registerCorsConfiguration("/**",corsConfiguration); //配置允许跨域访问的url
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super.configure(http); // 千万不要调用父类的同名方法

        // 白名单
        String[] urls = {
                "/swagger-ui",    // Knife4j在线API文档的资源
                "/userlogin/userloginfo",//登录
//                "/basic-api/userlogin/userloginfo",//登录
//                "/**/basic-api/userlogin/userloginfo",//登录
//                "/**/#/login",//登录
//                "/**/login",//登录
                "/favicon.ico",     // 网站图标文件
                "/",                // 根页面，通常是主页
                "/*.html",          // 任何html
                "/**/*.html",       // 任何目录下的html
                "/**/*.css",        // 任何目录下的css
                "/**/*.js",         // 任何目录下的js

                "/swagger-ui/**",
                "/swagger-resources/**",
                "/profile/**",
                "/profile/**",
                "/v3/**",
                "/v2/**",
                "/basic-api/**",
                "/**/*.jsp",
                "/swagger-resources/**",    // Knife4j在线API文档的资源
                "/v2/api-docs/**",          // Knife4j在线API文档的资源
                "/favicon.ico",     // 网站图标文件
                "/",                // 根页面，通常是主页
                "/*.html",          // 任何html
                "/**/*.html",       // 任何目录下的html
                "/**/*.css",        // 任何目录下的css
                "/**/*.js",         // 任何目录下的js
                "/user/login",     // 登录
                "/user/logout",    // 登录
                "/*/sso/checkLogin", // 验证登录
                "/*/sso/home"
        };

        // 配置各请求路径是否需要通过认证
        http.authorizeRequests() // 对请求进行授权
                .antMatchers(urls) // 匹配某些路径
                .permitAll() // 允许此前匹配的路径直接访问，不需要通过认证或授权
                // .antMatchers(HttpMethod.OPTIONS,"/**")
                // .permitAll()
                .anyRequest() // 除了以上配置过的其它任何路径
                .authenticated();
//                .and()
//                .formLogin()
//                .loginProcessingUrl("/userlogin/userloginfo"); // 需要通过认证

        // 允许跨域访问
        http.cors(); // 激活Spring Security框架内置的一个CorsFilter，允许跨域访问

        // 关于防伪造的跨域攻击，默认只针对POST / PUT / DELETE / PATCH请求
        // 禁用防伪造的跨域攻击，注意：禁用后是存在风险的
        http.csrf().disable();

        // 将JWT过滤器添加在Spring Security的UsernamePasswordAuthenticationFilter之前
        http.addFilterBefore(jwtAuthorizationFilter,
                UsernamePasswordAuthenticationFilter.class);

        // http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 不主动创建Session
        // http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER); // 从不创建Session

        //http.formLogin(); // 允许通过 /login 打开登录页面

        //权限不足处理器
        http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);
        //未认证处理器
        http.exceptionHandling().authenticationEntryPoint(myAuthenticationEntryPoint);

        // Session管理
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        http.sessionManagement().sessionFixation().none();
    }

}
