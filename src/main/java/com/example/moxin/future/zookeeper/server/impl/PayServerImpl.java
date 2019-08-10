package com.example.moxin.future.zookeeper.server.impl;

import com.example.moxin.future.zookeeper.config.PayConfig;
import com.example.moxin.future.zookeeper.server.PayServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;


@Service
public class PayServerImpl implements PayServer {

    @Autowired
    PayConfig payConfig;

    @Override
    public void pay(String type) {
        if("ali".equals(type)){
            String property = payConfig.getProperty("pay.alipay.url ");
            System.out.println("调用阿里接口"+property);
        }else if ("weixin".equals(type)){
            System.out.println("调用微信接口"+payConfig.getProperty("pay.weixin.url "));

        }else{
            System.out.println("配置文件"+ payConfig.getProperty("spring.datasource.password"));
        }
    }
}
