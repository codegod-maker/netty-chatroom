package com.xwm.common.message.client;

import com.xwm.common.message.ChatMessage;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

import java.util.LinkedList;

public class SeeHistoryChatWithFriendResponseMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return SEE_HISTORY_CHAT_WITH_FRIEND_RESPONSE_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {

    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
        SeeHistoryChatWithFriendResponseMessage seeHistoryChatWithFriendResponseMessage = (SeeHistoryChatWithFriendResponseMessage)msg;
        LinkedList<SingleChatResponseMessage> messages = seeHistoryChatWithFriendResponseMessage.getMessages();
        String content = seeHistoryChatWithFriendResponseMessage.getContent();
        if(content != null){
            MessageUtil.println(seeHistoryChatWithFriendResponseMessage,content);
            return;
        }
        messages.forEach(i->{
            String messageContent = i.getContent();
            MessageUtil.println(i,messageContent);
        });
    }

    private String content;
    private LinkedList<SingleChatResponseMessage> messages;

    public SeeHistoryChatWithFriendResponseMessage(String from,String to,String content) {
        super.from = from;
        super.to = to;
        this.content = content;
    }

    public LinkedList<SingleChatResponseMessage> getMessages() {
        return messages;
    }

    public void setMessages(LinkedList<SingleChatResponseMessage> messages) {
        this.messages = messages;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
