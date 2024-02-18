package com.xwm.server;

public class MainServer {
    public static void main(String[] args){
        new Thread(()->{
            try {
                WebsocketNettyServer.main(args);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            try {
                TcpNettyServer.main(args);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
