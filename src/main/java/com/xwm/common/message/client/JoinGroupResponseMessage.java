package com.xwm.common.message.client;

import com.xwm.common.message.ChatMessage;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

public class JoinGroupResponseMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return JOIN_GROUP_RESPONSE_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {

    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
        JoinGroupResponseMessage joinGroupResponseMessage = (JoinGroupResponseMessage)msg;
        MessageUtil.println(joinGroupResponseMessage,joinGroupResponseMessage.getContent());
    }

    private String content;

    public JoinGroupResponseMessage(String from,String to,String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
