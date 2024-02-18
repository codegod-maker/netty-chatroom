package com.xwm.common.message.server;

import com.xwm.common.message.ChatMessage;
import com.xwm.common.message.client.OnlineMemberListResponseMessage;
import com.xwm.managers.UserManager;
import io.netty.channel.ChannelHandlerContext;

import java.util.Set;

public class OnlineMemberListRequestMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return LIST_SYSTEM_REQUEST_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {
        OnlineMemberListRequestMessage message = (OnlineMemberListRequestMessage)msg;
        Set<String> strings = UserManager.getMe().getUSERS().keySet();
        OnlineMemberListResponseMessage onlineMemberListResponseMessage = new OnlineMemberListResponseMessage("系统", message.from, strings);
        channel.writeAndFlush(onlineMemberListResponseMessage.messageAdapter(channel));
    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {

    }

    public OnlineMemberListRequestMessage() {

    }

    public OnlineMemberListRequestMessage(String from) {
        super.from = from;
        super.to = "系统";
    }
}
