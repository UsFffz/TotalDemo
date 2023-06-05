package com.example.totaldemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.example.totaldemo.mapper")
@EnableKafka
public class TotalDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TotalDemoApplication.class, args);
    }

}
