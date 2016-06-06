package com.example.administrator.diarynet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListOfDiary extends AppCompatActivity {

    private ListView DiaryList;
    List<DIARY> diarys;
    public static final String[] sample_title = new String[]{"標題一","標題二","標題三"};
    public static final String[] sample_author = new String[]{"佐助","鳴人","小櫻"};
    public static final Integer[] sample_likes = new Integer[]{1000,100,10};
    public static final Integer[] sample_photo ={R.drawable.sasuke,R.drawable.naruto,R.drawable.sakura};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_diary);

        diarys  = new ArrayList<DIARY>();
        for(int i=0;i<sample_title.length;i++){
            DIARY temp = new DIARY(sample_title[i],sample_author[i],sample_likes[i],sample_photo[i]);
            diarys.add(temp);
        }
        DiaryList=(ListView)findViewById(R.id.diarylist);
        DiaryAdapter adapter = new DiaryAdapter(this,diarys);
        DiaryList.setAdapter(adapter);
    }
}
