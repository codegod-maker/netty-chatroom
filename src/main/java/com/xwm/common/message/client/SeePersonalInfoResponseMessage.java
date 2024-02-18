package com.xwm.common.message.client;

import com.xwm.beans.Group;
import com.xwm.beans.User;
import com.xwm.common.message.ChatMessage;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

import java.util.*;

public class SeePersonalInfoResponseMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return SEE_PERSONAL_INFO_RESPONSE_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {

    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
        SeePersonalInfoResponseMessage seePersonalInfoResponseMessage = (SeePersonalInfoResponseMessage)msg;
        Map<String, Integer> lists = seePersonalInfoResponseMessage.getMessages();
        User user = seePersonalInfoResponseMessage.getUser();
        String name = user.getName();
        String pwd = user.getPwd();
        byte chatType = user.getChatType();
        String chatTypeDesc="";
        if(chatType == ChatMessage.SINGLE_CHAT_REQUEST_MESSAGE_TYPE){
            chatTypeDesc="单聊";
        }
        else if(chatType == ChatMessage.GROUP_CHAT_REQUEST_MESSAGE_TYPE){
            chatTypeDesc="群聊";
        }
        else {
            chatTypeDesc="未知类型";
        }
        String currentSelect = user.getCurrentSelect();
        Set<String> friends = user.getFriends();
        Set<Group> groups = user.getGroups();
        Set<String> friendApplySet = user.getFriendApplySet();
        Set<String> groupApplySet = user.getGroupApplySet();
        StringBuilder sb = new StringBuilder();
        sb.append("您的个人信息如下：");
        sb.append("\n");
        sb.append("用户名："+name);
        sb.append("\n");
        sb.append("密码："+pwd);
        sb.append("\n");
        sb.append("当前聊天类型："+chatTypeDesc);
        sb.append("\n");
        sb.append("当前聊天对象："+currentSelect);
        sb.append("\n");
        sb.append("好友列表：【");
        sb.append("\n");
        friends.forEach(f->{
            sb.append("- "+f);
            sb.append("\n");
        });
        sb.append("】");
        sb.append("\n");
        sb.append("群组列表：【");
        sb.append("\n");
        groups.forEach(f->{
            sb.append("群组名："+f.getGroupName()+"。  群主："+f.getOwnerName());
            sb.append("\n");
        });
        sb.append("】");
        sb.append("\n");
        sb.append("已发出的好友申请列表：【");
        sb.append("\n");
        friendApplySet.forEach(f->{
            sb.append("- "+f);
            sb.append("\n");
        });
        sb.append("】");
        sb.append("\n");
        sb.append("已发出的群组申请列表：【");
        sb.append("\n");
        groupApplySet.forEach(f->{
            sb.append("- "+f);
            sb.append("\n");
        });
        sb.append("】");
        sb.append("\n");
        sb.append("离线消息列表：【");
        sb.append("\n");
        lists.entrySet().stream().forEach(e->{
            sb.append(e.getKey()+" 对你发送的离线消息总数："+e.getValue()+"条");
            sb.append("\n");
        });
        sb.append("】");
        sb.append("\n");
        MessageUtil.println(seePersonalInfoResponseMessage,sb.toString());
    }
    private User user;
    
    private Map<String,Integer> messages;

    public Map<String, Integer> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, Integer> messages) {
        this.messages = messages;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SeePersonalInfoResponseMessage(String from,String to,User user) {
        super.from = from;
        super.to = to;
        this.user = user;
    }
}
