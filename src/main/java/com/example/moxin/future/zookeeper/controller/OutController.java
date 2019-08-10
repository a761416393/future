package com.example.moxin.future.zookeeper.controller;

import com.example.moxin.future.zookeeper.server.PayServer;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OutController {

    @Autowired
    PayServer payServer;

    @RequestMapping("/getProperty")
    public String getProperty(String type){
        payServer.pay(type);
        return null;
    }
}
