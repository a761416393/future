package com.example.moxin.future.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MyNioServer {
    public static final ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();


    public static void main(String []args) throws IOException {
        ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);//使用非阻塞IO
        serverSocketChannel.bind(new InetSocketAddress(8080));//绑定端口号

        System.out.println("服务启动8080端口");

        //创建选择器
        Selector selector = Selector.open();
        //将信道注册到选择器中

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            selector.select(1000);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> results =  selectionKeys.iterator();
            while (results.hasNext()){
                SelectionKey key = results.next();
                if(key.isAcceptable()){ //如果是一个新的连接
                   SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ);
                }else if(key.isReadable()){
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    key.cancel();
                    threadPool.submit(()->{
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        socketChannel.read(byteBuffer); // 不像InputStream， 是非阻塞Non-blocking IO
                        byteBuffer.flip();
                        String request = new String(byteBuffer.array());

                        // TODO 处理请求
                        System.out.println(request);

                        // 给一个返回值
                        String responseContent = "HTTP/1.1 200 OK\r\nContent-Length: 11\r\n\r\nHello World";
                        socketChannel.write(ByteBuffer.wrap(responseContent.getBytes()));
                        // 告诉selector 继续监听
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        return null;
                    });
                }
                results.remove();
            }
        }
    }
}
