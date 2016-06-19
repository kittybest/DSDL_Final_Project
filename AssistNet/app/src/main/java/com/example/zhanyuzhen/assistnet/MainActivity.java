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
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
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
    int Bye = 10;

    //protocol
    //none

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();

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

        //RequestQueue.addRequest(Load);
        loadRequest();
        //thread = new Thread(client_request);
        //thread.start();

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
    protected void onResume(){
        intent = getIntent();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case 2:
                list = (ArrayList<JSONObject>)data.getExtras().getSerializable("list");
                mainList = (ListView) findViewById(R.id.main_list);
                MyArrayAdapter adapter = new MyArrayAdapter(main, R.layout.main_list, list);
                mainList.setAdapter(adapter);
                break;
            case 3:
                try {
                    json = new JSONObject(data.getExtras().getString("json"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestQueue.addRequest(add);
                RequestQueue.addRequest(Load);
                break;
        }
    }

    private Runnable client_request = new Runnable() {
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
            SocketHandler.setSocket(socket);
            //initialize input and output stream
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                System.out.println("client I/O Fault!");
                System.out.println("IOException: " + e.toString());
            }

            //request data list
            while(RequestQueue.isEmpty() || (!RequestQueue.isEmpty() && RequestQueue.getRequest(0) != Bye)) {
                if(!RequestQueue.isEmpty() && RequestQueue.getRequest(0) == Login){

                }
                else if(!RequestQueue.isEmpty() && RequestQueue.getRequest(0) == Load){
                    System.out.println("load");
                    list.clear();
                    try {
                        outputStream.writeUTF("Data");
                        try {
                            while(!((input = inputStream.readUTF()).equals("Data End"))){
                                json = new JSONObject(input);
                                list.add(json);
                                //Message msg = handler.obtainMessage();
                                //msg.what = 2;
                                //msg.sendToTarget();
                            }
                        } catch (IOException e) {
                            System.out.println("Data fault!");
                            System.out.println("IOException: " + e.toString());
                        } catch (JSONException e) {
                            System.out.println("Data fault!");
                            System.out.println("JSONException: " + e.toString());
                        }
                    } catch (IOException e) {
                        System.out.println("Data fault!");
                        System.out.println("IOException: " + e.toString());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainList = (ListView) findViewById(R.id.main_list);
                            MyArrayAdapter adapter = new MyArrayAdapter(main, R.layout.main_list, list);
                            mainList.setAdapter(adapter);
                        }
                    });
                    RequestQueue.rmRequest(0);
                }
                else if(!RequestQueue.isEmpty() && RequestQueue.getRequest(0) == add){
                    try {
                        outputStream.writeUTF("New");
                        outputStream.writeUTF(json.toString());
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
                    RequestQueue.rmRequest(0);
                }

            }
            //Bye
            try {
                outputStream.writeUTF("Bye");
                outputStream.close();
                outputStream = null;
                inputStream.close();
                inputStream = null;
                socket.close();
            } catch (IOException e) {
                System.out.println("Close fault!");
                System.out.println("IOException: " + e.toString());
            }
        }
    };


    public void addRequest(){
        intent = new Intent(this, AddRequest.class);
        Bundle bundle = new Bundle();
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
}
