package com.example.administrator.diarynet;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class DiaryReading extends AppCompatActivity {

    private String UserID = new String();
    private String requestUser = new String();
    private String requestItem = new String();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_reading);

        Bundle bundle = this.getIntent().getExtras();
        UserID = bundle.getString("UserID");
        requestUser = bundle.getString("requestUser");
        requestItem = bundle.getString("requestItem");
        TextView Title = (TextView)findViewById(R.id.title);
        TextView Author = (TextView)findViewById(R.id.author);
        Title.setText("物品 : "+requestItem);
        Author.setText("使用者 : "+requestUser);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent back =new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("UserID",UserID);
            back.putExtras(bundle);
            back.setClass(DiaryReading.this,ListOfDiary.class);
            startActivity(back);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_register:
                reg();
                return true;
            /*case R.id.action_search:
                Search_Title();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void reg(){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View Add_Dialog = inflater.inflate(R.layout.action_help,null);
        new AlertDialog.Builder(this)
                .setTitle("願意支援")
                .setView(Add_Dialog)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText support = (EditText) (Add_Dialog.findViewById(R.id.SUPPORT));
                        String sup = support.getText().toString();
                        JSONObject SupportNumberInfo = new JSONObject();
                        try {
                            SupportNumberInfo.put("Supporter",UserID);
                            SupportNumberInfo.put("count",sup);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            Toast.makeText(getApplicationContext(),SupportNumberInfo.getString("Supporter")+" 願意支援 "+SupportNumberInfo.getString("count")+"個",Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .show();
    }
}
