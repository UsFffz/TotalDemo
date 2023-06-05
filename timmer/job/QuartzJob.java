package com.example.totaldemo.timmer.job;


import com.example.totaldemo.pojo.entity.BookTestRabbit;
import com.example.totaldemo.rabbit.config.RabbitConfig;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;


public class QuartzJob implements Job {

    private static int bookId = 25;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        bookId++;
        BookTestRabbit bookTestRabbit = new BookTestRabbit();
        bookTestRabbit.setBookId(bookId)
                      .setMessage("这本书主要描述周巧双是怎么变漂亮的,有续集好几十部,每部限量100本,售完即止.")
                      .setName("周巧双是如何成为大漂亮的"+bookId)
                      .setNum(0)
                      .setInventory(100)
                      .setSale(17);
        /**
         * 发送信息RabbitMQ信息
         */
        System.out.println("准备发送");
        rabbitTemplate.convertSendAndReceive(RabbitConfig.STOCK_EX,RabbitConfig.STOCK_ROUT,bookTestRabbit);
        System.out.println("信息发送完成");
    }
}
