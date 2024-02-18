package com.xwm.common.message.server;

import com.xwm.common.message.ChatMessage;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingRequestMessage extends ChatMessage {
    private static final Logger log = LoggerFactory.getLogger(PingRequestMessage.class);

    @Override
    public Byte messageType() {
        return PING_REQUEST_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {
        log.info("心跳检测来自客户端：{}",channel.channel().remoteAddress().toString());
    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {

    }
}
