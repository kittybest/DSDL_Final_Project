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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

public class View_Requests extends AppCompatActivity {

    Intent intent;
    Bundle bundle;
    JSONObject jsonObject;
    String name;
    Context context;
    String Num;
    String Pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__requests);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();
        bundle = intent.getExtras();
        try {
            jsonObject = new JSONObject(bundle.getString("json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        name = bundle.getString("name");
        context = this;
        populatefields();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_support, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.support){
            LayoutInflater inflater = LayoutInflater.from(this);
            final View Sup_Dialog = inflater.inflate(R.layout.activity_add_support,null);
            new AlertDialog.Builder(this)
                    .setTitle("我要支援")
                    .setView(Sup_Dialog)
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText num = (EditText)Sup_Dialog.findViewById(R.id.sup_num);
                            EditText pic = (EditText)Sup_Dialog.findViewById(R.id.sup_pic);
                            //HashMap<String, String> support = new HashMap<String, String>();
                            Num = num.getText().toString();
                            Pic = pic.getText().toString();
                            Bundle tmp = new Bundle();
                            tmp.putString("num", Num);
                            tmp.putString("pic", Pic);
                            System.out.println("num / pic: " + Num + Pic);
                            tmp.putString("name", name);
                            tmp.putString("json", jsonObject.toString());
                            tmp.putInt("request", 6);
                            intent = new Intent(context, bg_thread.class);
                            intent.putExtras(tmp);
                            startActivityForResult(intent, 6);
                        }
                    })
                    .show();
        }
        else if(id == R.id.view_exit){
            finish();
        }

        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 6) {
            System.out.println("Add new support success!");
            setResult(6, intent);
            finish();
        }
    }

    public void populatefields(){
        TextView title = (TextView)findViewById(R.id.view_title);
        TextView num = (TextView)findViewById(R.id.view_num);
        TextView content = (TextView)findViewById(R.id.view_content);
        TextView author = (TextView)findViewById(R.id.view_author);
        TextView date = (TextView)findViewById(R.id.view_date);
        ListView supporters = (ListView)findViewById(R.id.view_supporters);
        try {
            title.setText(jsonObject.getString("Title"));
            num.setText(jsonObject.getString("Number"));
            content.setText(jsonObject.getString("note"));
            author.setText(jsonObject.getString("author"));
            date.setText(jsonObject.getString("date"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
