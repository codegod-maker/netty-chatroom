package com.xwm.common.message.server;

import com.xwm.beans.User;
import com.xwm.common.message.ChatMessage;
import com.xwm.common.message.client.SingleChatResponseMessage;
import com.xwm.managers.UserManager;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

public class SingleChatRequestMessage extends ChatMessage {
    private String content;

    @Override
    public Byte messageType() {
        return SINGLE_CHAT_REQUEST_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {
        SingleChatRequestMessage message = (SingleChatRequestMessage) msg;
        String from = message.from;
        User user = UserManager.getMe().findUser(from);
        byte chatType = user.getChatType();
        String content="";
        if(chatType == ChatMessage.GROUP_CHAT_REQUEST_MESSAGE_TYPE){
            content = "当前聊天方式为群聊，请选择单人聊天，才能发送单聊消息";
            SingleChatResponseMessage singleChatResponseMessage = new SingleChatResponseMessage(from, null, content);
            channel.channel().writeAndFlush(singleChatResponseMessage.messageAdapter(channel));
            return;
        }
        else if(chatType == ChatMessage.SINGLE_CHAT_REQUEST_MESSAGE_TYPE){
            String to = user.getCurrentSelect();
            SingleChatResponseMessage singleChatResponseMessage = new SingleChatResponseMessage(from, to, message.getContent());
            if(UserManager.getMe().findUser(to)!=null){
                UserManager.getMe().findUser(to).getChannel().writeAndFlush(singleChatResponseMessage.messageAdapter(channel));
            }
            else {
                MessageUtil.sendOfflineMessage(singleChatResponseMessage);
            }
            user.getChannel().writeAndFlush(new SingleChatResponseMessage(from, to, "发送成功"));
            return;
        }
        else {
            content = "请先选择单聊对象，才能发送聊天消息";
            SingleChatResponseMessage singleChatResponseMessage = new SingleChatResponseMessage(from, null, content);
            channel.channel().writeAndFlush(singleChatResponseMessage.messageAdapter(channel));
        }
    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
        SingleChatRequestMessage message = (SingleChatRequestMessage) msg;
        String from = message.from;
        String content = message.getContent();
        System.out.println("Receive from 【"+from + "】 message ：【" + content+"】 time：【 "+msg.time+" 】");
    }

    public SingleChatRequestMessage() {
    }

    public SingleChatRequestMessage(String from, String content) {
        super.from = from;
        this.content = content;
    }

    public SingleChatRequestMessage(String from, String content,String to) {
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
