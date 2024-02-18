package com.xwm.log;

import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SaveFileLocalLogBeanServiceImpl implements LogBeanService{
    private static final Logger log = LoggerFactory.getLogger(SaveFileLocalLogBeanServiceImpl.class);
    private static final String PATH="logs/channel";
    private static final String PARENT="logs";
    static {
        File file = new File(PARENT);
        if(!file.exists()){
            file.mkdirs();
        }
    }
    private LogBean logBean;
    private SaveFileLocalLogBeanServiceImpl(){}
    public SaveFileLocalLogBeanServiceImpl(LogBean logBean){
        this.logBean=logBean;
    }
    @Override
    public void saveLogBean(LogBean logBean) {
        String filePath=PATH+".log";
        File file = new File(filePath);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            if(file.length() >= 1024*1024 ){
                String destPath=PATH+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS"))+".log";
                FileChannel in = new FileInputStream(filePath).getChannel();
                FileChannel out = new FileOutputStream(destPath).getChannel();
                out.transferFrom(in,0,in.size());
                out.close();
                in.close();
            }
            FileWriter appendWriter = new FileWriter(file,true);
            appendWriter.write(JSONObject.toJSONString(logBean));
            appendWriter.write("\n");
            appendWriter.flush();
            appendWriter.close();
        } catch (IOException e) {
            log.error("日志操作失败！logBean：【{}】",JSONObject.toJSONString(logBean));
        }
    }

    @Override
    public void run() {
        saveLogBean(logBean);
    }
}
