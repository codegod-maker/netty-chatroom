package com.xwm.common.message.client;

import com.xwm.common.message.ChatMessage;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

public class GroupChatResponseMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return GROUP_CHAT_RESPONSE_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {

    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
        GroupChatResponseMessage groupChatResponseMessage = (GroupChatResponseMessage)msg;
        String content = groupChatResponseMessage.getContent();
        MessageUtil.println(groupChatResponseMessage,content);
    }

    private String content;

    public GroupChatResponseMessage(String from,String to,String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
