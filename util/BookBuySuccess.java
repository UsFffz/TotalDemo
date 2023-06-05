package com.example.totaldemo.util;


import com.example.totaldemo.codenum.WarnEnum;
import com.example.totaldemo.codenum.WarnMessage;
import com.example.totaldemo.mapper.TestPlayMapper;
import com.example.totaldemo.pojo.entity.KafkaSuccess;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookBuySuccess {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void userSuccessLog(Integer bookId,String bookName,Integer inventory){
        KafkaSuccess kafkaSuccess = new KafkaSuccess();
        kafkaSuccess.setSuccessCode(WarnEnum.SUCCESS_CODE.getNum())
                    .setBookName(bookName)
                    .setBookId(bookId)
                    .setInventory(inventory)
                    .setSuccessMessage(WarnMessage.WARN_MESSAGE_SUCCESS);
        Gson gson = new Gson();
        String message = gson.toJson(kafkaSuccess);
        kafkaTemplate.send("kafkasuccess",message);
    }
}
