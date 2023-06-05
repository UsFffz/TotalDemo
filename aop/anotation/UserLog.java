package com.example.totaldemo.aop.anotation;



import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLog {
    String value();
}
