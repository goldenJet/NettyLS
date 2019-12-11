package com.jet.netty.helloworld.socketdemo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Description: 引导客户端  
* @Author: Jet.Chen
 * @Date: 2019-12-11 21:53 
*/
public class EchoClient {

    private static final String HOST = "dd";
    private static final int PORT = 7001;

    public static void main(String[] args) throws InterruptedException {
        new EchoClient().start();
    }

    public void start() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    // 指定使用 NIO 的传输 Channel
                    .channel(NioSocketChannel.class)
                    // 设置端口
                    .remoteAddress(new InetSocketAddress(HOST, PORT))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 添加 EchoClientHandler 到 Channel 的 ChannelPipeline
                            pipeline.addLast("EchoClientHandler", new EchoClientHandler());
                        }
                    });
            // 绑定的服务器;sync 等待服务器关闭
            ChannelFuture f = b.connect().sync();
            System.out.println(EchoClient.class.getName() + " connect remote addr " + f.channel().remoteAddress());
            // 关闭 channel 和 块，直到它被关闭
            f.channel().closeFuture().sync();
        } finally {
            // 释放所有资源
            group.shutdownGracefully().sync();
        }
    }

}
