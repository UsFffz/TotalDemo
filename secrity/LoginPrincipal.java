package com.example.totaldemo.secrity;

import lombok.Data;

import java.io.Serializable;

/**
 * 当前登录的管理员当事人，专用于Spring Security
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Data
public class LoginPrincipal implements Serializable {

    /**
     * 数据id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    private String authorities;

}
