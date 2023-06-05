package com.example.totaldemo.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AopEntity implements Serializable {
    private Integer userId;
    private String date;
    private String operate;
    private Integer result; //1 为成功 2为失败 3为操作前
    /**
     * 本机ip地址
     */
    private String remoteAddr;

    /**
     * 远程ip地址
     */
    private String userAgent;

}
