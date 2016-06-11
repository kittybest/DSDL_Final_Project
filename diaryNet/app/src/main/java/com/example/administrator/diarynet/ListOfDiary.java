package com.example.administrator.diarynet;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListOfDiary extends AppCompatActivity implements OnItemClickListener {

    private ListView DiaryList;
    List<DIARY> diarys;
    public static final String[] sample_title = new String[]{"標題一","標題二","標題三"};
    public static final String[] sample_author = new String[]{"佐助","鳴人","小櫻"};
    public static final Integer[] sample_likes = new Integer[]{1000,100,10};
    public static final Integer[] sample_photo ={R.drawable.sasuke,R.drawable.naruto,R.drawable.sakura};

    private  String UserID = new String();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_diary);


        Bundle bundle = this.getIntent().getExtras();
        UserID = bundle.getString("UserID");
        diarys  = new ArrayList<DIARY>();
        for(int i=0;i<sample_title.length;i++){
            DIARY temp = new DIARY(sample_title[i],sample_author[i],sample_likes[i],sample_photo[i]);
            diarys.add(temp);
        }
        DiaryList=(ListView)findViewById(R.id.diarylist);
        DiaryAdapter adapter = new DiaryAdapter(this,diarys);
        DiaryList.setAdapter(adapter);
        DiaryList.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(ListOfDiary.this,DiaryReading.class);
        Bundle pass_info = new Bundle();
        pass_info.putString("UserID",UserID);
        pass_info.putString("requestUser",diarys.get(position).getAUTHOR());
        pass_info.putString("requestItem",diarys.get(position).getTITLE());
        intent.putExtras(pass_info);
        startActivity(intent);
        ListOfDiary.this.finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_v2,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Add();
                return true;
            /*case R.id.action_search:
                Search_Title();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void Add(){
        //Toast.makeText(getApplicationContext(),"HELLO",Toast.LENGTH_SHORT).show();
        LayoutInflater inflater = LayoutInflater.from(this);
        final View Add_Dialog = inflater.inflate(R.layout.action_add,null);
        new AlertDialog.Builder(this)
                .setTitle("新增事件")
                .setView(Add_Dialog)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText item_name = (EditText) (Add_Dialog.findViewById(R.id.ITEM));
                        EditText item_count =  (EditText) (Add_Dialog.findViewById(R.id.COUNT));
                        EditText trade_detail =  (EditText) (Add_Dialog.findViewById(R.id.DETAIL));
                        String ITEM,COUNT,LOCATION;
                        ITEM = item_name.getText().toString();
                        COUNT = item_count.getText().toString();
                        LOCATION = trade_detail.getText().toString();
                        JSONObject AddMissionInfo = new JSONObject();
                        try {
                            AddMissionInfo.put("item",ITEM);
                            AddMissionInfo.put("count",COUNT);
                            AddMissionInfo.put("location",LOCATION);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .show();
    }
}
