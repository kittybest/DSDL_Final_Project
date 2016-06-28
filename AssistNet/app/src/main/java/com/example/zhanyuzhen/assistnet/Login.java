package com.example.zhanyuzhen.assistnet;

import android.app.AlertDialog;
import android.content.Context;
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
import java.util.ArrayList;

public class Login extends AppCompatActivity {

    private static Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String input;
    private String Account,Password;
    private String str_AccountInfo = new String();
    private final int ClientPort = 8765;
    private final static String address = "10.5.2.56";
    private String reg_status;
    private String login_status;
    Thread thread;
    private JSONObject AccountInfo = new JSONObject();
    Intent intent = new Intent();
    Context context;
    ArrayList<String> mails = new ArrayList<String>();


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
            }
        });

        context = this;

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

                intent = new Intent(context, bg_thread.class);
                Bundle bundle = new Bundle();
                bundle.putInt("request", 0);
                bundle.putString("Account", Account);
                bundle.putString("Password", Password);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode){
            case 0://login
                login_status = data.getExtras().getString("login_status");
                if(login_status.equals("LogIn_Success")){
                    mails = (ArrayList<String>) data.getExtras().getSerializable("mails");
                    login(Account);
                }
                else if(login_status.equals("LogIn_WrongPassWord")){
                    Toast.makeText(getApplicationContext(),"密碼錯誤",Toast.LENGTH_SHORT).show();
                }
                else if(login_status.equals("LogIn_NoAccount")){
                    Toast.makeText(getApplicationContext(),"無此帳號",Toast.LENGTH_SHORT).show();
                }
                break;
            case 1://register
                reg_status = data.getExtras().getString("reg_status");
                System.out.println("req_status : " + reg_status);
                if (reg_status.equals("Register_Success")) {
                    login(Account);
                } else if (reg_status.equals("Register_SameAccount")) {
                    Toast.makeText(getApplicationContext(), "已有此帳號 請重新申請", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "錯誤 請再試一次", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
                        intent = new Intent(context, bg_thread.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("request", 1);
                        bundle.putString("AccountInfo", AccountInfo.toString());
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 1);
                    }
                })
                .show();

    }
    private void login(String UserID){
        Intent intent = new Intent();
        intent.setClass(Login.this,MainActivity.class);
        Bundle pass_UserID = new Bundle();
        pass_UserID.putString("UserID",UserID);
        pass_UserID.putSerializable("mails", mails);
        intent.putExtras(pass_UserID);
        startActivity(intent);
        Login.this.finish();
    }


}
