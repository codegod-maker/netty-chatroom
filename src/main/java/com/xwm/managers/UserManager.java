package com.xwm.managers;

import com.alibaba.fastjson2.JSONObject;
import com.xwm.beans.Group;
import com.xwm.beans.User;
import com.xwm.service.UserService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class UserManager implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserManager.class);
    private static final Object LOCK = new Object();
    private volatile static UserManager userManager = null;
    private UserManager(){}
    public static UserManager getMe(){
        if(userManager!=null){
            return userManager;
        }
        synchronized (LOCK){
            if(userManager!=null){
                return userManager;
            }
            userManager=new UserManager();
        }
        return userManager;
    }
    private Map<String, User> USERS = new HashMap();
    private Map<Channel, User> CHANNELS = new HashMap<>();

    public Map<String, User> getUSERS() {
        return USERS;
    }

    public Map<Channel, User> getCHANNELS() {
        return CHANNELS;
    }

    public void setUSERS(Map<String, User> USERS) {
        this.USERS = USERS;
    }

    public void setCHANNELS(Map<Channel, User> CHANNELS) {
        this.CHANNELS = CHANNELS;
    }

    public static final String PARENT="users";
    private static final String SUFFIX=".info";
    static {
        File file = new File(PARENT);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public void addUserAndChannel(User user){
        USERS.put(user.getName(),user);
        CHANNELS.put(user.getChannel(),user);
    }

    public void deleteUserAndChannel(String username){
        User user = findUser(username);
        Channel channel = user.getChannel();
        quit(user);
        CHANNELS.remove(channel);
        USERS.remove(username);
    }

    public User findUser(String name){
        return USERS.get(name);
    }

    public Channel getChannelByUsername(String name){
        User user = findUser(name);
        if(user == null){
            return null;
        }
        return user.getChannel();
    }

    public String getUsernameByChannel(Channel channel){
        return CHANNELS.get(channel).getName();
    }

    public User getUserByChannel(Channel channel){
        return CHANNELS.get(channel);
    }

    public Set<Group> getGroupsByUsername(String username){
        User user = findUser(username);
        if(user == null){
            return null;
        }
        return user.getGroups();
    }

    public Group getGroupByUsernameAndGroupName(String username, String groupName){
        User user = findUser(username);
        if(user == null){
            return null;
        }
        Set<Group> groups = user.getGroups();
        return groups.stream().filter(g-> groupName.equals(g.getGroupName())).findFirst().orElse(null);
    }

    public Set<String> getFriendsByUsername(String username){
        User user = findUser(username);
        if(user == null){
            return null;
        }
        return user.getFriends();
    }

    public String getFriendUserByUsername(String username, String friendName){
        User user = findUser(username);
        Set<String> friends = user.getFriends();
        return friends.stream().filter(f->friendName.equals(f)).findFirst().orElse(null);
    }

    @Override
    public Boolean saveUser(User user) {
        String userStr = JSONObject.toJSONString(user);
        String fileName = getFileName(user.getName());
        File file = new File(fileName);
        FileWriter appendWriter = null;
        try {
            appendWriter = new FileWriter(file);
            appendWriter.write(userStr);
            appendWriter.flush();
            appendWriter.close();
            log.error("保存用户成功,用户信息：{}",userStr);
        } catch (IOException e) {
            log.error("保存用户失败,用户信息：{}",userStr);
            return false;
        }
        return true;
    }

    private String getFileName(String username){
        return PARENT+ "/" +username+SUFFIX;
    }

    @Override
    public User queryUser(String username) {
        String fileName = getFileName(username);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            User user = JSONObject.parseObject(bufferedReader.readLine(), User.class);
            bufferedReader.close();
            return user;
        } catch (IOException e) {
            log.error("查询用户失败,用户名：{}",username);
        }
        return null;
    }

    public User queryUserByFileName(String fileName) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            User user = JSONObject.parseObject(bufferedReader.readLine(), User.class);
            bufferedReader.close();
            return user;
        } catch (IOException e) {
            log.error("查询用户失败,文件名：{}",fileName);
        }
        return null;
    }

    @Override
    public Collection<User> getUsers() {
        Set<User> users = new HashSet<>();
        File file = new File(PARENT);
        if(file.exists()){
            for (File listFile : file.listFiles()) {
                String username = listFile.getName().substring(0, listFile.getName().lastIndexOf("."));
                users.add(queryUser(username));
            }
            return users;
        }
        else {
            return null;
        }
    }

    @Override
    public void quit(User user) {
        if(saveUser(user)){
            user = null;
        }
    }

    public boolean isOnline(User user){
        if(user == null){
            return false;
        }
        return findUser(user.getName()) != null;
    }

    public boolean isOnline(String username){
        return findUser(username) != null;
    }

    public boolean isOnline(Channel channel){
        return getUserByChannel(channel) != null;
    }

    public Set<String> getAllUsers(){
        HashSet<String> hashSet = new HashSet<>();
        File file = new File(PARENT);
        File[] files = file.listFiles();
        for (File file1 : files) {
            String username = file1.getName().split(".")[0];
            hashSet.add(username);
        }
        return hashSet;
    }
}
