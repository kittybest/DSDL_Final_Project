package com.example.zhanyuzhen.assistnet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //parameters
    private String address = "10.5.2.56";
    private int ClientPort = 8765;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Socket socket;
    private String input;
    private JSONObject json;
    MyArrayAdapter adapter;
    ArrayList<JSONObject> list = new ArrayList<JSONObject>();
    ArrayList<String> str_list = new ArrayList<String>();
    //ArrayList<Integer> rqueue = new ArrayList<Integer>();
    ListView mainList;
    Thread thread;
    Context main = this;
    String name = "Doris";//到時候取login的值
    Intent intent;

    //requests
    int Login = 0;
    int Register = 1;
    int Load = 2;
    int add = 3;
    int edit =4;
    int del = 5;
    int sup = 6;
    int Bye = 10;

    //protocol
    //none

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        name = intent.getExtras().getString("UserID");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                addRequest();
            }
        });
        loadRequest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy(){
        RequestQueue.addRequest(Bye);
        super.onDestroy();
    }

    @Override
    protected void onRestart(){
        loadRequest();
        super.onRestart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            switch (requestCode) {
                case 2:
                    System.out.println("return to ui thread");
                    str_list = (ArrayList<String>) data.getExtras().getSerializable("list");
                    if(str_list.isEmpty()) System.out.println("OMG!!!!Empty!!!!");
                    list.clear();
                    for(int i = 0; i < str_list.size(); i ++){
                        try {
                            json = new JSONObject(str_list.get(i));
                            list.add(json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mainList = (ListView) findViewById(R.id.main_list);
                    MyArrayAdapter adapter = new MyArrayAdapter(main, R.layout.main_list, list);
                    mainList.setAdapter(adapter);
                    //adapter.notifyDataSetChanged();
                    /*mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            System.out.println("item clicked!");
                            //parent.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                            JSONObject json_tmp = (JSONObject) parent.getItemAtPosition(position);
                            intent = new Intent(main, View_Requests.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("request", 6);
                            bundle.putString("json", json_tmp.toString());
                            bundle.putString("name", name);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, 6);
                        }
                    });*/
                    break;
                case 5:
                    loadRequest();
                    break;

            }
        }
    }


    public void addRequest(){
        intent = new Intent(this, AddRequest.class);
        Bundle bundle = new Bundle();
        bundle.putInt("request", 3);
        bundle.putString("author", name);
        intent.putExtras(bundle);
        startActivityForResult(intent, add);
    }

    public void loadRequest(){
        intent = new Intent(this, bg_thread.class);
        Bundle bundle = new Bundle();
        bundle.putInt("request", Load);
        intent.putExtras(bundle);
        startActivityForResult(intent, Load);
    }


    //my array adapter
    public class MyArrayAdapter extends ArrayAdapter<JSONObject> {

        private int resourceID;

        public MyArrayAdapter(Context context, int resource, List<JSONObject> list) {
            super(context, resource, list);
            this.resourceID = resource;

        }

        public View getView(int position, View convertView, ViewGroup parentView){
//        super.getView(position, convertView, parentView);

            //get data
            final JSONObject json = getItem(position);
            String title = null;
            String author = null;
            //Date date = null;
            String date = null;
            try {
                title = json.getString("Title");
                author = json.getString("author");
                //date = (Date) json.get("date");//change to date
                date = json.getString("date");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //set view
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(resourceID, parentView, false);
            TextView Title = (TextView)rowView.findViewById(R.id.main_title);
            TextView Author = (TextView)rowView.findViewById(R.id.main_author);
            TextView Date = (TextView)rowView.findViewById(R.id.main_id);

            parentView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

            Title.setText(title);
            Author.setText(author);
            //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            //String curDate = dateFormat.format(date);
            //Date.setText(curDate);
            Date.setText(date);

            Button view = (Button)rowView.findViewById(R.id.view);
            Button edit = (Button)rowView.findViewById(R.id.edit);
            Button del = (Button)rowView.findViewById(R.id.delete);

            final String finalAuthor = author;

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    System.out.println("item clicked!");
                    //parent.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    //JSONObject json_tmp = (JSONObject) parent.getItemAtPosition(position);
                    intent = new Intent(main, View_Requests.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("request", 6);
                    bundle.putString("json", json.toString());
                    bundle.putString("name", name);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 6);
                }
            });

            edit.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(name.equals(finalAuthor)) {
                        Intent intent = new Intent(v.getContext(), AddRequest.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("request", 4);
                        bundle.putString("json", json.toString());
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 4);
                    }
                }
            });

            del.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(name.equals(finalAuthor)){
                        Intent intent = new Intent(v.getContext(), bg_thread.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("request", 5);
                        bundle.putString("json", json.toString());
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 5);
                    }
                }
            });

            return rowView;
        }
    }

}
