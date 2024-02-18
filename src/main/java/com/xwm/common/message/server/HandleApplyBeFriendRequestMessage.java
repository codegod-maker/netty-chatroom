package com.xwm.common.message.server;

import com.xwm.beans.User;
import com.xwm.common.message.ChatMessage;
import com.xwm.common.message.client.HandleApplyBeFriendResponseMessage;
import com.xwm.managers.UserManager;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

public class HandleApplyBeFriendRequestMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return HANDLE_APPLY_BEFRIEND_REQUEST_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {
        HandleApplyBeFriendRequestMessage handleApplyBeFriendRequestMessage = (HandleApplyBeFriendRequestMessage)msg;
        Boolean agreeBeFriend = handleApplyBeFriendRequestMessage.agreeBeFriend;
        String from = handleApplyBeFriendRequestMessage.from;
        String to = handleApplyBeFriendRequestMessage.to;
        User applyUser = UserManager.getMe().findUser(to);
        User toUser = UserManager.getMe().queryUser(to);
        User fromUser = UserManager.getMe().findUser(from);
        if(applyUser == null){
            boolean contains = toUser.getFriendApplySet().contains(from);
            if(!contains){
                HandleApplyBeFriendResponseMessage applyBeFriendResponseMessage = new HandleApplyBeFriendResponseMessage("系统", to, to + "没有对你发送好友申请，不要这么自恋哦");
                MessageUtil.sendOfflineMessage(applyBeFriendResponseMessage);
                return;
            }
        }
        else {
            boolean contains = applyUser.getFriendApplySet().contains(from);
            if(!contains){
                HandleApplyBeFriendResponseMessage applyBeFriendResponseMessage = new HandleApplyBeFriendResponseMessage("系统", to, to + "没有对你发送好友申请，不要这么自恋哦");
                applyUser.getChannel().writeAndFlush(applyBeFriendResponseMessage.messageAdapter(channel));
                return;
            }
        }
        if(agreeBeFriend){
            fromUser.getFriends().add(to);
            HandleApplyBeFriendResponseMessage applyBeFriendResponseMessage = new HandleApplyBeFriendResponseMessage("系统", to, "添加好友："+from+" 成功");
            if(applyUser == null){
                toUser.getFriendApplySet().remove(from);
                toUser.getFriends().add(from);
                UserManager.getMe().saveUser(toUser);
                MessageUtil.sendOfflineMessage(applyBeFriendResponseMessage);
            }else {
                applyUser.getFriendApplySet().remove(from);
                applyUser.getFriends().add(from);
                applyUser.getChannel().writeAndFlush(applyBeFriendResponseMessage.messageAdapter(channel));
            }
            HandleApplyBeFriendResponseMessage fromFriendResponseMessage = new HandleApplyBeFriendResponseMessage("系统", to, "添加好友："+to+" 成功");
            fromUser.getChannel().writeAndFlush(fromFriendResponseMessage.messageAdapter(channel));
        }
        else {
            HandleApplyBeFriendResponseMessage applyBeFriendResponseMessage = new HandleApplyBeFriendResponseMessage("系统", to, from + "拒绝了您的好友申请");
            if(applyUser == null){
                MessageUtil.sendOfflineMessage(applyBeFriendResponseMessage);
            }else {
                applyUser.getChannel().writeAndFlush(applyBeFriendResponseMessage.messageAdapter(channel));
            }
            HandleApplyBeFriendResponseMessage fromFriendResponseMessage = new HandleApplyBeFriendResponseMessage("系统", to, "您拒绝了："+to+"的好友申请");
            fromUser.getChannel().writeAndFlush(fromFriendResponseMessage.messageAdapter(channel));
        }

    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {

    }

    private Boolean agreeBeFriend;

    public HandleApplyBeFriendRequestMessage(String from,String to,Boolean agreeBeFriend) {
        super.from = from;
        super.to = to;
        this.agreeBeFriend = agreeBeFriend;
    }

    public Boolean getAgreeBeFriend() {
        return agreeBeFriend;
    }

    public void setAgreeBeFriend(Boolean agreeBeFriend) {
        this.agreeBeFriend = agreeBeFriend;
    }
}
