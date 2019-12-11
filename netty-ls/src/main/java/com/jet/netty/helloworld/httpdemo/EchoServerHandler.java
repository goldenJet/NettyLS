package com.jet.netty.helloworld.httpdemo;

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
        System.out.println(msg.getClass().getName());
        if (msg instanceof HttpRequest) {
            // 处理 http 请求
            HttpRequest request  = (HttpRequest)msg;

            // 请求信息
            String methodName = request.method().name();
            String uri = request.uri();
            System.out.println(String.format("接收到请求，method：%s，uri：%s", methodName, uri));

            // 响应
            ByteBuf content = Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            ctx.writeAndFlush(response);
        }
    }

    // 异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
