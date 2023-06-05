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
public class IPMessage implements Serializable {

    /**
     * 本机ip地址
     */
    private String remoteAddr;

    /**
     * 远程ip地址
     */
    private String userAgent;

    /**
     * 操作用户id
     */
    private Long id;

}
