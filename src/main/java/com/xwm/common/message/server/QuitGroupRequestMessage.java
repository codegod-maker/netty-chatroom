package com.xwm.common.message.server;

import com.xwm.common.message.ChatMessage;
import com.xwm.common.message.SystemMessage;
import com.xwm.common.message.client.QuitGroupResponseMessage;
import com.xwm.managers.GroupManager;
import com.xwm.beans.User;
import com.xwm.managers.UserManager;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

import java.util.Set;

public class QuitGroupRequestMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return QUIT_GROUP_REQUEST_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {
        QuitGroupRequestMessage message = (QuitGroupRequestMessage)msg;
        String groupName = message.getGroupName();
        String from = message.from;
        Set<String> strings = GroupManager.getMe().membersChatGroup(message.from,groupName);
        GroupManager.getMe().quitChatGroup(groupName,message.getUser());
        QuitGroupResponseMessage quitGroupResponseMessage = new QuitGroupResponseMessage("系统",from,"退出群组："+groupName +"成功");
        channel.channel().writeAndFlush(quitGroupResponseMessage.messageAdapter(channel));
        strings.stream().forEach(s->{
            if(!s.equals(from)){
                QuitGroupResponseMessage quitGroupResponseMessage1 = new QuitGroupResponseMessage("系统",s,from+"已退出群组："+groupName);
                User user = UserManager.getMe().findUser(s);
                if(user!=null){
                    user.getChannel().writeAndFlush(quitGroupResponseMessage1.messageAdapter(channel));
                }
                else {
                    MessageUtil.sendOfflineMessage(quitGroupResponseMessage1);
                }
            }
        });
    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {

    }
    private String groupName;
    private User user;

    public QuitGroupRequestMessage() {
    }

    public QuitGroupRequestMessage(String groupName, String from) {
        this.groupName = groupName;
        super.from = from;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
