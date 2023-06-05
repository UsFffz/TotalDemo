package com.example.totaldemo.secrity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Data
@ApiModel("自定义认证框架数据封装")
public class AuthenticationInfo {
    /**
     * 用户id 可以是admin用户也可以是普通user用户id
     */
    @ApiModelProperty(value="用户id")
    private Long id;
    @ApiModelProperty(value="用户名")
    private String username;
    @ApiModelProperty(value="用户权限列表")
    List<SimpleGrantedAuthority> authorities;
}
