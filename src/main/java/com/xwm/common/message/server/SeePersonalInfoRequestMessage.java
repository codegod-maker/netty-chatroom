package com.xwm.common.message.server;

import com.xwm.beans.User;
import com.xwm.common.message.ChatMessage;
import com.xwm.common.message.client.SeePersonalInfoResponseMessage;
import com.xwm.managers.UserManager;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SeePersonalInfoRequestMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return SEE_PERSONAL_INFO_REQUEST_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {
        SeePersonalInfoRequestMessage seePersonalInfoRequestMessage = (SeePersonalInfoRequestMessage)msg;
        User user = UserManager.getMe().findUser(seePersonalInfoRequestMessage.from);
        Map<Byte, LinkedList<ChatMessage>> offlineMessages = user.getOfflineMessages();
        Map<String,Integer> lists = new HashMap<>();
        offlineMessages.values().stream().forEach(i->{
            Map<String, List<ChatMessage>> collect = i.stream().collect(Collectors.groupingBy(ChatMessage::getFrom));
            collect.entrySet().stream().forEach((k)->{
                String key = k.getKey();
                List<ChatMessage> value = k.getValue();
                Integer integer = lists.getOrDefault(key,0);
                integer = integer+value.size();
                lists.put(key,integer);
            });
        });
        SeePersonalInfoResponseMessage personalInfoResponseMessage = new SeePersonalInfoResponseMessage("系统", seePersonalInfoRequestMessage.from, user);
        personalInfoResponseMessage.setMessages(lists);
        channel.channel().writeAndFlush(personalInfoResponseMessage);
    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {

    }

    public SeePersonalInfoRequestMessage(String from) {
        super.from = from;
    }
}
