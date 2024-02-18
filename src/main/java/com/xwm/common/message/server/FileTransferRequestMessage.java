package com.xwm.common.message.server;


import com.xwm.beans.User;
import com.xwm.common.message.ChatMessage;
import com.xwm.common.message.client.FileTransferResponseMessage;
import com.xwm.common.message.client.SingleChatResponseMessage;
import com.xwm.managers.UserManager;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class FileTransferRequestMessage extends ChatMessage {
    public static void main(String[] args) throws Exception {
       /* String url="C:\\Users\\Administrator\\Pictures\\Saved Pictures\\bizhi1.jpg";
        String dest="D:\\test.jpg";
        File file = new File(url);
        Long length = file.length();
        byte[] bytes = new byte[length.intValue()];
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(url));
        bufferedInputStream.read(bytes);
        String s = new String(Base64.getEncoder().encode(bytes), Charset.forName("UTF-8"));
        byte[] decode = Base64.getDecoder().decode(s.getBytes(StandardCharsets.UTF_8));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(dest));
        bufferedOutputStream.write(decode);
        bufferedOutputStream.flush();*/
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        System.out.println(fileSystemView.getHomeDirectory().getAbsolutePath());
    }

    @Override
    public Byte messageType() {
        return FILE_TRANSFER_REQUEST_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {
        FileTransferRequestMessage fileTransferRequestMessage = (FileTransferRequestMessage)msg;
        String from = fileTransferRequestMessage.from;
        byte[] encodeStr = fileTransferRequestMessage.encodeStr;
        String fileName = fileTransferRequestMessage.fileName;
        String destPath = fileTransferRequestMessage.destPath;
        String content = "";
        User user = UserManager.getMe().findUser(from);
        byte chatType = user.getChatType();
        if(chatType != ChatMessage.SINGLE_CHAT_REQUEST_MESSAGE_TYPE){
            content = "目前只支持单人文件发送，请先选择单聊后再操作";
            FileTransferResponseMessage fileTransferResponseMessage = new FileTransferResponseMessage("系统", from, null, "", content, false);
            user.getChannel().writeAndFlush(fileTransferResponseMessage.messageAdapter(channel));
            return;
        }
        else {
            String to = user.getCurrentSelect();
            FileTransferResponseMessage fileTransferResponseMessage = new FileTransferResponseMessage(from,to);
            content = "发送成功";
            FileTransferResponseMessage fileTransferResponseMessageFrom = new FileTransferResponseMessage(from, to, null, "", content, false);
            channel.channel().writeAndFlush(fileTransferResponseMessageFrom.messageAdapter(channel));
            fileTransferResponseMessage.setContent(content);
            boolean transferSuccess = true;
            fileTransferResponseMessage.setFileName(fileName);
            fileTransferResponseMessage.setTransferSuccess(transferSuccess);
            fileTransferResponseMessage.setDestPath(destPath);
            fileTransferResponseMessage.setDecodeStr(encodeStr);
            User toUser = UserManager.getMe().findUser(to);
            if(toUser == null){
                MessageUtil.sendOfflineMessage(fileTransferResponseMessage);
            }
            else {
                toUser.getChannel().writeAndFlush(fileTransferResponseMessage.messageAdapter(channel));
            }
        }
    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {

    }

    private String fileName;
    private String destPath;
    private byte[] encodeStr;

    public FileTransferRequestMessage(String from,String fileName) {
        super.from = from;
        this.fileName = fileName;
    }

    public FileTransferRequestMessage() {

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getEncodeStr() {
        return encodeStr;
    }

    public void setEncodeStr(byte[] encodeStr) {
        this.encodeStr = encodeStr;
    }

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }
}
