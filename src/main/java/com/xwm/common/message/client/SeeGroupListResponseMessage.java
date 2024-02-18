package com.xwm.common.message.client;

import com.xwm.common.message.ChatMessage;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

import java.util.Set;

public class SeeGroupListResponseMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return LIST_GROUPS_RESPONSE_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {

    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
        SeeGroupListResponseMessage seeGroupListResponseMessage = (SeeGroupListResponseMessage)msg;
        Set<String> groupNames = seeGroupListResponseMessage.getGroupNames();
        if(groupNames.isEmpty()){
            MessageUtil.println(seeGroupListResponseMessage,"群组列表为空");
            return;
        }
        StringBuilder content = new StringBuilder();
        content.append("群组列表：\n");
        groupNames.forEach(u->{
            content.append("- ");
            content.append(u);
            content.append("\n");
        });
        MessageUtil.println(seeGroupListResponseMessage,content.toString());
    }

    private Set<String> groupNames;

    public Set<String> getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(Set<String> groupNames) {
        this.groupNames = groupNames;
    }

    public SeeGroupListResponseMessage(String from, String to, Set<String> groupNames){
        super.from = from;
        super.to = to;
        this.groupNames = groupNames;
    }
}
