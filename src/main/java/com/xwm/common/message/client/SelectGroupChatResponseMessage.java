package com.xwm.common.message.client;

import com.xwm.common.message.ChatMessage;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

public class SelectGroupChatResponseMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return SELECT_GROUP_CHAT_RESPONSE_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {

    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
        SelectGroupChatResponseMessage selectGroupChatResponseMessage = (SelectGroupChatResponseMessage)msg;
        String content = selectGroupChatResponseMessage.getContent();
        MessageUtil.println(selectGroupChatResponseMessage,content);
    }

    private String content;

    public SelectGroupChatResponseMessage(String from,String to,String content) {
        this.content = content;
        super.from =from;
        super.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
