package com.xwm.beans;

import com.xwm.common.message.ChatMessage;
import io.netty.channel.Channel;

import java.io.Serializable;
import java.util.*;

public class User implements Serializable {
    private String userId;
    private String name;
    private String pwd;
    //当前聊天框名字，取决于聊天类型（如果是单聊就是用户名，群聊就是聊天组组名）
    private String currentSelect="";
    //当前聊天类型
    private byte chatType;

    private boolean forbid = false;

    private Set<Group> groups = new HashSet<>();

    private Set<String> friends = new HashSet<>();

    private transient Channel channel;

    //离线消息列表，用户登陆时加载
    private transient Map<Byte, LinkedList<ChatMessage>> offlineMessages = new HashMap<>();

    public Map<Byte, LinkedList<ChatMessage>> getOfflineMessages() {
        return offlineMessages;
    }

    public void setOfflineMessages(Map<Byte, LinkedList<ChatMessage>> offlineMessages) {
        this.offlineMessages = offlineMessages;
    }

    //保存好友申请列表
    private Set<String> friendApplySet = new HashSet<>();

    //保存群组申请列表
    private Set<String> groupApplySet = new HashSet<>();

    public Set<String> getFriendApplySet() {
        return friendApplySet;
    }

    public void setFriendApplySet(Set<String> friendApplySet) {
        this.friendApplySet = friendApplySet;
    }

    public Set<String> getGroupApplySet() {
        return groupApplySet;
    }

    public void setGroupApplySet(Set<String> groupApplySet) {
        this.groupApplySet = groupApplySet;
    }

    public User() {
    }

    public User(String userId,String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
    }

    public User(String name, String pwd, Channel channel) {
        this.name = name;
        this.pwd = pwd;
        this.channel = channel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String id) {
        this.userId = id;
    }

    public User(String id, String name, String pwd, boolean isOnline, String currentSelect, byte chatType, Channel channel) {
        this.userId = id;
        this.name = name;
        this.pwd = pwd;
        this.currentSelect = currentSelect;
        this.chatType = chatType;
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getCurrentSelect() {
        return currentSelect;
    }

    public void setCurrentSelect(String currentSelect) {
        this.currentSelect = currentSelect;
    }

    public byte getChatType() {
        return chatType;
    }

    public void setChatType(byte chatType) {
        this.chatType = chatType;
    }

    public User(String name, String pwd, boolean isOnline, String currentSelect, byte chatType) {
        this.name = name;
        this.pwd = pwd;
        this.currentSelect = currentSelect;
        this.chatType = chatType;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public User(String name, String pwd, boolean isOnline, String currentSelect, byte chatType, Set<Group> groups, Set<String> friends, Channel channel) {
        this.name = name;
        this.pwd = pwd;
        this.currentSelect = currentSelect;
        this.chatType = chatType;
        this.groups = groups;
        this.friends = friends;
        this.channel = channel;
    }

    public Set<String> getFriends() {
        return friends;
    }

    public void setFriends(Set<String> friends) {
        this.friends = friends;
    }

    public Channel getChannel() {
        return this.channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public boolean isForbid() {
        return forbid;
    }

    public void setForbid(boolean forbid) {
        this.forbid = forbid;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", currentSelect='" + currentSelect + '\'' +
                ", chatType=" + chatType +
                ", forbid=" + forbid +
                ", groups=" + groups +
                ", friends=" + friends +
                ", channel=" + channel +
                ", offlineMessages=" + offlineMessages +
                ", friendApplySet=" + friendApplySet +
                ", groupApplySet=" + groupApplySet +
                '}';
    }
}
