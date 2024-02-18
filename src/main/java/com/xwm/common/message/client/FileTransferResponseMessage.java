package com.xwm.common.message.client;

import com.xwm.common.message.ChatMessage;
import com.xwm.utils.MessageUtil;
import io.netty.channel.ChannelHandlerContext;

import javax.swing.filechooser.FileSystemView;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class FileTransferResponseMessage extends ChatMessage {
    @Override
    public Byte messageType() {
        return FILE_TRANSFER_RESPONSE_MESSAGE_TYPE;
    }

    @Override
    public void doServer(ChannelHandlerContext channel, ChatMessage msg) {

    }

    @Override
    public void doClient(ChannelHandlerContext channel, ChatMessage msg) {
        FileTransferResponseMessage fileTransferResponseMessage = (FileTransferResponseMessage)msg;
        if(!fileTransferResponseMessage.isTransferSuccess()){
            MessageUtil.println(fileTransferResponseMessage,fileTransferResponseMessage.content);
            return;
        }
        byte[] decodeStr = fileTransferResponseMessage.getDecodeStr();
        String fileName = fileTransferResponseMessage.getFileName();
        String destPath = fileTransferResponseMessage.getDestPath();
        if(destPath == null || destPath.trim().equals("")){
            destPath = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
        }
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destPath + "//" + fileName));
            bufferedOutputStream.write(decodeStr);
            bufferedOutputStream.flush();
            fileTransferResponseMessage.setContent("接受："+fileTransferResponseMessage.from+" 的文件成功！文件名："+fileName+"，存储路径："+destPath);
            MessageUtil.println(fileTransferResponseMessage,fileTransferResponseMessage.getContent());
        } catch (IOException e) {
            fileTransferResponseMessage.setContent("接受："+fileTransferResponseMessage.from+" 的文件失败！文件名："+fileName+"，存储路径："+destPath);
            MessageUtil.println(fileTransferResponseMessage,fileTransferResponseMessage.getContent());
            e.printStackTrace();
        }finally {
            if(bufferedOutputStream!=null){
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private byte[] decodeStr;
    private String fileName;
    private String content;
    private String destPath;
    private boolean transferSuccess;

    public FileTransferResponseMessage(String from,String to,byte[] decodeStr, String fileName, String content,boolean transferSuccess) {
        super.from = from;
        super.to = to;
        this.decodeStr = decodeStr;
        this.fileName = fileName;
        this.content = content;
        this.transferSuccess = transferSuccess;
    }

    public FileTransferResponseMessage(String from,String to) {
        super.from = from;
        super.to = to;
    }

    public FileTransferResponseMessage() {

    }

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }

    public boolean isTransferSuccess() {
        return transferSuccess;
    }

    public void setTransferSuccess(boolean transferSuccess) {
        this.transferSuccess = transferSuccess;
    }

    public byte[] getDecodeStr() {
        return decodeStr;
    }

    public void setDecodeStr(byte[] decodeStr) {
        this.decodeStr = decodeStr;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
