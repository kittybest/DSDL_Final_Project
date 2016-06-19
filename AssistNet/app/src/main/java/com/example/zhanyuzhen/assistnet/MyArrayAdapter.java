package com.example.zhanyuzhen.assistnet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhanyuzhen on 2016/6/14.
 */
public class MyArrayAdapter extends ArrayAdapter<JSONObject> {

    private int resourceID;

    public MyArrayAdapter(Context context, int resource, List<JSONObject> list) {
        super(context, resource, list);
        this.resourceID = resource;
    }

    public View getView(int position, View convertView, ViewGroup parentView){
//        super.getView(position, convertView, parentView);

        //get data
        JSONObject json = getItem(position);
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

        Title.setText(title);
        Author.setText(author);
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        //String curDate = dateFormat.format(date);
        //Date.setText(curDate);
        Date.setText(date);

        return rowView;
    }
}
