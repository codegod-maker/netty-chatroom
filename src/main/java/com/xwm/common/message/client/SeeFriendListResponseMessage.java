package com.xwm.common.message.client;

import com.xwm.beans.User;
import com.xwm.common.message.ChatMessage;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

import java.util.Set;

public class SeeFriendListResponseMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return SEE_FRIEND_LIST_RESPONSE_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {

    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
        SeeFriendListResponseMessage seeFriendListResponseMessage = (SeeFriendListResponseMessage)msg;
        Set<String> friends = seeFriendListResponseMessage.getFriends();
        if(friends.isEmpty()){
            MessageUtil.println(seeFriendListResponseMessage,"好友列表为空");
            return;
        }
        StringBuilder content = new StringBuilder();
        content.append("好友列表：\n");
        friends.forEach(u->{
            content.append("- ");
            content.append(u);
            content.append("\n");
        });
        MessageUtil.println(seeFriendListResponseMessage,content.toString());
    }

    public SeeFriendListResponseMessage(String from,String to,Set<String> friends){
        super.from=from;
        super.to=to;
        this.friends = friends;
    }

    private Set<String> friends;

    public Set<String> getFriends() {
        return friends;
    }

    public void setFriends(Set<String> friends) {
        this.friends = friends;
    }
}
