package com.xwm.common.message.server;

import com.xwm.beans.User;
import com.xwm.common.message.ChatMessage;
import com.xwm.common.message.client.CreateGroupResponseMessage;
import com.xwm.common.message.SystemMessage;
import com.xwm.managers.GroupManager;
import com.xwm.managers.UserManager;
import com.xwm.utils.MessageUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashSet;
import java.util.Set;

public class CreateGroupRequestMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return CREATE_GROUP_REQUEST_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {
        CreateGroupRequestMessage message = (CreateGroupRequestMessage) msg;
        //需要被拉入群聊中的所有用户
        Set<String> users = message.getUsers();
        //群主的好友列表
        Set<String> friends = UserManager.getMe().getFriendsByUsername(message.getOwner());
        //能被拉进群聊的用户
        Set<String> friendUsers = new HashSet<>();
        users.forEach(u->{
            if(u.equals(message.getOwner())){
                friendUsers.add(u);
            }
            else if(friends.stream().anyMatch(f->f.equals(u))){
                friendUsers.add(u);
            }
            else {
                channel.channel().writeAndFlush(new CreateGroupResponseMessage("用户"+u+"不是您的好友，无法拉入群聊",message.getOwner()).messageAdapter(channel));
            }
        });

        GroupManager.getMe().createChatGroup(message.getOwner(),message.getGroupName(), friendUsers);
        channel.channel().writeAndFlush(new SystemMessage("创建群聊成功",message.getOwner()).messageAdapter(channel));
        friendUsers.stream().forEach(item->{
            if(!item.equals(message.getOwner())){
                Channel channelByUsername = UserManager.getMe().getChannelByUsername(item);
                CreateGroupResponseMessage createGroupResponseMessage = new CreateGroupResponseMessage("用户：" + message.getOwner() + "已将您拉入群聊：" + message.groupName, item);
                if(channelByUsername!=null){
                    channelByUsername.writeAndFlush(createGroupResponseMessage.messageAdapter(channel));
                }
                else {
                    MessageUtil.sendOfflineMessage(createGroupResponseMessage);
                }
            }
        });
    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {

    }

    private String groupName;
    private String owner;
    private Set<String> users;

    public CreateGroupRequestMessage(String groupName, String owner, Set<String> users) {
        this.groupName = groupName;
        this.owner = owner;
        this.users = users;
        super.from=owner;
        super.to="系统";
    }

    public CreateGroupRequestMessage() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
