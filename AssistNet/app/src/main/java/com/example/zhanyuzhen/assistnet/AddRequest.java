package com.example.zhanyuzhen.assistnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddRequest extends AppCompatActivity {

    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Socket socket;
    //ArrayList<Integer> rqueue = new ArrayList<Integer>();
    HashMap<String, String> base_sup;
    ArrayList<HashMap<String, String>> support;
    JSONObject jsonObject;
    String author;
    Intent intent;
    Date date;
    String input;
    Bundle bundle;
    int Request;

    //request
    int Login = 0;
    int Load = 1;
    int Register = 2;
    int add = 3;
    int edit = 4;
    int Bye = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        System.out.println("in Add request");

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();
        Request = intent.getExtras().getInt("request");
        if(Request == 4){
            populatefield();
        }
        bundle = intent.getExtras();
        author = bundle.getString("author");
        base_sup = new HashMap<String, String>();
        base_sup.put("num", "數量");
        base_sup.put("pic", "圖片");
        base_sup.put("name", "支援人");
        support = new ArrayList<HashMap<String, String>>();
        support.add(base_sup);
        //rqueue = (ArrayList<Integer>) bundle.getSerializable("rqueue");

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_request, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save) {

                Map map = new HashMap();
                TextView textView = (TextView) findViewById(R.id.request);
                map.put("Title", textView.getText().toString());
                textView = (TextView) findViewById(R.id.num);
                map.put("Number", textView.getText().toString());
                textView = (TextView) findViewById(R.id.note);
                map.put("note", textView.getText().toString());
                //map.put("author", author);
                date = new Date(System.currentTimeMillis());
                //map.put("date", date);
                jsonObject = new JSONObject(map);
                Intent intent2 = new Intent(this, bg_thread.class);
                Bundle bundle = new Bundle();

            if(Request == 3) {
                bundle.putInt("request", 3);
                try {
                    jsonObject.put("date", date);
                    jsonObject.put("author", author);
                    JSONArray array_tmp = new JSONArray(support);
                    jsonObject.put("support", array_tmp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("new request: " + jsonObject);
                bundle.putString("NewRequest", jsonObject.toString());
                intent2.putExtras(bundle);
                startActivityForResult(intent2, 3);
            }
            else{
                JSONObject tmp = null;
                try {
                    tmp = new JSONObject(intent.getExtras().getString("json"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.put("date", tmp.getString("date"));
                    jsonObject.put("author", tmp.getString("author"));
                    JSONArray array_tmp = new JSONArray(tmp.getString("support"));
                    jsonObject.put("support", array_tmp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("edit request: " + jsonObject);
                bundle.putInt("request", 4);
                bundle.putString("EditRequest", jsonObject.toString());
                intent2.putExtras(bundle);
                startActivityForResult(intent2, 4);
            }

            return true;
        }
        else if (id == R.id.exit){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3){
            System.out.println("Add new request success!");
            setResult(3, intent);
            finish();
        }
        else{
            System.out.println("Edit request success!");
            setResult(4, intent);
            finish();
        }
    }

    protected void onPause(){
        super.onPause();
    }

    public void populatefield(){
        JSONObject json = null;
        try {
            json = new JSONObject(intent.getExtras().getString("json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView request = (TextView)findViewById(R.id.request);
        try {
            request.setText(json.getString("Title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView num = (TextView)findViewById(R.id.num);
        try {
            num.setText(json.getString("Number"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView note = (TextView)findViewById(R.id.note);
        try {
            note.setText(json.getString("note"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
