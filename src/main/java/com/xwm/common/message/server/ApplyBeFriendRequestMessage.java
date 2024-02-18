package com.xwm.common.message.server;

import com.xwm.beans.User;
import com.xwm.common.message.ChatMessage;
import com.xwm.common.message.client.ApplyBeFriendResponseMessage;
import com.xwm.managers.UserManager;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

import java.util.Set;

public class ApplyBeFriendRequestMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return APPLY_BEFRIEND_REQUEST_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {
        ApplyBeFriendRequestMessage applyBeFriendRequestMessage = (ApplyBeFriendRequestMessage)msg;
        String from = applyBeFriendRequestMessage.from;
        String to = applyBeFriendRequestMessage.to;
        User user = UserManager.getMe().findUser(from);
        Set<String> friendApplySet = user.getFriendApplySet();
        Set<String> friends = user.getFriends();
        String content = "";
        if(friendApplySet.contains(to)){
            content = "您已经申请过了，请勿重复申请";
        }
        else if(friends.contains(to)){
            content = to+"已经是您好友了";
        }
        else {
            friendApplySet.add(to);
            User toUser = UserManager.getMe().findUser(to);
            String tip = from+"申请添加您为好友";
            ApplyBeFriendResponseMessage applyBeFriendResponseMessage = new ApplyBeFriendResponseMessage(from,to,tip);
            if(toUser == null){
                MessageUtil.sendOfflineMessage(applyBeFriendResponseMessage);
            }else {
                toUser.getChannel().writeAndFlush(applyBeFriendResponseMessage.messageAdapter(channel));
            }
            content = "申请已发送";
        }
        ApplyBeFriendResponseMessage applyBeFriendResponseMessage = new ApplyBeFriendResponseMessage(from,to,content);
        channel.channel().writeAndFlush(applyBeFriendResponseMessage.messageAdapter(channel));
    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {

    }

    public ApplyBeFriendRequestMessage(String from,String to) {
        super.from = from;
        super.to = to;
    }

}
