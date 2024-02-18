package com.xwm.common.message.server;

import com.xwm.common.message.ChatMessage;
import com.xwm.common.message.client.SeeGroupListResponseMessage;
import com.xwm.managers.GroupManager;
import io.netty.channel.ChannelHandlerContext;

import java.util.Set;

public class SeeGroupListRequestMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return LIST_GROUPS_REQUEST_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {
        SeeGroupListRequestMessage message = (SeeGroupListRequestMessage) msg;
        Set<String> stringSet = GroupManager.getMe().listGroups(message.from);
        SeeGroupListResponseMessage seeGroupListResponseMessage = new SeeGroupListResponseMessage("系统", message.from, stringSet);
        channel.channel().writeAndFlush(seeGroupListResponseMessage.messageAdapter(channel));
    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {

    }

    public SeeGroupListRequestMessage() {
    }

    public SeeGroupListRequestMessage(String from) {
        super.from = from;
        super.to = "系统";
    }
}
