package com.example.demo.controller;

import com.example.demo.RetryProcess;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {

    @RequestMapping("/test")
    @RetryProcess(value = 3)
    public void testException(){
        System.out.println("执行业务！！！");
        throw new RuntimeException("测试重试异常");
    }
}
