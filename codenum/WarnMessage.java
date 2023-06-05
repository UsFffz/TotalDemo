package com.example.totaldemo.codenum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * 警告信息专用类
 */
@Data
@Accessors(chain = true)
public class WarnMessage {

    public static final String WARN_MESSAGE_INVENTORY = "该书已即将告罄,请尽快补充";

    public static final String WARN_MESSAGE_INSUFFICIENT = "该书库存已无,请补充";

    public static final String WARN_MESSAGE_ERROR = "因未知原因购买失败,请手动查看原因";

    public static final String WARN_MESSAGE_SUCCESS = "购买成功";

}
