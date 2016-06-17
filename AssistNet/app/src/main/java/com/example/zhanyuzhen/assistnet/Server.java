package com.example.zhanyuzhen.assistnet;

import android.util.Log;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhanyuzhen on 2016/6/16.
 */
public class Server extends Thread {
    private boolean OutServer = false;
    private ServerSocket server;
    private final int ServerPort = 8765;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private String input;
    private JSONObject json;
    ArrayList<JSONObject> list = new ArrayList<JSONObject>();
    private Socket socket = new Socket();

    public Server(){
        try{
            server = new ServerSocket(ServerPort);
            server.setSoTimeout(20000);
        } catch(IOException e){
            System.out.println("Server fault!");
            System.out.println("IOException: " + e.toString());
        }
        String ip = getLocalIpAddress();
        System.out.println("ip = " + ip);

        //try put in list
        Map map_1 = new HashMap();
        map_1.put("Title", "pot");
        map_1.put("Content", "need a pot for performance.");
        map_1.put("author", "Doris");
        map_1.put("id", 1);
        JSONObject first = new JSONObject(map_1);
        list.add(first);
        Map map_2 = new HashMap();
        map_2.put("Title", "hat");
        map_2.put("Content", "two hats for travel.");
        map_2.put("author", "James");
        map_2.put("id", 1);
        JSONObject second = new JSONObject(map_2);
        list.add(second);
        Map map_3 = new HashMap();
        map_3.put("Title", "dress");
        map_3.put("Content", "two dresses for party.");
        map_3.put("author", "Doris");
        map_3.put("id", 2);
        JSONObject third = new JSONObject(map_3);
        list.add(third);
    }
    public void run(){
        System.out.println("Server start!");
        while(!OutServer){
            socket = null;
            try{
                synchronized (server){
                    socket = server.accept();
                }
               // socket = server.accept();
                System.out.println("Connected, InetAddress = " + socket.getInetAddress());
                socket.setSoTimeout(20000);
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());

                while(!((input = inputStream.readUTF()).equals("Bye"))){
                    //client request for data
                    if(input.equals("Data")){
                        for(int i = 0; i < list.size(); i ++){
                            outputStream.writeUTF(list.get(i).toString());
                        }
                        outputStream.writeUTF("Data End");
                    }

                }
                outputStream.close();
                outputStream = null;
                inputStream.close();
                inputStream = null;
                socket.close();
            } catch(IOException e){
                System.out.println("Socket fault!");
                System.out.println("IOException: " + e.toString());
            }
        }
    }
    //public void main(String args[]){
    //    (new Server()).start();
    //}
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("cannot get ip");
        }
        return null;
    }
}
