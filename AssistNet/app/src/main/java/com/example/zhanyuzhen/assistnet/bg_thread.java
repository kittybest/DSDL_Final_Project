package com.example.zhanyuzhen.assistnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class bg_thread extends AppCompatActivity {

    private String address = "10.5.2.56";
    private int ClientPort = 8765;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Socket socket;
    private String input;
    private JSONObject json;
    ArrayList<String> list = new ArrayList<String>();
    Intent intent = new Intent();
    Bundle bundle;
    Thread thread;
    int request;
   // String reg_status;
    String Account = "";
    String Password = "";
    /*int Login = 0;
    int Register = 1;
    int Load = 2;
    int Add = 3;
    int Bye = 10;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        thread = new Thread(client_request);
        thread.start();

    }


   /* private Runnable load = new Runnable(){
        @Override
        public void run(){
            socket = new Socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(address, ClientPort);
            try{
                socket.connect(inetSocketAddress, 20000);
                System.out.println("Socket success!");
            } catch(IOException e){
                System.out.println("Socket Fault! from client");
                System.out.println("IOException: " + e.toString());
            }
            SocketHandler.setSocket(socket);
            //initialize input and output stream
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                System.out.println("client I/O Fault!");
                System.out.println("IOException: " + e.toString());
            }
            System.out.println("load");
            list.clear();
            try {
                outputStream.writeUTF("Data");
                try {
                    while(!((input = inputStream.readUTF()).equals("Data End"))){
                        list.add(input);
                        //json = new JSONObject(input);
                        //list.add(json);
                    }
                } catch (IOException e) {
                    System.out.println("Data fault!");
                    System.out.println("IOException: " + e.toString());
                } /*catch (JSONException e) {
                    System.out.println("Data fault!");
                    System.out.println("JSONException: " + e.toString());
                }
            } catch (IOException e) {
                System.out.println("Data fault!");
                System.out.println("IOException: " + e.toString());
            }
        }
    };*/

    public String login(String Account, String Password){
            //request data list
            try {
                outputStream.writeUTF("LogIn");
                outputStream.writeUTF(Account);
                outputStream.writeUTF(Password);
                input = inputStream.readUTF();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return input;
    }

    public String register(JSONObject jsonObject){
            //connect to Server
            System.out.println("in runnable, AccountInfo JSON: " + jsonObject);
            try {
                outputStream.writeUTF("Register");
                outputStream.writeUTF(jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                input = inputStream.readUTF();
                System.out.println("reg_status: " + input);
            } catch (IOException e) {
                e.printStackTrace();
                input = "Register_Fail";
            }
            //System.out.println("end of run");
        return input;
    }

    private Runnable client_request = new Runnable() {
        @Override
        public void run() {
            socket = new Socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(address, ClientPort);
            try{
                socket.connect(inetSocketAddress, 20000);
                System.out.println("Socket success!");
            } catch(IOException e){
                System.out.println("Socket Fault! from client");
                System.out.println("IOException: " + e.toString());
            }
            //initialize input and output stream
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                System.out.println("client I/O Fault!");
                System.out.println("IOException: " + e.toString());
            }

            request = intent.getExtras().getInt("request");
            System.out.println("client request num: " + request);
            switch(request){
                case 0:
                    Account = intent.getExtras().getString("Account");
                    Password = intent.getExtras().getString("Password");
                    String login_status = login(Account, Password);
                    bundle = new Bundle();
                    bundle.putString("login_status", login_status);
                    intent.putExtras(bundle);
                    setResult(0, intent);
                    try {
                        inputStream.close();
                        outputStream.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                    break;
                case 1:
                    try {
                        json = new JSONObject(intent.getExtras().getString("AccountInfo"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String reg_status = register(json);
                    bundle = new Bundle();
                    System.out.println("reg_status before send: " + input);
                    bundle.putString("reg_status", input);
                    intent.putExtras(bundle);
                    setResult(1, intent);
                    try {
                        inputStream.close();
                        outputStream.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                    break;
                case 2:
                    System.out.println("load");
                    //list.clear();
                    try {
                        outputStream.writeUTF("Data");
                        try {
                            while(!((input = inputStream.readUTF()).equals("Data End"))){
                                //json = new JSONObject(input);
                                System.out.println("json receive: " + input);
                                list.add(input);
                            }
                        } catch (IOException e) {
                            System.out.println("Data fault!");
                            System.out.println("IOException: " + e.toString());
                        } /*catch (JSONException e) {
                            System.out.println("Data fault!");
                            System.out.println("JSONException: " + e.toString());
                        }*/
                    } catch (IOException e) {
                        System.out.println("Data fault!");
                        System.out.println("IOException: " + e.toString());
                    }
                    bundle = new Bundle();
                    bundle.putSerializable("list", list);
                    intent.putExtras(bundle);
                    setResult(2, intent);
                    try {
                        inputStream.close();
                        outputStream.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                    break;
                case 3:
                    try {
                        outputStream.writeUTF("New");
                        outputStream.writeUTF(intent.getExtras().getString("NewRequest"));
                        if((input = inputStream.readUTF()).equals("add success")){
                            System.out.println("Add Request Success!");
                        }
                        else{
                            System.out.println("Add Request Fault!");
                        }

                    } catch (IOException e) {
                        System.out.println("Add Request Fault!");
                        System.out.println("IOException: " + e.toString());
                    }

                    setResult(3, intent);

                    try {
                        inputStream.close();
                        outputStream.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    finish();
                    break;
            }
        }
    };

}
