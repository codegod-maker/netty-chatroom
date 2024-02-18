package com.xwm.common.message.server;

import com.xwm.beans.User;
import com.xwm.common.message.ChatMessage;
import com.xwm.common.message.client.UserRegisterResponseMessage;
import com.xwm.managers.UserManager;
import com.xwm.utils.IdUtil;
import io.netty.channel.ChannelHandlerContext;

public class UserRegisterRequestMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return REGISTER_REQUEST_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {
        UserRegisterRequestMessage message = (UserRegisterRequestMessage)msg;
        Boolean insertUser = UserManager.getMe().saveUser(new User(IdUtil.simpleUUID(),message.getUsername(), message.getPwd()));
        UserRegisterResponseMessage responseMessage = new UserRegisterResponseMessage(insertUser);
        channel.writeAndFlush(responseMessage.messageAdapter(channel));
    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {

    }

    private String username;
    private String pwd;

    public UserRegisterRequestMessage() {
    }

    public UserRegisterRequestMessage(String username, String pwd) {
        this.username = username;
        this.pwd = pwd;
    }

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
}
