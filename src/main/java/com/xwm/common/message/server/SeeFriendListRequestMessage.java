package com.xwm.common.message.server;

import com.xwm.beans.User;
import com.xwm.common.message.ChatMessage;
import com.xwm.common.message.client.SeeFriendListResponseMessage;
import com.xwm.managers.UserManager;
import io.netty.channel.ChannelHandlerContext;

import java.util.Set;

public class SeeFriendListRequestMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return SEE_FRIEND_LIST_REQUEST_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {
        SeeFriendListRequestMessage seeFriendListRequestMessage = (SeeFriendListRequestMessage)msg;
        Set<String> friends = UserManager.getMe().findUser(seeFriendListRequestMessage.from).getFriends();
        SeeFriendListResponseMessage seeFriendListResponseMessage = new SeeFriendListResponseMessage("系统", seeFriendListRequestMessage.from,friends);
        channel.channel().writeAndFlush(seeFriendListResponseMessage.messageAdapter(channel));
    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {

    }

    public SeeFriendListRequestMessage(String from,String to){
        super.from=from;
        super.to=to;
    }
}
