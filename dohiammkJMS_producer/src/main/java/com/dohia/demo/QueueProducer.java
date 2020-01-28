package com.dohia.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Component
public class QueueProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Destination queueDohiaDestination;

    public void testQueueProducer(final String text){
        jmsTemplate.send(queueDohiaDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                //发送队列消息
                return session.createTextMessage(text);
            }
        });
    }
}
