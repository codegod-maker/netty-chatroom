package com.xwm.common.message.client;

import com.xwm.common.message.ChatMessage;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

public class HandleApplyBeFriendResponseMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return HANDLE_APPLY_BEFRIEND_RESPONSE_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {

    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
        HandleApplyBeFriendResponseMessage handleApplyBeFriendResponseMessage = (HandleApplyBeFriendResponseMessage)msg;
        MessageUtil.println(handleApplyBeFriendResponseMessage,handleApplyBeFriendResponseMessage.getContent());
    }

    private String content;

    public HandleApplyBeFriendResponseMessage(String from,String to,String content) {
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
