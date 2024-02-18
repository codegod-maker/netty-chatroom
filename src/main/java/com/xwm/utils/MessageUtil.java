package com.xwm.utils;

import com.alibaba.fastjson2.JSONObject;
import com.xwm.beans.User;
import com.xwm.common.message.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.LinkedList;

public class MessageUtil {
    private static final Logger log = LoggerFactory.getLogger(MessageUtil.class);
    private static final String PARENT="offline_messages";
    private static final String SUFFIX=".message";
    private static final String MSG="msg";
    private static final String CLASS="class";
    static {
        File file = new File(PARENT);
        if(!file.exists()){
            file.mkdirs();
        }
    }
    public static void println(ChatMessage message,String content){
        System.out.println("Receive from 【"+message.from +"】 message：【"+ content+"】 time：【"+message.time+"】");
    }

    public static void print(ChatMessage message,String content){
        System.out.print("Receive from 【"+message.from +"】 message：【"+ content+"】 time：【"+message.time+"】");
    }

    public static Boolean sendOfflineMessage(ChatMessage chatMessage) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MSG,chatMessage);
        jsonObject.put(CLASS,chatMessage.messageType());
        String msg = jsonObject.toJSONString();
        String fileName = getFileName(chatMessage.to);
        File file = new File(fileName);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter appendWriter = null;
        try {
            appendWriter = new FileWriter(file,true);
            appendWriter.write(msg);
            appendWriter.write("\n");
            appendWriter.flush();
            appendWriter.close();
            log.error("保存离线消息成功,消息：{}",msg);
        } catch (IOException e) {
            log.error("保存离线消息失败,消息：{}",msg);
            return false;
        }
        return true;
    }

    private static String getFileName(String username){
        return PARENT+ "/" +username+SUFFIX;
    }

    /**
     *  用户登录时加载离线消息
     * @param user
     * @return
     */
    public static void loadOfflineMessageByFromUser(User user){
        String fileName = getFileName(user.getName());
        File file = new File(fileName);
        if(!file.exists()){
            return;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String s = null;
            while ((s = bufferedReader.readLine())!=null){
                JSONObject json = JSONObject.parseObject(s);
                ChatMessage chatMessage = (ChatMessage) json.getObject(MSG, ChatMessage.getMessageClass(json.getByte(CLASS)));
                Byte messageType = chatMessage.messageType();
                LinkedList<ChatMessage> messageLinkedList = user.getOfflineMessages().getOrDefault(messageType, new LinkedList<>());
                messageLinkedList.push(chatMessage);
                user.getOfflineMessages().put(messageType,messageLinkedList);
                s=null;
            }
        } catch (Exception e) {
            log.error("加载用户离线消息失败：{}",user.getName());
        }
    }
}
