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
        int id = -1;
        try {
            title = json.getString("Title");
            author = json.getString("author");
            id = json.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //set view
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resourceID, parentView, false);
        TextView Title = (TextView)rowView.findViewById(R.id.main_title);
        TextView Author = (TextView)rowView.findViewById(R.id.main_author);
        TextView Id = (TextView)rowView.findViewById(R.id.main_id);

        Title.setText(title);
        Author.setText(author);
        Id.setText(Integer.toString(id));

        return rowView;
    }
}
