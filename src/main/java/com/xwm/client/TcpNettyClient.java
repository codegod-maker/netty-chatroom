package com.xwm.client;

import com.xwm.codec.ChatMessageCodec;
import com.xwm.common.locks.JoinGroupLock;
import com.xwm.common.locks.LoginLock;
import com.xwm.common.locks.RegisterLock;
import com.xwm.common.message.*;
import com.xwm.beans.User;
import com.xwm.common.message.server.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class TcpNettyClient {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap client = new Bootstrap();
            ChannelFuture channelFuture = client.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            nioSocketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024*1024*500, 5, 4, 0, 0));
                            nioSocketChannel.pipeline().addLast(new ChatMessageCodec());
                            // 发送心跳包，让服务器知道客户端在线
                            // 3s未发生WRITER_IDLE，就像服务器发送心跳包
                            // 该值为服务器端设置的READER_IDLE触发时间的一半左右
                            nioSocketChannel.pipeline().addLast(new IdleStateHandler(0, 3, 0));
                            nioSocketChannel.pipeline().addLast(new SimpleChannelInboundHandler<ChatMessage>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) throws Exception {
                                    msg.doClient(ctx,msg);
                                }

                                @Override
                                public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                    IdleStateEvent event = (IdleStateEvent) evt;
                                    if (event.state() == IdleState.WRITER_IDLE) {
                                        // 发送心跳包
                                        ctx.writeAndFlush(new PingRequestMessage());
                                    }
                                }
                            });
                        }
                    })
                    //123.207.223.130
                    .connect("123.207.223.130", 1024)
                    .sync();
            Channel channel = channelFuture.channel();
            new Thread(() -> {
                System.out.println("连接成功");
                handleReg(channel);
                try {
                    handleLogin(channel);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }).start();
            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

    }

    private static void handleLogin(Channel channel) throws InterruptedException, BrokenBarrierException, TimeoutException {
        System.out.println("请输入用户名：");
        Scanner scanner1 = new Scanner(System.in);
        String username = "";
        while (scanner1.hasNextLine()) {
            username = scanner1.nextLine();
            break;
        }
        System.out.println("请输入密码：");
        Scanner scanner2 = new Scanner(System.in);
        String pwd = "";
        while (scanner2.hasNextLine()) {
            pwd = scanner2.nextLine();
            break;
        }
        channel.writeAndFlush(new UserLoginRequestMessage(username, pwd));
        try {
            LoginLock.await(60L, TimeUnit.SECONDS);
        } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
            LoginLock.await(60L, TimeUnit.SECONDS);
            LoginLock.compareAndSet(1, -1);
        }
        LoginLock.reset();
        User user = new User(username,pwd, channel);
        if (LoginLock.get() == 0) {
            LoginLock.compareAndSet(0,1);
            handleReg(channel);
            handleLogin(channel);
        } else if (LoginLock.get() == 2) {
            doSomething(user);
        } else if(LoginLock.get() == 3){
            LoginLock.compareAndSet(3,1);
            handleReg(channel);
            handleLogin(channel);
        }
        else {
            System.out.println("服务器内部错误");
            channel.close();
        }
    }

    private static void handleReg(Channel channel) {
        System.out.println("==================================");
        System.out.println("login");
        System.out.println("register");
        System.out.println("exit");
        System.out.println("==================================");
        System.out.println("请输入指令：");
        Scanner scanner = new Scanner(System.in);
        String cmd = "";
        while (scanner.hasNextLine()) {
            cmd = scanner.nextLine();
            break;
        }
        if("register".equals(cmd)){
            System.out.println("请输入用户名：");
            Scanner scanner1 = new Scanner(System.in);
            String username = "";
            while (scanner1.hasNextLine()) {
                username = scanner1.nextLine();
                break;
            }
            System.out.println("请输入密码：");
            Scanner scanner2 = new Scanner(System.in);
            String pwd = "";
            while (scanner2.hasNextLine()) {
                pwd = scanner2.nextLine();
                break;
            }
            channel.writeAndFlush(new UserRegisterRequestMessage(username,pwd));
            try {
                RegisterLock.await(60L,TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                RegisterLock.countDown();
                RegisterLock.compareAndSet(1, -1);
            }
            if (RegisterLock.get() == 2) {
                System.out.println("注册成功");
            } else if (RegisterLock.get() == 0) {
                System.out.println("注册失败");
                handleReg(channel);
            } else {
                System.out.println("服务器内部错误");
                handleReg(channel);
            }
        }
        else if("exit".equals(cmd)){
            System.exit(0);
        }
        else if(!"login".equals(cmd)){
            handleReg(channel);
        }
    }

    private static void doSomething(User user) {
        try {
            printCmd();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("请输入指令：");
                String cmd = scanner.nextLine();
                String[] strings = cmd.split(" ");
                String content="";
                String groupName="";
                String to="";
                Set<String> users=null;
                switch (strings[0]) {
                    case "flist":
                        to = "系统";
                        SeeFriendListRequestMessage seeFriendListRequestMessage = new SeeFriendListRequestMessage(user.getName(), to);
                        user.getChannel().writeAndFlush(seeFriendListRequestMessage);
                        continue;
                    case "glist":
                        user.getChannel().writeAndFlush(new SeeGroupListRequestMessage(user.getName()));
                        continue;
                    case "fselect":
                        to = strings[1];
                        SelectFriendChatRequestMessage selectFriendChatRequestMessage = new SelectFriendChatRequestMessage(user.getName(),to);
                        user.getChannel().writeAndFlush(selectFriendChatRequestMessage);
                        continue;
                    case "gselect":
                        to = strings[1];
                        SelectGroupChatRequestMessage selectGroupChatRequestMessage = new SelectGroupChatRequestMessage(user.getName(),to);
                        user.getChannel().writeAndFlush(selectGroupChatRequestMessage);
                        continue;
                    case "fapply":
                        to = strings[1];
                        ApplyBeFriendRequestMessage applyBeFriendRequestMessage = new ApplyBeFriendRequestMessage(user.getName(),to);
                        user.getChannel().writeAndFlush(applyBeFriendRequestMessage);
                        continue;
                    case "happly":
                        to = strings[1];
                        Boolean isAgree=strings[2].equals("true");
                        HandleApplyBeFriendRequestMessage handleApplyBeFriendRequestMessage = new HandleApplyBeFriendRequestMessage(user.getName(),to,isAgree);
                        user.getChannel().writeAndFlush(handleApplyBeFriendRequestMessage);
                        continue;
                    case "hfriend":
                        Integer limit = null;
                        try {
                            if(strings.length==1){
                                limit = Integer.MAX_VALUE;
                            }
                            else {
                                limit = Integer.valueOf(strings[1]);
                            }
                        } catch (NumberFormatException e) {
                            limit = Integer.MAX_VALUE;
                        }
                        SeeHistoryChatWithFriendRequestMessage seeHistoryChatWithFriendRequestMessage = new SeeHistoryChatWithFriendRequestMessage(user.getName(), limit);
                        user.getChannel().writeAndFlush(seeHistoryChatWithFriendRequestMessage);
                        continue;
                    case "send":
                        content = cmd.substring(5);
                        SingleChatRequestMessage singleRequestMessage = new SingleChatRequestMessage(user.getName(),content);
                        user.getChannel().writeAndFlush(singleRequestMessage);
                        continue;
                    case "gsend":
                        content = cmd.substring(6);
                        GroupChatRequestMessage groupChatRequestMessage = new GroupChatRequestMessage(user.getName(),content);
                        user.getChannel().writeAndFlush(groupChatRequestMessage);
                        continue;
                    case "gcreate":
                        groupName=strings[1];
                        users=new HashSet(Arrays.asList(strings[2].split(",")));
                        users.add(user.getName());
                        CreateGroupRequestMessage createGroupRequestMessage = new CreateGroupRequestMessage(groupName, user.getName(), users);
                        user.getChannel().writeAndFlush(createGroupRequestMessage);
                        continue;
                    case "gmembers":
                        groupName=strings[1];
                        SeeGroupMembersRequestMessage seeGroupMembersRequestMessage = new SeeGroupMembersRequestMessage(groupName,user.getName());
                        user.getChannel().writeAndFlush(seeGroupMembersRequestMessage);
                        continue;
                    case "gjoin":
                        groupName=strings[1];
                        user.getChannel().writeAndFlush(new JoinGroupRequestMessage(groupName,user.getName()));
                        JoinGroupLock.await();
                        continue;
                    case "gquit":
                        groupName=strings[1];
                        user.getChannel().writeAndFlush(new QuitGroupRequestMessage(groupName,user.getName()));
                        continue;
                    case "olist":
                        user.getChannel().writeAndFlush(new OnlineMemberListRequestMessage(user.getName()));
                        continue;
                    case "fsend":
                        String source = strings[1];
                        String dest = "";
                        if(strings.length>2){
                            dest=strings[2];
                        }
                        File file = new File(source);
                        if(!file.exists()){
                            System.out.println("文件不存在或路径错误，请重新输入");
                            continue;
                        }
                        String name = file.getName();
                        Long length = file.length();
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                        byte[] bytes = new byte[length.intValue()];
                        bufferedInputStream.read(bytes);
                        FileTransferRequestMessage fileTransferRequestMessage = new FileTransferRequestMessage(user.getName(), name);
                        fileTransferRequestMessage.setEncodeStr(bytes);
                        fileTransferRequestMessage.setDestPath(dest);
                        user.getChannel().writeAndFlush(fileTransferRequestMessage);
                        continue;
                    case "info":
                        user.getChannel().writeAndFlush(new SeePersonalInfoRequestMessage(user.getName()));
                        continue;
                    case "cmd":
                        printCmd();
                        continue;
                    case "quit":
                        user.getChannel().close();
                        return;
                    default:
                        System.out.println("指令有误，请重新输入");
                }
            }
        } catch (Exception e) {
            System.out.println("指令有误");
            doSomething(user);
        }
    }

    private static void printCmd(){
        System.out.println("==================================");
        System.out.println("cmd：flist | description：查看好友列表");
        System.out.println("cmd：glist | description：查看群组列表");
        System.out.println("cmd：fselect [username] | description：选择好友聊天");
        System.out.println("cmd：gselect [group name] | description：选择群组聊天");
        System.out.println("cmd：fapply [username] | description：申请添加好友");
        System.out.println("cmd：happly [username] [isAgree]| description：处理好友申请");
        System.out.println("cmd：hfriend [limit]| description：查看与该好友的聊天记录");
        System.out.println("cmd：hgroup [limit]| description：查看与该群组的聊天记录");
        System.out.println("cmd：send [content] | description：发送单聊信息，需要先选择好友");
        System.out.println("cmd：gsend [content] | description：发送群聊信息，需要先选择群组");
        System.out.println("cmd：gcreate [group name] [m1,m2,m3...] | description：创建群组");
        System.out.println("cmd：gmembers [group name] | description：查看组内成员");
        System.out.println("cmd：gjoin [group name] | description：申请加入群组");
        System.out.println("cmd：gquit [group name] | description：退出群组");
        System.out.println("cmd：fsend [source absolute path] [dest absolute path]| description：发送单人文件，文件大小尽量小于500M，目标路径不填写默认发送到对方桌面");
        System.out.println("cmd：info | description：查看个人信息");
        System.out.println("cmd：olist | description：查看聊天室在线用户列表");
        System.out.println("cmd：cmd | description：查看指令列表");
        System.out.println("cmd：quit | description：退出系统");
        System.out.println("==================================");
    }
}
