package com.xwm.common.message.client;

import com.xwm.common.message.ChatMessage;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

import java.util.Set;

public class OnlineMemberListResponseMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return LIST_SYSTEM_RESPONSE_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {

    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
        OnlineMemberListResponseMessage onlineMemberListResponseMessage = (OnlineMemberListResponseMessage)msg;
        Set<String> users = onlineMemberListResponseMessage.getUsers();
        if(users.isEmpty()){
            MessageUtil.println(onlineMemberListResponseMessage,"在线人员列表为空");
            return;
        }
        StringBuilder content = new StringBuilder();
        content.append("在线人员列表：\n");
        users.forEach(u->{
            content.append("- ");
            content.append(u);
            content.append("\n");
        });
        MessageUtil.println(onlineMemberListResponseMessage,content.toString());
    }

    private Set<String> users;

    public OnlineMemberListResponseMessage(String from , String to, Set<String> users) {
        this.users = users;
        super.from = from;
        super.to = to;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
