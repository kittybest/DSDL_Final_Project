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
    JSONObject jsonObject;
    String author;
    Intent intent;
    Date date;
    String input;
    Bundle bundle;

    //request
    int Login = 0;
    int Load = 1;
    int Register = 2;
    int add = 3;
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
        bundle = intent.getExtras();
        author = bundle.getString("author");
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
            TextView textView = (TextView)findViewById(R.id.request);
            map.put("Title", textView.getText().toString());
            textView = (TextView)findViewById(R.id.num);
            map.put("Number", textView.getText().toString());
            textView = (TextView)findViewById(R.id.note);
            map.put("note", textView.getText().toString());
            map.put("author", author);
            date = new Date(System.currentTimeMillis());
            map.put("date", date);
            jsonObject = new JSONObject(map);
            System.out.println("new request: " + jsonObject);

            Intent intent2 = new Intent(this, bg_thread.class);
            Bundle bundle = new Bundle();
            bundle.putInt("request", 3);
            bundle.putString("NewRequest", jsonObject.toString());
            intent2.putExtras(bundle);
            startActivityForResult(intent2, 3);

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
    }

    protected void onPause(){
        super.onPause();
    }
}
