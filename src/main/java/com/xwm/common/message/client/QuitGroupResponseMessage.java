package com.xwm.common.message.client;

import com.xwm.common.message.ChatMessage;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

public class QuitGroupResponseMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return QUIT_GROUP_RESPONSE_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {

    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
        QuitGroupResponseMessage quitGroupResponseMessage = (QuitGroupResponseMessage)msg;
        MessageUtil.println(quitGroupResponseMessage, quitGroupResponseMessage.getContent());
    }
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public QuitGroupResponseMessage(String from,String to,String content) {
        this.content = content;
        super.from = from;
        super.to = to;
    }
}
