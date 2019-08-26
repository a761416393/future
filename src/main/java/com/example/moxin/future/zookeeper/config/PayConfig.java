package com.example.moxin.future.zookeeper.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.RetryOneTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Component
public class PayConfig {
    Properties properties = new Properties();
    @Value("${config.zookeeper.url}")
    private String zkUrl;
    @Value("${config.zookeeper.nodename}")
    private String nodeName;

    @Autowired
    Environment environment;

    @PostConstruct
    public void init() throws Exception {
        //从数据库中读取
        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(zkUrl,new RetryOneTime(1000));
        zkClient.start();//启动与zookeeper的连接
        //获取节点
        List<String> nodeKeys =zkClient.getChildren().forPath("/"+nodeName);
        //写一个中间变量Map
        Map<String,String> tempMap = new HashMap<String,String>();

        for (String nodeKey:nodeKeys){
            //获取kezkClienty对应的值
            byte[] valueBytes = zkClient.getData().forPath("/"+nodeName+"/"+nodeKey);
            tempMap.put(nodeKey,new String(valueBytes));
        }
        //设置到Properties中
        properties.putAll(tempMap);

        //阿监听zookeeper的配置比变化
        TreeCache treeCache = new TreeCache(zkClient,"/"+nodeName);
        treeCache.start();//开始监听
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                TreeCacheEvent.Type type = treeCacheEvent.getType();
                switch (type){
                    case NODE_UPDATED:
                        System.out.println("发生了配置比变化");
                        String key  = treeCacheEvent.getData().getPath().replace("/"+nodeName+"/","");
                        String value = new String(treeCacheEvent.getData().getData());
                        properties.put(key,value);
                        break;
                        default:
                            break;
                }
            }
        });
    }

    public Properties getProperties() {
        return properties;
    }

    public String  getProperty(String key) {
        String value;
        value = properties.getProperty(key);
        if(value==null ||"".equals(value)){
           value =  environment.getProperty(key);
        }
        return value;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
