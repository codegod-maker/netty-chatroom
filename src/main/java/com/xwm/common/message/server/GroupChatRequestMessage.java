package com.xwm.common.message.server;

import com.xwm.beans.User;
import com.xwm.common.message.ChatMessage;
import com.xwm.common.message.client.GroupChatResponseMessage;
import com.xwm.managers.GroupManager;
import com.xwm.managers.UserManager;
import io.netty.channel.ChannelHandlerContext;

public class GroupChatRequestMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return GROUP_CHAT_REQUEST_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {
        GroupChatRequestMessage message = (GroupChatRequestMessage) msg;
        String content = message.getContent();
        String from = message.from;
        User user = UserManager.getMe().findUser(from);
        byte chatType = user.getChatType();
        String res = "";
        if(chatType == ChatMessage.GROUP_CHAT_REQUEST_MESSAGE_TYPE){
            GroupChatResponseMessage groupChatResponseMessage = new GroupChatResponseMessage(from, user.getCurrentSelect(), content);
            GroupManager.getMe().sendMsgChatGroup(user.getCurrentSelect(),from,groupChatResponseMessage,channel);
            res = "群聊消息发送成功";
        }
        else if(chatType == ChatMessage.SINGLE_CHAT_REQUEST_MESSAGE_TYPE){
            res = "当前聊天方式为单聊，请先选择群聊，才能发送群聊消息";
        }
        else {
            res = "请先选择群聊对象，才能发送群聊消息";
        }
        GroupChatResponseMessage groupChatResponseMessage = new GroupChatResponseMessage(from, user.getCurrentSelect(), res);
        channel.channel().writeAndFlush(groupChatResponseMessage.messageAdapter(channel));
    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {

    }

    private String content;

    public GroupChatRequestMessage() {
    }

    public GroupChatRequestMessage(String from, String content) {
        super.from = from;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
