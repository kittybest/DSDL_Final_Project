package com.example.zhanyuzhen.assistnet;

import java.net.Socket;

/**
 * Created by zhanyuzhen on 2016/6/19.
 */
public class SocketHandler {
    private static Socket socket;

    public static synchronized Socket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(Socket socket){
        SocketHandler.socket = socket;
    }
}
