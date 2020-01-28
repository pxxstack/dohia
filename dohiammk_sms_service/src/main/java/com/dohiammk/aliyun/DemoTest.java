package com.dohiammk.aliyun;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoTest {

    @RequestMapping("/test")
    public String demo(){
        return "helloWorld95";
    }
}
