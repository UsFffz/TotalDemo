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
public class CouponEntity implements Serializable {

    /**
     * 优惠卷id
     */
    private Integer couponId;

    /**
     * 优惠卷名字
     */
    private String couponName;

    /**
     * 优惠卷数量
     */
    private Integer num;

    /**
     * 优惠卷效果
     */
    private String couponEffect;

}
