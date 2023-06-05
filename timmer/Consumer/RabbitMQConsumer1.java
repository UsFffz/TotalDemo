package com.example.totaldemo.timmer.Consumer;


import com.example.totaldemo.mapper.TestPlayMapper;
import com.example.totaldemo.pojo.entity.BookTestRabbit;
import com.example.totaldemo.rabbit.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Slf4j
public class RabbitMQConsumer1 {

    @Autowired
    private TestPlayMapper testPlayMapper;


    @RabbitHandler
    @Transactional
    @RabbitListener(queues = RabbitConfig.STOCK_QUEUE)
    public void JieShou(BookTestRabbit bookTestRabbit){
        System.out.println("警告,已经开始添加图书");
        testPlayMapper.insertBook(bookTestRabbit);
        System.out.println("添加成功");
    }
}
