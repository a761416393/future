package com.example.moxin.future;

import com.example.moxin.future.margerequest.CommandService;
import com.example.moxin.future.zookeeper.server.PayServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FutureApplication.class)
public class FutureApplicationTests {
    @Autowired
    CommandService commandService;
    @Test
    public void contextLoads() {
    }


    @Autowired
    PayServer payServer;

    @Test
    public void zookeeperTest() {
        payServer.pay("ali");
    }

    @Test
    public void mergeRequest () throws IOException {
        //模仿并发请求
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        for (int i=0;i<1000;i++){
            Thread thread = new Thread(()->{
                try {
                    countDownLatch.await();
                    int id= (int) (Math.random()*1000%15);
                    String name =commandService.queryName(id+"");
                    System.out.println("id"+id+"---name"+name);
                }catch (Exception e){
                    System.out.println("执行异常"+e);
                }
            });
            thread.setName("thread--"+i);
            thread.start();
            countDownLatch.countDown();

        }
        // 输入任意内容退出
        System.in.read();
    }


}
