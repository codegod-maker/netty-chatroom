package com.xwm.common.message.client;

import com.xwm.common.message.ChatMessage;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

public class CreateGroupResponseMessage extends ChatMessage {
    private String content;

    public CreateGroupResponseMessage(String content,String to) {
        this.content = content;
        super.from="系统";
        super.to=to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public Byte messageType() {
        return CREATE_GROUP_RESPONSE_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {

    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
        CreateGroupResponseMessage sys = (CreateGroupResponseMessage) msg;
        MessageUtil.println(sys,sys.getContent());
    }
}
