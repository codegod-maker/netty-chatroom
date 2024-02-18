package com.xwm.common.message.client;

import com.xwm.common.message.ChatMessage;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

import java.util.Set;

public class SeeGroupMembersResponseMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return SEE_GROUP_RESPONSE_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {

    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
        SeeGroupMembersResponseMessage seeGroupMembersResponseMessage = (SeeGroupMembersResponseMessage)msg;
        Set<String> users = seeGroupMembersResponseMessage.getUsers();
        StringBuilder sb = new StringBuilder();
        sb.append("群组人员列表：\n");
        users.forEach(u->{
            sb.append("- ");
            sb.append(u);
            sb.append("\n");
        });
        MessageUtil.println(seeGroupMembersResponseMessage,sb.toString());
    }

    private Set<String> users;

    public SeeGroupMembersResponseMessage(String from, String to, Set<String> users) {
        this.users = users;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
