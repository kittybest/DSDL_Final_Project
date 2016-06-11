package com.example.administrator.diarynet;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONException;

public class LogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Button create_account = (Button)findViewById(R.id.CREATE_ACCOUNT);
        create_account.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        Button log_in = (Button)findViewById(R.id.LOG_IN);
        log_in.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ACCOUNT = (EditText)findViewById(R.id.ACCOUNT_NAME);
                EditText PASSWORD = (EditText)findViewById(R.id.PASSWORD);
                String account = ACCOUNT.getText().toString();
                String password = PASSWORD.getText().toString();
                login(account);
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
                        String Account,Name,Password,Phone;
                        Account = account.getText().toString();
                        Name = name.getText().toString();
                        Password = password.getText().toString();
                        Phone = phone_number.getText().toString();
                        JSONObject AccountInfo = new JSONObject();
                        try {
                            AccountInfo.put("account",Account);
                            AccountInfo.put("name",Name);
                            AccountInfo.put("password",Password);
                            AccountInfo.put("phone",Phone);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(LogIn.this,"註冊成功",Toast.LENGTH_LONG).show();
                        login(Account);
                    }
                })
                .show();
    }
    private void login(String UserID){
        Intent intent = new Intent();
        intent.setClass(LogIn.this,ListOfDiary.class);
        Bundle pass_UserID = new Bundle();
        pass_UserID.putString("UserID",UserID);
        intent.putExtras(pass_UserID);
        startActivity(intent);
        LogIn.this.finish();
    }
}
