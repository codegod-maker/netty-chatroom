package com.xwm.common.message.client;


import com.xwm.common.locks.LoginLock;
import com.xwm.common.message.ChatMessage;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class UserLoginResponseMessage extends ChatMessage {
    private Boolean isSuccess;
    private Integer status;

    public UserLoginResponseMessage(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public UserLoginResponseMessage() {
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public Byte messageType() {
        return LOGIN_RESPONSE_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {

    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
        UserLoginResponseMessage message = (UserLoginResponseMessage) msg;
        if (message.getSuccess()) {
            LoginLock.compareAndSet(1, message.status);
        } else {
            LoginLock.compareAndSet(1, message.status);
        }
        try {
            LoginLock.await(60L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
