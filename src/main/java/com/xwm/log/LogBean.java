package com.xwm.log;

import com.xwm.common.message.ChatMessage;

public class LogBean {
    private ChatMessage chatMessage;
    private String time;
    private Class className;
    private Byte messageType;

    public LogBean() {
    }

    public LogBean(ChatMessage chatMessage, String time, Class className, Byte messageType) {
        this.chatMessage = chatMessage;
        this.time = time;
        this.className = className;
        this.messageType = messageType;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Class getClassName() {
        return className;
    }

    public void setClassName(Class className) {
        this.className = className;
    }

    public Byte getMessageType() {
        return messageType;
    }

    public void setMessageType(Byte messageType) {
        this.messageType = messageType;
    }
}
