package com.example.totaldemo.timmer;


import com.example.totaldemo.timmer.job.QuartzJob;
import com.example.totaldemo.timmer.job.QuartzJob2;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail jobDetail(){
        return JobBuilder.newJob(QuartzJob.class)
                .withIdentity("initSeckills")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger jobTrigger(){
        CronScheduleBuilder cron=
                CronScheduleBuilder.cronSchedule("0 0 0/1 * * ? ");
        return TriggerBuilder.newTrigger()
                              .forJob(jobDetail())
                              .withIdentity("initTriggers")
                              .withSchedule(cron)
                              .build();
    }

    @Bean
    public JobDetail jobDetail2(){
        return JobBuilder.newJob(QuartzJob2.class)
                         .withIdentity("initSeckills2")
                         .storeDurably()
                         .build();
    }

    @Bean
    public Trigger jobTrigger2(){
        CronScheduleBuilder cronScheduleBuilder =
                           CronScheduleBuilder.cronSchedule("0/59 * * * * ?");
        return TriggerBuilder.newTrigger()
                             .forJob(jobDetail2())
                             .withIdentity("initTriggers2")
                             .withSchedule(cronScheduleBuilder)
                             .build();
    }
}
