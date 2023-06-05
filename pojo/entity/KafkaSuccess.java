package com.example.totaldemo.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class KafkaSuccess implements Serializable {

    /**
     * 用户id 暂定为只有1017(因为懒得弄安全和用户表还有各种权限表,太麻烦,后续如果没什么事情干弄一下也无所谓)
     */
    private Integer userId = 1017;

    /**
     * 书籍id
     */
    private Integer bookId;

    /**
     * 书籍名称
     */
    private String bookName;

    /**
     * 书籍剩余库存
     */
    private Integer inventory;

    /**
     * 成功信息
     */
    private String successMessage;

    /**
     * 成功代码编号
     */
    private Integer successCode;


}
