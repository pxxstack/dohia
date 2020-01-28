package com.pinyougou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;

@Component
public class SolrMessageListener implements MessageListener {

    @Autowired
    private SearchItemService searchItemService;
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage =(TextMessage)message;
        try {
            String text = textMessage.getText();
            List<TbItem> tbItems = JSON.parseArray(text, TbItem.class);
            System.out.println("集合的长度"+tbItems.size());
            searchItemService.saveList(tbItems);
            System.out.println("索引库更新成功");
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
