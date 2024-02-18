package com.xwm.handler;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.util.BeanUtils;
import com.xwm.beans.User;
import com.xwm.common.message.ChatMessage;
import com.xwm.common.message.client.FileTransferResponseMessage;
import com.xwm.common.message.server.FileTransferRequestMessage;
import com.xwm.common.message.server.PingRequestMessage;
import com.xwm.managers.UserManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessageHandler extends SimpleChannelInboundHandler<ChatMessage> {
    private static final Logger log = LoggerFactory.getLogger(ChatMessageHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) throws Exception {
        if(msg instanceof PingRequestMessage){

        }
        else if(msg instanceof FileTransferRequestMessage){
            FileTransferRequestMessage fileTransferRequestMessage = new FileTransferRequestMessage();
            BeanUtil.copyProperties(msg,fileTransferRequestMessage,"encodeStr");
            msg.beforeDoServerLog(ctx,fileTransferRequestMessage);
        }
        else if(msg instanceof FileTransferResponseMessage){
            FileTransferResponseMessage fileTransferResponseMessage = new FileTransferResponseMessage();
            BeanUtil.copyProperties(msg,fileTransferResponseMessage,"decodeStr");
            msg.beforeDoServerLog(ctx,fileTransferResponseMessage);
        }
        else {
            msg.beforeDoServerLog(ctx,msg);
        }
        msg.doServer(ctx,msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if(ctx.channel()!=null){
            User user = UserManager.getMe().getUserByChannel(ctx.channel());
            if(user == null){
                log.error("用户登录时断开连接,时间：{}，客户端：{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS")),ctx.channel().toString());
            }
            else {
                String username = UserManager.getMe().getUsernameByChannel(ctx.channel());
                UserManager.getMe().deleteUserAndChannel(username);
                log.warn("用户退出，username：{}",username);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(ctx.channel()!=null){
            User user = UserManager.getMe().getUserByChannel(ctx.channel());
            if(user == null){
                log.error("用户登录时断开连接");
            }
            else {
                String username = UserManager.getMe().getUsernameByChannel(ctx.channel());
                log.warn("玩家异常，断开连接，username：{}",username);
            }
        }
        ctx.fireExceptionCaught(cause);
    }
}
