package com.xwm.common.message;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.xwm.common.message.client.*;
import com.xwm.common.message.server.*;
import com.xwm.handler.WebsocketChatMessageHandler;
import com.xwm.log.LogBean;
import com.xwm.log.SaveFileLocalLogBeanServiceImpl;
import com.xwm.utils.IdUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class ChatMessage implements Serializable{

    private static final Logger log = LoggerFactory.getLogger(ChatMessage.class);

    public static Class getMessageClass(Byte messageType) {
        return classMap.get(messageType);
    }

    public String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS"));
    public String id = IdUtil.UUID();
    public String from;
    public String to;

    public abstract Byte messageType();

    @JSONField(serialize = false, deserialize = false)
    public static final Byte SYSTEM_MESSAGE_TYPE = 0;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte LOGIN_REQUEST_MESSAGE_TYPE = 1;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte LOGIN_RESPONSE_MESSAGE_TYPE = 2;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte SINGLE_CHAT_REQUEST_MESSAGE_TYPE = 3;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte GROUP_CHAT_REQUEST_MESSAGE_TYPE = 4;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte CREATE_GROUP_REQUEST_MESSAGE_TYPE = 5;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte SEE_GROUP_REQUEST_MESSAGE_TYPE = 6;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte JOIN_GROUP_REQUEST_MESSAGE_TYPE = 7;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte QUIT_GROUP_REQUEST_MESSAGE_TYPE = 8;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte LIST_SYSTEM_REQUEST_MESSAGE_TYPE = 9;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte LIST_GROUPS_REQUEST_MESSAGE_TYPE = 10;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte REGISTER_REQUEST_MESSAGE_TYPE = 11;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte REGISTER_RESPONSE_MESSAGE_TYPE = 12;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte CREATE_GROUP_RESPONSE_MESSAGE_TYPE = 13;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte GROUP_CHAT_RESPONSE_MESSAGE_TYPE = 14;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte JOIN_GROUP_RESPONSE_MESSAGE_TYPE = 15;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte LIST_GROUPS_RESPONSE_MESSAGE_TYPE = 16;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte SINGLE_CHAT_RESPONSE_MESSAGE_TYPE = 17;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte LIST_SYSTEM_RESPONSE_MESSAGE_TYPE = 18;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte SEE_GROUP_RESPONSE_MESSAGE_TYPE = 19;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte QUIT_GROUP_RESPONSE_MESSAGE_TYPE = 20;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte SEE_FRIEND_LIST_REQUEST_MESSAGE_TYPE = 21;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte SEE_FRIEND_LIST_RESPONSE_MESSAGE_TYPE = 22;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte SELECT_FRIEND_CHAT_REQUEST_MESSAGE_TYPE = 23;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte SELECT_FRIEND_CHAT_RESPONSE_MESSAGE_TYPE = 24;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte SELECT_GROUP_CHAT_REQUEST_MESSAGE_TYPE = 25;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte SELECT_GROUP_CHAT_RESPONSE_MESSAGE_TYPE = 26;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte APPLY_BEFRIEND_REQUEST_MESSAGE_TYPE = 27;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte APPLY_BEFRIEND_RESPONSE_MESSAGE_TYPE = 28;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte HANDLE_APPLY_BEFRIEND_REQUEST_MESSAGE_TYPE = 29;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte HANDLE_APPLY_BEFRIEND_RESPONSE_MESSAGE_TYPE = 30;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte SEE_HISTORY_CHAT_WITH_FRIEND_REQUEST_MESSAGE_TYPE = 31;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte SEE_HISTORY_CHAT_WITH_FRIEND_RESPONSE_MESSAGE_TYPE = 32;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte SEE_PERSONAL_INFO_REQUEST_MESSAGE_TYPE = 33;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte SEE_PERSONAL_INFO_RESPONSE_MESSAGE_TYPE = 34;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte PING_REQUEST_MESSAGE_TYPE = 35;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte FILE_TRANSFER_REQUEST_MESSAGE_TYPE = 36;
    @JSONField(serialize = false, deserialize = false)
    public static final Byte FILE_TRANSFER_RESPONSE_MESSAGE_TYPE = 37;

    public static final Map<Byte, Class> classMap = new HashMap<>();

    static {
        classMap.put(SYSTEM_MESSAGE_TYPE, SystemMessage.class);

        classMap.put(LOGIN_REQUEST_MESSAGE_TYPE, UserLoginRequestMessage.class);
        classMap.put(LOGIN_RESPONSE_MESSAGE_TYPE, UserLoginResponseMessage.class);

        classMap.put(REGISTER_REQUEST_MESSAGE_TYPE, UserRegisterRequestMessage.class);
        classMap.put(REGISTER_RESPONSE_MESSAGE_TYPE, UserRegisterResponseMessage.class);

        classMap.put(SINGLE_CHAT_REQUEST_MESSAGE_TYPE, SingleChatRequestMessage.class);
        classMap.put(SINGLE_CHAT_RESPONSE_MESSAGE_TYPE, SingleChatResponseMessage.class);

        classMap.put(GROUP_CHAT_REQUEST_MESSAGE_TYPE, GroupChatRequestMessage.class);
        classMap.put(GROUP_CHAT_RESPONSE_MESSAGE_TYPE, GroupChatResponseMessage.class);

        classMap.put(CREATE_GROUP_REQUEST_MESSAGE_TYPE, CreateGroupRequestMessage.class);
        classMap.put(CREATE_GROUP_RESPONSE_MESSAGE_TYPE, CreateGroupResponseMessage.class);

        classMap.put(JOIN_GROUP_REQUEST_MESSAGE_TYPE, JoinGroupRequestMessage.class);
        classMap.put(JOIN_GROUP_RESPONSE_MESSAGE_TYPE, JoinGroupResponseMessage.class);


        classMap.put(SEE_GROUP_REQUEST_MESSAGE_TYPE, SeeGroupMembersRequestMessage.class);
        classMap.put(SEE_GROUP_RESPONSE_MESSAGE_TYPE, SeeGroupMembersResponseMessage.class);


        classMap.put(QUIT_GROUP_REQUEST_MESSAGE_TYPE, QuitGroupRequestMessage.class);
        classMap.put(QUIT_GROUP_RESPONSE_MESSAGE_TYPE, QuitGroupResponseMessage.class);

        classMap.put(LIST_SYSTEM_REQUEST_MESSAGE_TYPE, OnlineMemberListRequestMessage.class);
        classMap.put(LIST_SYSTEM_RESPONSE_MESSAGE_TYPE, OnlineMemberListResponseMessage.class);

        classMap.put(LIST_GROUPS_REQUEST_MESSAGE_TYPE, SeeGroupListRequestMessage.class);
        classMap.put(LIST_GROUPS_RESPONSE_MESSAGE_TYPE, SeeGroupListResponseMessage.class);

        classMap.put(SEE_FRIEND_LIST_REQUEST_MESSAGE_TYPE,SeeFriendListRequestMessage.class);
        classMap.put(SEE_FRIEND_LIST_RESPONSE_MESSAGE_TYPE,SeeFriendListResponseMessage.class);

        classMap.put(SELECT_FRIEND_CHAT_REQUEST_MESSAGE_TYPE,SelectFriendChatRequestMessage.class);
        classMap.put(SELECT_FRIEND_CHAT_RESPONSE_MESSAGE_TYPE,SelectFriendChatResponseMessage.class);

        classMap.put(SELECT_GROUP_CHAT_REQUEST_MESSAGE_TYPE,SelectGroupChatRequestMessage.class);
        classMap.put(SELECT_GROUP_CHAT_RESPONSE_MESSAGE_TYPE,SelectGroupChatResponseMessage.class);

        classMap.put(APPLY_BEFRIEND_REQUEST_MESSAGE_TYPE,ApplyBeFriendRequestMessage.class);
        classMap.put(APPLY_BEFRIEND_RESPONSE_MESSAGE_TYPE,ApplyBeFriendResponseMessage.class);

        classMap.put(HANDLE_APPLY_BEFRIEND_REQUEST_MESSAGE_TYPE,HandleApplyBeFriendRequestMessage.class);
        classMap.put(HANDLE_APPLY_BEFRIEND_RESPONSE_MESSAGE_TYPE,HandleApplyBeFriendResponseMessage.class);

        classMap.put(SEE_HISTORY_CHAT_WITH_FRIEND_REQUEST_MESSAGE_TYPE,SeeHistoryChatWithFriendRequestMessage.class);
        classMap.put(SEE_HISTORY_CHAT_WITH_FRIEND_RESPONSE_MESSAGE_TYPE,SeeHistoryChatWithFriendResponseMessage.class);

        classMap.put(SEE_PERSONAL_INFO_REQUEST_MESSAGE_TYPE,SeePersonalInfoRequestMessage.class);
        classMap.put(SEE_PERSONAL_INFO_RESPONSE_MESSAGE_TYPE,SeePersonalInfoResponseMessage.class);

        classMap.put(PING_REQUEST_MESSAGE_TYPE,PingRequestMessage.class);

        classMap.put(FILE_TRANSFER_REQUEST_MESSAGE_TYPE,FileTransferRequestMessage.class);
        classMap.put(FILE_TRANSFER_RESPONSE_MESSAGE_TYPE,FileTransferResponseMessage.class);

    }

    public void beforeDoServerLog(ChannelHandlerContext ctx, ChatMessage msg){
        String time=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS"));
        log.info("服务器收到消息，日志打印   ====>   time：【{}】，msg：【{}】", time, JSONObject.toJSONString(msg));
        LogBean logBean = new LogBean(msg, time, msg.getClass(),msg.messageType());
        ctx.executor().schedule(new SaveFileLocalLogBeanServiceImpl(logBean), 500, TimeUnit.MILLISECONDS);
    }

    public abstract void doServer(ChannelHandlerContext channel, ChatMessage msg);

    public abstract void doClient(ChannelHandlerContext channel, ChatMessage msg);

    public Object messageAdapter(ChannelHandlerContext channel){
        if(channel.handler() instanceof WebsocketChatMessageHandler){
            return new TextWebSocketFrame(JSONObject.toJSONString(this));
        }
        else {
            return this;
        }
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
