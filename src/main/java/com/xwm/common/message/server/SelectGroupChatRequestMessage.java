package com.xwm.common.message.server;

import com.xwm.beans.Group;
import com.xwm.beans.User;
import com.xwm.common.message.ChatMessage;
import com.xwm.common.message.client.SelectGroupChatResponseMessage;
import com.xwm.managers.UserManager;
import io.netty.channel.ChannelHandlerContext;

import java.util.Set;

public class SelectGroupChatRequestMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return SELECT_GROUP_CHAT_REQUEST_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {
        SelectGroupChatRequestMessage selectGroupChatRequestMessage = (SelectGroupChatRequestMessage)msg;
        String to = selectGroupChatRequestMessage.to;
        String from = selectGroupChatRequestMessage.from;
        User user = UserManager.getMe().findUser(from);
        Set<Group> groups = user.getGroups();
        String content = "";
        if(groups.stream().anyMatch(g->g.getGroupName().equals(to))){
            user.setChatType(ChatMessage.GROUP_CHAT_REQUEST_MESSAGE_TYPE);
            user.setCurrentSelect(to);
            content = "选择成功";
        }
        else {
            content = "您未加入该群组，不能选择";
        }
        SelectGroupChatResponseMessage selectGroupChatResponseMessage = new SelectGroupChatResponseMessage(from, to, content);
        channel.channel().writeAndFlush(selectGroupChatResponseMessage.messageAdapter(channel));
    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {

    }

    public SelectGroupChatRequestMessage(String from,String to) {
        super.from = from;
        super.to = to;
    }
}
