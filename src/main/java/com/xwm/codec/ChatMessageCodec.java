package com.xwm.codec;

import com.alibaba.fastjson2.JSONObject;
import com.xwm.common.message.ChatMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ChatMessageCodec extends ByteToMessageCodec<ChatMessage> {
    @Override
    public void encode(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage, ByteBuf byteBuf) throws Exception {
        byte[] bytes = JSONObject.toJSONString(chatMessage).getBytes(StandardCharsets.UTF_8);
        byteBuf.writeBytes(new byte[]{'b', 'a', 'b', 'y'});
        byteBuf.writeByte(chatMessage.messageType());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int readInt = byteBuf.readInt();
        byte type = byteBuf.readByte();
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes, 0, length);
        list.add(JSONObject.parseObject(new String(bytes, StandardCharsets.UTF_8), ChatMessage.getMessageClass(type)));
    }
}
