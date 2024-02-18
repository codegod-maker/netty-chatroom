package com.xwm.common.message.server;

import com.xwm.common.message.ChatMessage;
import com.xwm.common.message.client.SeeGroupMembersResponseMessage;
import com.xwm.managers.GroupManager;
import io.netty.channel.ChannelHandlerContext;

import java.util.Set;

public class SeeGroupMembersRequestMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return SEE_GROUP_REQUEST_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {
        SeeGroupMembersRequestMessage message=(SeeGroupMembersRequestMessage) msg;
        Set<String> membersChatGroup = GroupManager.getMe().membersChatGroup(message.from,message.getGroupName());
        SeeGroupMembersResponseMessage seeGroupMembersResponseMessage = new SeeGroupMembersResponseMessage("系统", message.from, membersChatGroup);
        channel.channel().writeAndFlush(seeGroupMembersResponseMessage.messageAdapter(channel));
    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
    }

    private String groupName;

    public SeeGroupMembersRequestMessage() {
    }

    public SeeGroupMembersRequestMessage(String groupName, String from) {
        this.groupName = groupName;
        super.from = from;
        super.to="系统";
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
