package com.xwm.managers;

import com.xwm.beans.Group;
import com.xwm.common.message.client.GroupChatResponseMessage;
import com.xwm.beans.User;
import com.xwm.utils.MessageUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupManager {
    private volatile static GroupManager groupManager;
    private static final Object LOCK=new Object();

    private GroupManager(){}

    public static GroupManager getMe(){
        if(groupManager !=null){
            return groupManager;
        }
        synchronized (LOCK){
            if(groupManager!=null){
                return groupManager;
            }
            groupManager=new GroupManager();
        }
        return groupManager;
    }

    public void createChatGroup(String owner, String groupName, Set<String> users){
        Group group = new Group(groupName, owner, users);
        users.forEach(u->{
            User user = UserManager.getMe().findUser(u);
            if(user!=null){
                user.getGroups().add(group);
            }
            else {
                User queryUser = UserManager.getMe().queryUser(user.getName());
                queryUser.getGroups().add(group);
                UserManager.getMe().saveUser(queryUser);
            }
        });
        users.forEach(u->UserManager.getMe().findUser(u).getGroups().add(group));
    }

    public void sendMsgChatGroup(String groupName, String from, GroupChatResponseMessage message, ChannelHandlerContext channel){
        Group group = UserManager.getMe().getGroupByUsernameAndGroupName(from,groupName);
        group.getUsernames().forEach(user->{
            if(!user.equals(from)){
                GroupChatResponseMessage groupChatResponseMessage = new GroupChatResponseMessage(message.from, user, message.getContent());
                User inner = UserManager.getMe().findUser(user);
                if(user!=null){
                    Channel ch = inner.getChannel();
                    ch.writeAndFlush(message.messageAdapter(channel));
                }
                else {
                    MessageUtil.sendOfflineMessage(groupChatResponseMessage);
                }
            }
        });
    }

    public void joinChatGroup(String groupName,User user){
        Set<Group> groups = user.getGroups();
        Group group = groups.stream().filter(g -> g.getGroupName().equals(groupName)).findFirst().orElse(null);
        if(group!=null){
            group.getUsernames().add(user.getName());
        }
    }

    public Set<String> membersChatGroup(String from,String groupName){
        Set<Group> groupsByUsername = UserManager.getMe().getGroupsByUsername(from);
        Group group = groupsByUsername.stream().filter(g -> g.getGroupName().equals(groupName)).findFirst().orElse(null);
        if(group == null){
            return new HashSet<>();
        }
        return group.getUsernames();
    }

    public void quitChatGroup(String groupName,User user){
        Set<Group> groups = user.getGroups();
        Group group = groups.stream().filter(g -> g.getGroupName().equals(groupName)).findFirst().orElse(null);
        if(group!=null){
            group.getUsernames().remove(user.getName());
            groups.remove(group);
        }
    }

    public Set<String> listGroups(String from){
        User user = UserManager.getMe().findUser(from);
        return user.getGroups().stream().map(i->i.getGroupName()).collect(Collectors.toSet());
    }

    public Set<Group> getChatRoomGroups(){
        HashSet<String> names = new HashSet<>();
        HashSet<Group> groups = new HashSet<>();
        String parent = UserManager.PARENT;
        File file = new File(parent);
        File[] files = file.listFiles();
        for (File file1 : files) {
            User user = UserManager.getMe().queryUserByFileName(file1.getName());
            for (Group group : user.getGroups()) {
                if(!names.contains(group.getGroupName())){
                    names.add(group.getGroupName());
                    groups.add(group);
                }
            }
        }
        return groups;
    }
}
