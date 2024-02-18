package com.xwm.utils;

import java.util.UUID;

public class IdUtil {

    public static String UUID(){
        return UUID.randomUUID().toString().toLowerCase();
    }

    public static String simpleUUID(){
        return UUID.randomUUID().toString().replaceAll("-","").toLowerCase();
    }
}
