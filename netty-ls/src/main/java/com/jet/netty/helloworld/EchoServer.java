package com.jet.netty.helloworld;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

import java.net.InetSocketAddress;

/**
 * @ClassName: EchoServer
 * @Description: 引导服务器
 * @Author: Jet.Chen
 * @Date: 2019/11/26 15:07
 * @Version: 1.0
 **/
public class EchoServer {

    private static final int PORT = 7001;

    public static void main(String[] args) throws InterruptedException {
        new EchoServer().start();
    }

    public void start() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    // 指定使用 NIO 的传输 Channel
                    .channel(NioServerSocketChannel.class)
                    // 设置端口
                    .localAddress(new InetSocketAddress(PORT))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast("HttpServerCodec", new HttpServerCodec());
                            // 添加 EchoServerHandler 到 Channel 的 ChannelPipeline
                            pipeline.addLast("EchoServerHandler", new EchoServerHandler());
                        }
                    });
            // 绑定的服务器;sync 等待服务器关闭
            ChannelFuture f = b.bind().sync();
            System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
            // 关闭 channel 和 块，直到它被关闭
            f.channel().closeFuture().sync();
        } finally {
            // 释放所有资源
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        }
    }

}
