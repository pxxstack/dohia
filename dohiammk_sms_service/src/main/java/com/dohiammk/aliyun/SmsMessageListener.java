package com.dohiammk.aliyun;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 消息监听类
 */
@Component
public class SmsMessageListener {

    @Autowired
    private SmsUtil smsUtil;

    @JmsListener(destination="dohiaMessage")
    public void sendSms(Map<String,String> map){

        try {
            SendSmsResponse response = smsUtil.sendSms(
                    map.get("phone"),
                    map.get("template_code"),
                    map.get("sign_name"),
                    map.get("param")
            );
            System.out.println("Code=" + response.getCode());
            System.out.println("Message=" + response.getMessage());
            System.out.println("RequestId=" + response.getRequestId());
            System.out.println("BizId=" + response.getBizId());
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
