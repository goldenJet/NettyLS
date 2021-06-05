package com.jet.netty.helloworld._01_discard_demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @ClassName: Test
 * @Description: 抛弃服务 server 端 channel
 * @Author: Jet.Chen
 * @Date: 2021/6/4 17:00
 * @Version: 1.0
 **/
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 数据丢弃
//        ((ByteBuf)msg).release();

        // 数据打印
        ByteBuf in = (ByteBuf) msg;

        try {
            System.out.println(in.toString(CharsetUtil.US_ASCII));
        } finally {
            in.release();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 异常打印
        cause.printStackTrace();
        // 关闭连接
        ctx.close();
    }
}
