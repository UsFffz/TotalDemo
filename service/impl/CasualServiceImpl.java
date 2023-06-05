package com.example.totaldemo.service.impl;

import com.example.totaldemo.codenum.WarnEnum;
import com.example.totaldemo.codenum.WarnMessage;
import com.example.totaldemo.mapper.TestPlayMapper;
import com.example.totaldemo.pojo.entity.BookTestRabbit;
import com.example.totaldemo.pojo.entity.KafKaWarn;
import com.example.totaldemo.service.CasualService;
import com.example.totaldemo.util.BookBuySuccess;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class CasualServiceImpl implements CasualService {

    @Autowired
    private TestPlayMapper mapper;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private BookBuySuccess bookBuySuccess;

    @Override
    @Async("normalThreadPool")
    public Map<String,Object> buyBook(Integer bookId) {
        BookTestRabbit bookTestRabbit = mapper.selectBookById(bookId);
        Map<String,Object> map = new HashMap();
        if (bookTestRabbit == null){
            map.put("message","该书不存在");
            return map;
        }
        Integer i = mapper.buyBook(bookId);
        if (i <= 0){
            KafKaWarn kafKaWarn = new KafKaWarn();
            kafKaWarn.setBookId(bookTestRabbit.getBookId())
                     .setWarnCode(WarnEnum.ERROR_CODE.getNum())
                     .setWarnMessage(WarnMessage.WARN_MESSAGE_ERROR)
                     .setBookName(bookTestRabbit.getName())
                     .setInventory(bookTestRabbit.getInventory());
            Gson gson = new Gson();
            String message =  gson.toJson(kafKaWarn);
            kafkaTemplate.send("kafkawarn",message);
            map.put("message","因未知原因购买失败");
            return map;
        }
        bookBuySuccess.userSuccessLog(bookId,bookTestRabbit.getName(),bookTestRabbit.getInventory());
        map.put("message","购买成功");
        return map;
    }

    @Override
    public Map<String, Object> bookList() {
        List<BookTestRabbit> list = mapper.selectAllBook();
        Map<String,Object> map = new HashMap<>();
        map.put("message","查询成功");
        map.put("code",200);
        map.put("data",list);
        return map;
    }

}
