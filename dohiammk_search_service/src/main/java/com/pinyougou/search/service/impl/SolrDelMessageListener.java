package com.pinyougou.search.service.impl;

import com.pinyougou.search.service.SearchItemService;
import org.apache.commons.collections.ArrayStack;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.Serializable;
import java.util.Arrays;

public class SolrDelMessageListener implements MessageListener {

    @Autowired
    private SearchItemService searchItemService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage idsMessage =(ObjectMessage)message;
        try {
            //一定要从消息中得到消息 因为消息被封装啦
            Long[] ids =(Long[]) idsMessage.getObject();
            System.out.println("监听到消息啦");
            searchItemService.delGoods(Arrays.asList(ids));
            System.out.println("索引库更新成功");
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
