package com.xwm.common.message.client;

import com.xwm.common.locks.RegisterLock;
import com.xwm.common.message.ChatMessage;
import io.netty.channel.ChannelHandlerContext;

public class UserRegisterResponseMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return REGISTER_RESPONSE_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {

    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
        UserRegisterResponseMessage message = (UserRegisterResponseMessage)msg;
        if(message.getRegisterSuccess()){
            RegisterLock.compareAndSet(1,2);
            RegisterLock.countDown();
        }
        else {
            RegisterLock.compareAndSet(1,0);
        }
    }

    private Boolean registerSuccess;

    public UserRegisterResponseMessage() {
    }

    public UserRegisterResponseMessage(Boolean registerSuccess) {
        this.registerSuccess = registerSuccess;
    }

    public Boolean getRegisterSuccess() {
        return registerSuccess;
    }

    public void setRegisterSuccess(Boolean registerSuccess) {
        this.registerSuccess = registerSuccess;
    }
}
