package com.example.totaldemo.kafka;


import com.example.totaldemo.pojo.entity.KafkaSuccess;
import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaSuccessLog {
    @Autowired
    private MongoTemplate mongoTemplate;

    @KafkaListener(topics = "kafkasuccess")
    public void kafkaSuccess(ConsumerRecord<String,String> record){
        System.out.println("kafka已经接受到成功日志消息");
        String value = record.value();
        Gson gson = new Gson();
        KafkaSuccess kafkaSuccess = gson.fromJson(value,KafkaSuccess.class);
        System.out.println("接受的消息为:" + kafkaSuccess.toString());
        mongoTemplate.save(kafkaSuccess,"successlog");
        System.out.println("准备发送到mongo中准备录入到mongo");
    }
}
