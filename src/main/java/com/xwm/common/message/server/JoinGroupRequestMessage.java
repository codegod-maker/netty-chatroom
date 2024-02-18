package com.xwm.common.message.server;

import com.xwm.beans.Group;
import com.xwm.common.locks.JoinGroupLock;
import com.xwm.common.message.ChatMessage;
import com.xwm.common.message.SystemMessage;
import com.xwm.common.message.client.JoinGroupResponseMessage;
import com.xwm.managers.GroupManager;
import com.xwm.beans.User;
import com.xwm.managers.UserManager;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

import java.util.Set;

public class JoinGroupRequestMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return JOIN_GROUP_REQUEST_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {
        JoinGroupRequestMessage message= (JoinGroupRequestMessage)msg;
        String groupName = message.getGroupName();
        Set<Group> groups = GroupManager.getMe().getChatRoomGroups();
        Group group = groups.stream().filter(g -> g.getGroupName().equals(groupName)).findFirst().orElse(null);
        if(group==null){
            channel.writeAndFlush(new SystemMessage("您要已加入的【"+message.getGroupName()+"】群组不存在",message.from).messageAdapter(channel));
        }
        else {
            String ownerName = group.getOwnerName();
            User user = UserManager.getMe().findUser(ownerName);
            JoinGroupResponseMessage joinGroupResponseMessage = new JoinGroupResponseMessage("系统", ownerName, message.from + "申请加入群组：" + message.getGroupName());
            if(user != null){
                user.getChannel().writeAndFlush(joinGroupResponseMessage.messageAdapter(channel));
            }
            else {
                MessageUtil.sendOfflineMessage(joinGroupResponseMessage);
            }
        }
    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
        JoinGroupLock.countDown();
    }
    private String groupName;
    private User user;

    public JoinGroupRequestMessage() {
    }

    public JoinGroupRequestMessage(String groupName, String from) {
        super.from = from;
        super.to="系统";
        this.groupName = groupName;
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

    public JoinGroupRequestMessage(String groupName, User user, String from) {
        super.from = from;
        super.to="系统";
        this.groupName = groupName;
        this.user = user;
    }
}
