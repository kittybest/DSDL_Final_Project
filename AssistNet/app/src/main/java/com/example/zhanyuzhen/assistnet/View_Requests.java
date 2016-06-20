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
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class View_Requests extends AppCompatActivity {

    Intent intent;
    Bundle bundle;
    JSONObject jsonObject;

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
        populatefields();
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
        return true;
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
