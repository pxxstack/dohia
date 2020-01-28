package com.dohia.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-jms-producer.xml")
public class DemoTestTopic {

    @Autowired
    private TopicProducer topicProducer;

    @Test
    public void testMyTopic(){
        System.out.println("进来了吗？");
        topicProducer.testTopic("welcome to new York");
    }
}
