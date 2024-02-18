package com.xwm.common.message.server;

import com.xwm.beans.User;
import com.xwm.common.message.ChatMessage;
import com.xwm.common.message.client.SelectFriendChatResponseMessage;
import com.xwm.managers.UserManager;
import io.netty.channel.ChannelHandlerContext;

public class SelectFriendChatRequestMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return SELECT_FRIEND_CHAT_REQUEST_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {
        SelectFriendChatRequestMessage selectFriendChatRequestMessage = (SelectFriendChatRequestMessage)msg;
        String to = selectFriendChatRequestMessage.to;
        String from = selectFriendChatRequestMessage.from;
        User fromUser = UserManager.getMe().findUser(from);
        User toUser = UserManager.getMe().findUser(to);
        String res = "";
        if(toUser != null){
            if(fromUser.getFriends().stream().anyMatch(u->u.equals(toUser.getName()))){
                fromUser.setCurrentSelect(toUser.getName());
                fromUser.setChatType(ChatMessage.SINGLE_CHAT_REQUEST_MESSAGE_TYPE);
                res = "选择成功";
            }
            else {
                res = "选择失败，请先加对方为好友";
            }

        }else {
            User user = UserManager.getMe().queryUser(to);
            if(user == null){
                res="选择失败，该用户不存在";
            }
            else {
                if(!fromUser.getFriends().stream().anyMatch(u->u.equals(user.getName()))){
                    res = "选择失败，请先加对方为好友";
                }
                else {
                    //用户离线，也可以设置当前聊天人，只不过是发送离线消息
                    fromUser.setCurrentSelect(user.getName());
                    fromUser.setChatType(ChatMessage.SINGLE_CHAT_REQUEST_MESSAGE_TYPE);
                    res = "选择成功，对方处于离线";
                }
            }
        }

        SelectFriendChatResponseMessage selectFriendChatResponseMessage = new SelectFriendChatResponseMessage(from,to,res);
        channel.channel().writeAndFlush(selectFriendChatResponseMessage.messageAdapter(channel));
    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {

    }

    public SelectFriendChatRequestMessage(String from,String to){
        super.from = from;
        super.to = to;
    }
}
