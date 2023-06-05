package com.example.totaldemo.pojo.entity;

import com.example.totaldemo.codenum.WarnEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class KafKaWarn implements Serializable {

    /**
     *警告码
     */
    private Integer warnCode;

    /**
     *警告信息
     */
    private String warnMessage;

    /**
     * 书籍ID
     */
    private Integer bookId;

    /**
     * 书籍名称
     */
    private String bookName;

    /**
     * 书籍现有库存
     */
    private Integer inventory;


}
