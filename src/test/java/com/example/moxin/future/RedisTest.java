package com.example.moxin.future;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @Test
    public void test()  {

        // 保存字符串
        //stringRedisTemplate.opsForValue().set("aaa", "111");
        String a = stringRedisTemplate.opsForValue().get("aaa");

        System.out.println(a);

    }
}
