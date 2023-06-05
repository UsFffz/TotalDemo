package com.example.totaldemo.rabbit.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 将rabbitMq交由spring管理
 */
@Configuration
public class RabbitConfig {
    //交换机1
    public static final String STOCK_EX = "stock_ex";
    //路由Key1
    public static final String STOCK_ROUT = "stock_rout";
    //队列1
    public static final String STOCK_QUEUE="stock_queue";

    //交换机2
    public static final String STOCK_EX_TWO = "stock_ex_two";
    //路由Key2
    public static final String STOCK_ROUT_TWO  = "stock_rout_two";
    //队列2
    public static final String STOCK_QUEUE_TWO = "stock_queue_two";


    /**
     * 交换机1
     * @return
     */
    @Bean
    public DirectExchange stockDirectExchange(){
        return new DirectExchange(STOCK_EX);
    }

    /**
     * 队列1
     * @return
     */
    @Bean
    public Queue stockQueue(){
        return new Queue(STOCK_QUEUE);
    }

    /**
     * 队列绑定1
     * @return
     */
    @Bean
    public Binding stockBinding(){
        return BindingBuilder.bind(stockQueue()).to(stockDirectExchange()).with(STOCK_ROUT);
    }

    /**
     * 交换机2
     * @return
     */
    @Bean
    public DirectExchange stockDirectExchangeTwo(){
        return new DirectExchange(STOCK_EX_TWO);
    }

    /**
     * 队列2
     * @return
     */
    @Bean
    public Queue stockQueueTwo(){
        return new Queue(STOCK_QUEUE_TWO);
    }

    /**
     * 队列绑定2
     * @return
     */
    @Bean
    public Binding stockBindingTwo(){
        return BindingBuilder.bind(stockQueueTwo()).to(stockDirectExchangeTwo()).with(STOCK_ROUT_TWO);
    }

}
