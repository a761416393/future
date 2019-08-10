package com.example.moxin.future;

import com.example.moxin.future.zookeeper.server.PayServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FutureApplication.class)
public class FutureApplicationTests {

    @Test
    public void contextLoads() {
    }


    @Autowired
    PayServer payServer;

    @Test
    public void zookeeperTest() {
        payServer.pay("ali");
    }
}
