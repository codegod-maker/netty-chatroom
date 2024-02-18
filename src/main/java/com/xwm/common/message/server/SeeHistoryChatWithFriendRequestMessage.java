package com.xwm.common.message.server;

import com.xwm.beans.User;
import com.xwm.common.message.ChatMessage;
import com.xwm.common.message.client.SeeHistoryChatWithFriendResponseMessage;
import com.xwm.common.message.client.SingleChatResponseMessage;
import com.xwm.managers.UserManager;
import io.netty.channel.ChannelHandlerContext;

import java.util.LinkedList;

public class SeeHistoryChatWithFriendRequestMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return SEE_HISTORY_CHAT_WITH_FRIEND_REQUEST_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {
        SeeHistoryChatWithFriendRequestMessage seeHistoryChatWithFriendRequestMessage = (SeeHistoryChatWithFriendRequestMessage)msg;
        String from = seeHistoryChatWithFriendRequestMessage.from;
        Integer limit = seeHistoryChatWithFriendRequestMessage.limit;
        User user = UserManager.getMe().findUser(from);
        byte chatType = user.getChatType();
        String content = null;
        if(chatType != ChatMessage.SINGLE_CHAT_REQUEST_MESSAGE_TYPE){
            content = "你选择的对象为群，请选择单人后再查看单聊记录";
            SeeHistoryChatWithFriendResponseMessage responseMessage = new SeeHistoryChatWithFriendResponseMessage("系统", from, content);
            channel.channel().writeAndFlush(responseMessage.messageAdapter(channel));
            return;
        }
        String to = user.getCurrentSelect();
        LinkedList<ChatMessage> chatMessages = user.getOfflineMessages().get(ChatMessage.SINGLE_CHAT_RESPONSE_MESSAGE_TYPE);
        LinkedList<SingleChatResponseMessage> singleChatResponseMessages = new LinkedList<>();
        if(chatMessages.isEmpty()){
            SeeHistoryChatWithFriendResponseMessage responseMessage = new SeeHistoryChatWithFriendResponseMessage("系统", from, "该用户未对你发送离线消息");
            channel.channel().writeAndFlush(responseMessage.messageAdapter(channel));
            return;
        }
        for (ChatMessage chatMessage : chatMessages) {
            if(to.equals(chatMessage.from)){
                if(singleChatResponseMessages.size() < limit){
                    singleChatResponseMessages.offer((SingleChatResponseMessage) chatMessage);
                }
                else {
                    break;
                }
            }
        }
        SeeHistoryChatWithFriendResponseMessage responseMessage = new SeeHistoryChatWithFriendResponseMessage("系统", from, content);
        responseMessage.setMessages(singleChatResponseMessages);
        channel.channel().writeAndFlush(responseMessage.messageAdapter(channel));
    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {

    }

    private Integer limit;

    public SeeHistoryChatWithFriendRequestMessage(String from,Integer limit) {
        super.from = from;
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
