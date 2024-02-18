package com.xwm.server;

import com.xwm.codec.ChatMessageCodec;
import com.xwm.handler.ChatMessageHandler;
import com.xwm.managers.UserManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpNettyServer {
    private static final Logger log = LoggerFactory.getLogger(TcpNettyServer.class);

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.channel(NioServerSocketChannel.class);
            server.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024*1024*500, 5, 4, 0, 0));
                    socketChannel.pipeline().addLast(new ChatMessageCodec());
                    socketChannel.pipeline().addLast(new ChatMessageHandler());
                    // 用于空闲连接的检测，5s内未读到数据，会触发READ_IDLE事件
                    socketChannel.pipeline().addLast(new IdleStateHandler(5, 0, 0));
                    socketChannel.pipeline().addLast(new ChannelDuplexHandler() {
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
                            // 获得事件
                            IdleStateEvent event = (IdleStateEvent) evt;
                            if (event.state() == IdleState.WRITER_IDLE) {
                                // 断开连接
                                log.error("心跳检测失败，即将退出，客户端信息：{}", ctx.channel().remoteAddress().toString());
                                ctx.channel().close();
                            }
                        }
                    });
                }
            });
            ChannelFuture channelFuture = server.group(boss, worker)
                    .bind(1024)
                    .sync();
            System.out.println("tcp服务端启动完成！");
            channelFuture.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
