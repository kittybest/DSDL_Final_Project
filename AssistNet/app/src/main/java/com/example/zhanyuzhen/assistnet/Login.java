package com.example.zhanyuzhen.assistnet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Login extends AppCompatActivity {

    private static Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String input;
    private String Account,Password;
    private String str_AccountInfo = new String();
    private final int ClientPort = 8765;
    private final static String address = "10.5.2.56";
    private String reg_status = new String();
    Thread thread;
    JSONObject AccountInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create account
        Button create_account = (Button)findViewById(R.id.CREATE_ACCOUNT);
        create_account.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
                if (reg_status.equals("Register_Success")) {
                    login(Account);
                } else if (reg_status.equals("Register_SameAccount")) {
                    Toast.makeText(getApplicationContext(), "已有此帳號 請重新申請", Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(getApplicationContext(), "錯誤 請再試一次", Toast.LENGTH_SHORT);
                }
            }
        });

        Button log_in = (Button)findViewById(R.id.LOG_IN);
        log_in.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_LONG);
                EditText ACCOUNT = (EditText)findViewById(R.id.ACCOUNT_NAME);
                EditText PASSWORD = (EditText)findViewById(R.id.PASSWORD);
                Account = new String();
                Account = ACCOUNT.getText().toString();
                Password = new String();
                Password = PASSWORD.getText().toString();

                thread =new Thread(Login);
                thread.start();
            }
        });

    }

    private void register(){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View Reg_Dialog = inflater.inflate(R.layout.activity_log_in_register,null);
        new AlertDialog.Builder(this)
                .setTitle("新增事件")
                .setView(Reg_Dialog)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText account = (EditText) (Reg_Dialog.findViewById(R.id.ACCOUNT_ID));
                        EditText name =  (EditText) (Reg_Dialog.findViewById(R.id.NAME));
                        EditText password =  (EditText) (Reg_Dialog.findViewById(R.id.PassWord));
                        EditText phone_number =  (EditText) (Reg_Dialog.findViewById(R.id.PHONE_NUMBER));
                        String Name,Phone;
                        Account = account.getText().toString();
                        Name = name.getText().toString();
                        Password = password.getText().toString();
                        Phone = phone_number.getText().toString();
                        AccountInfo = new JSONObject();
                        try {
                            AccountInfo.put("account",Account);
                            AccountInfo.put("name",Name);
                            AccountInfo.put("password",Password);
                            AccountInfo.put("phone",Phone);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("AccountInfo JSON: " + AccountInfo);
                        str_AccountInfo = new String();
                        str_AccountInfo = AccountInfo.toString();
                        System.out.println("AccountInfo String: " + str_AccountInfo);
                        //Toast.makeText(Login.this,"註冊成功",Toast.LENGTH_LONG).show();
                    }
                })
                .show();
        thread = new Thread(Register);
        thread.start();
    }
    private void login(String UserID){
        Intent intent = new Intent();
        intent.setClass(Login.this,MainActivity.class);
        Bundle pass_UserID = new Bundle();
        pass_UserID.putString("UserID",UserID);
        intent.putExtras(pass_UserID);
        startActivity(intent);
        Login.this.finish();
    }
    private Runnable Register = new Runnable() {
        @Override
        public void run() {
            //connect to Server
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
            try {
                outputStream.writeUTF("Register");
                System.out.println("Before send, AccountInfo String: " + str_AccountInfo);
                outputStream.writeUTF(str_AccountInfo);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                reg_status = inputStream.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
                reg_status = new String();
                reg_status = "Register_Fail";
            }
        }
    };
    private Runnable Login = new Runnable() {
        @Override
        public void run() {

            //connect to Server
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
            //request data list
            try {
                outputStream.writeUTF("LogIn");
                outputStream.writeUTF(Account);
                outputStream.writeUTF(Password);
                input = inputStream.readUTF();
                if(input.equals("LogIn_Success")){
                    login(Account);
                }
                else if(input.equals("LogIn_WrongPassword")){
                    Toast.makeText(getApplicationContext(),"密碼錯誤",Toast.LENGTH_SHORT);
                }
                else if(input.equals("Login_NoAccount")){
                    Toast.makeText(getApplicationContext(),"無此帳號",Toast.LENGTH_SHORT);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

}
