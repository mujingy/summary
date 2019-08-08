package com.example.demo;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RetryProcess {
    //重试次数
    int value() default 1;
}
