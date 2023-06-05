package com.example.totaldemo.aop.log;


import com.example.totaldemo.pojo.entity.AopEntity;
import com.example.totaldemo.util.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.example.totaldemo.aop.anotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

@Aspect
@Component
@Slf4j
public class LogService {
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Pointcut("@annotation(com.example.totaldemo.aop.anotation.UserLog)")
    private void log(){

    }

    @Around("log() && @annotation(userLog)")
    public Object userLog(ProceedingJoinPoint pjd, UserLog userLog) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String remoteAddr = LoginUtils.getIpAddress(request);//如果是localhost访问会记录ipv6格式的本机地址,正常
        String userAgent=request.getHeader("User-Agent");
        AopEntity aopEntity = new AopEntity();
        aopEntity.setDate(getNowDateStr())
                 .setUserId(1017)
                 .setResult(3)
                 .setOperate(userLog.value())
                 .setRemoteAddr(remoteAddr)
                 .setUserAgent(userAgent);
        mongoTemplate.save(aopEntity,"userlog");
        Map<String,Object> map = (Map<String, Object>) pjd.proceed();
        String s = String.valueOf(map.get("message"));
        String message = s.substring(2,4);
        System.out.println(message);
        if (!message.equals("成功")){
            aopEntity.setDate(getNowDateStr())
                     .setResult(2);
            mongoTemplate.save(aopEntity,"userlog");
            return map;
        }
        aopEntity.setDate(getNowDateStr())
                 .setResult(1);
        mongoTemplate.save(aopEntity,"userlog");
        return map;
    }


    /**
     * 获取当前时间
     * @return
     */
    private String getNowDateStr(){
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simple.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String nowdate = simple.format(new Date());
        nowdate = nowdate.substring(0,14) + "00:00" ;
        return nowdate;
    }
}
