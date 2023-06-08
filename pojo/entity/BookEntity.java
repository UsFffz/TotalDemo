package com.example.totaldemo.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BookEntity  implements Serializable {
    /**
     * 书籍id
     */
    private Integer bookId;

    /**
     * 书籍名称
     */
    private String name;

    /**
     * 书籍售量
     */
    private Integer num;

    /**
     * 书籍信息(简介)
     */
    private String message;

    /**
     * 书籍库存
     */
    private Integer inventory;

    /**
     * 书籍售价
     */
    private BigDecimal sale;

    /**
     * 该书籍购买后所加积分
     */
    private Integer integral;
}
