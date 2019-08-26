package com.example.moxin.future.margerequest;

import com.example.moxin.future.mybatisTest.dao.TestMapper;
import com.example.moxin.future.mybatisTest.dto.TestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class CommandService {
    @Autowired
    TestMapper testMapper;

    //定义一个任务队列
    private  LinkedBlockingQueue<Request> queue  = new LinkedBlockingQueue();

    @PostConstruct
    private void init(){
        //初始化一个线程池
        ScheduledExecutorService scheduledExecutorService= Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(()->{
            //判断任务队列是否是0
            int size=queue.size();
            if(size==0){
                return;
            }else{
                List<Request> list = new ArrayList();
                List<String>  ids = new ArrayList<>();
                for (int i=0;i<size;i++){
                    Request request = queue.poll();
                    ids.add(request.getId());
                    list.add(request);
                }
                List<TestModel> responce =testMapper.getNameByIds(ids);
                //将请求值放入Map中
                Map resultMap = new HashMap<>();
                for (TestModel result:responce){
                    resultMap.put(result.getId()+"",result.getName());
                }
                //循环之前的任务将值赋值
                for(Request request:list){
                    String  name = (String) resultMap.get(request.getId());
                    request.getCompletableFuture().complete(name);
                }
            }
        },0,10,TimeUnit.MILLISECONDS);
    }
    public String queryName(String id) throws ExecutionException, InterruptedException {
        //进来的所有请求都封装成request对象
        Request request = new Request();
        request.setId(id);
        CompletableFuture future = new CompletableFuture();
        request.setCompletableFuture(future);
        //将请求放到任务队列中
        queue.add(request);
        String name =(String) request.getCompletableFuture().get();
        System.out.println("id----"+id+"---name"+name);

        return name;
    }
}
