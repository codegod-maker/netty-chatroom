package com.xwm.common.message.client;

import com.xwm.common.message.ChatMessage;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

public class SingleChatResponseMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return SINGLE_CHAT_RESPONSE_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {

    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
        SingleChatResponseMessage singleChatResponseMessage = (SingleChatResponseMessage)msg;
        MessageUtil.println(singleChatResponseMessage,singleChatResponseMessage.getContent());
    }

    private String content;

    public SingleChatResponseMessage() {
    }
    public SingleChatResponseMessage(String from,String to,String content) {
        super.from = from;
        super.to = to;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
