package com.example.totaldemo.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserLogin implements Serializable {

    /**
     * 用户id
     */
    private Integer userID;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户权限
     */
    private List<String> permissions;

    /**
     * 用户状态
     */
    private Integer userState;
}
