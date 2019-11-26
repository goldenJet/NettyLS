package com.jet.netty.helloworld;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @ClassName: EchoServerHandler
 * @Description: 服务端处理逻辑
 * @Author: Jet.Chen
 * @Date: 2019/11/26 15:26
 * @Version: 1.0
 **/
public class EchoServerHandler extends SimpleChannelInboundHandler<HttpObject> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));

        ByteBuf content = Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8);
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

        ctx.writeAndFlush(response);
    }

    // 异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
