package com.example.totaldemo.kafka;


import com.example.totaldemo.pojo.entity.KafKaWarn;
import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class kafkaWarn {

    @Autowired
    private MongoTemplate mongoTemplate;

    @KafkaListener(topics = "kafkawarn")
    public void received(ConsumerRecord<String,String> record){
        System.out.println("kafka已接受到警告消息");
        System.out.println("kafka已接受到消息,消息内容为"+record.value());
        String json = record.value();
        Gson gson = new Gson();
        KafKaWarn kafKaWarn = gson.fromJson(json,KafKaWarn.class);
        String mongoName = "niuBi";
        System.out.println("准备发送到mongo中进行记录");
        mongoTemplate.save(kafKaWarn,mongoName);
        System.out.println("发送成功");
    }
}
