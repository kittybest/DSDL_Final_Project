package com.example.zhanyuzhen.assistnet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
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
    private String address = "10.0.2.15";
    private int ClientPort = 8765;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Socket socket;
    private String input;
    private JSONObject json;
    ArrayList<JSONObject> list = new ArrayList<JSONObject>();
    ListView mainList;
    Thread thread;
    Context main = this;

    //protocols
    //none

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        (new Server()).start();
        thread = new Thread(client_request);
        thread.start();

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
        super.onDestroy();
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
                outputStream.writeUTF("Data");
                try {
                    while(!((input = inputStream.readUTF()).equals("Data End"))){
                        json = new JSONObject(input);
                        list.add(json);
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

            mainList = (ListView) findViewById(R.id.main_list);
            MyArrayAdapter adapter = new MyArrayAdapter(main, R.layout.main_list, list);
            mainList.setAdapter(adapter);

            
        }
    };
}
