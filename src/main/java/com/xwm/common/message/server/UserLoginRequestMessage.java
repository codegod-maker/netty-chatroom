package com.xwm.common.message.server;

import com.xwm.common.message.ChatMessage;
import com.xwm.common.message.SystemMessage;
import com.xwm.common.message.client.UserLoginResponseMessage;
import com.xwm.managers.UserManager;
import com.xwm.beans.User;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

public class UserLoginRequestMessage extends ChatMessage {
    private String username;
    private String pwd;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public UserLoginRequestMessage(String username, String pwd) {
        this.username = username;
        this.pwd = pwd;
    }

    public UserLoginRequestMessage() {
    }

    @Override
    public Byte messageType() {
        return LOGIN_REQUEST_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {
        UserLoginRequestMessage message = (UserLoginRequestMessage) msg;
        UserLoginResponseMessage re = new UserLoginResponseMessage();
        re.id=message.id;
        User queryUser = UserManager.getMe().queryUser(message.getUsername());
        if (queryUser != null && queryUser.getPwd().equals(message.getPwd())) {
            if(UserManager.getMe().getUSERS().containsKey(queryUser.getName())){
                re.setSuccess(false);
                re.setStatus(3);
                channel.writeAndFlush(new SystemMessage("该账号在另一个终端已在线", queryUser.getName()).messageAdapter(channel));
            }
            else {
                re.setSuccess(true);
                re.setStatus(2);
                queryUser.setChannel(channel.channel());
                UserManager.getMe().addUserAndChannel(queryUser);
                MessageUtil.loadOfflineMessageByFromUser(queryUser);
                channel.writeAndFlush(new SystemMessage("欢迎您进入在线聊天系统", queryUser.getName()).messageAdapter(channel));
            }
        } else {
            re.setSuccess(false);
            re.setStatus(0);
            channel.writeAndFlush(new SystemMessage("用户不存在，或密码错误","cmd").messageAdapter(channel));
        }
        channel.writeAndFlush(re);
    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {

    }

}
