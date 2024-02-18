package com.xwm.common.message;

import io.netty.channel.ChannelHandlerContext;

import java.io.File;

public class SystemMessage extends ChatMessage {
    private String content;

    @Override
    public Byte messageType() {
        return SYSTEM_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {

    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
        SystemMessage sys = (SystemMessage) msg;
        System.out.println("Receive from 【"+sys.from + "】 message：【" + sys.getContent()+"】 time：【 "+msg.time+" 】");
    }

    public SystemMessage(String content, String to) {
        this.content = content;
        super.to = to;
        super.from = "系统";
    }

    public SystemMessage() {
    }

    public SystemMessage(String content) {
        this.content = content;
        super.from = "系统";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
