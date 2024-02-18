package com.xwm.tests;

import com.alibaba.fastjson2.JSONObject;
import com.xwm.codec.ChatMessageCodec;
import com.xwm.common.message.server.UserLoginRequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;

public class Test {
    public static void main(String[] args) throws Exception {
        UserLoginRequestMessage user = new UserLoginRequestMessage("123", "fsdfsd");
        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(LogLevel.DEBUG),
                new LengthFieldBasedFrameDecoder(1024, 5, 4, 0, 0),
                new ChatMessageCodec()
        );

        // 测试编码与解码
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        byte[] bytes = JSONObject.toJSONString(user).getBytes(Charset.defaultCharset());
        byteBuf.writeBytes(new byte[]{'b', 'a', 'b', 'y'});
        byteBuf.writeByte(user.messageType());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        channel.writeInbound(byteBuf);
    }
}
