package com.jet.netty.helloworld._02_time_demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @ClassName: TimeServerHandler
 * @Description:
 * @Author: Jet.Chen
 * @Date: 2021/6/9 15:40
 * @Version: 1.0
 **/
public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf time = ctx.alloc().buffer(4); // 生成一个消息的缓冲
        time.writeInt((int)(System.currentTimeMillis() / 1000L + 2208988800L));

        ChannelFuture f = ctx.writeAndFlush(time);
        f.addListener((ChannelFutureListener) future -> {
            assert  f == future;
            ctx.close();
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
