package com.xwm.handler;

import com.alibaba.fastjson2.JSONObject;
import com.xwm.common.message.ChatMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebsocketChatMessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(textWebSocketFrame.text());
        Byte msgType = jsonObject.getByte("msgType");
        jsonObject.remove("msgType");
        ChatMessage msg = (ChatMessage) JSONObject.parseObject(jsonObject.toJSONString(), ChatMessage.getMessageClass(msgType));

        msg.beforeDoServerLog(ctx,msg);
        msg.doServer(ctx,msg);
    }
}
