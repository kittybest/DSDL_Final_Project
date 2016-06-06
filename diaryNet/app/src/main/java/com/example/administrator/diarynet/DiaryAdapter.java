package com.example.administrator.diarynet;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class DiaryAdapter extends BaseAdapter {
    private List<DIARY> Diarys;
    private LayoutInflater mylayoutinflater;
    public DiaryAdapter(Context context,List<DIARY> diary){
        mylayoutinflater=LayoutInflater.from(context);
        this.Diarys=diary;
    }
    public class ViewHolder{
        TextView txtTitle;
        TextView txtAuthor;
        TextView txtLikes;
        ImageView imgPhoto;
        public ViewHolder(TextView t,TextView a,TextView l,ImageView p){
            this.txtTitle=t;
            this.txtAuthor=a;
            this.txtLikes=l;
            this.imgPhoto=p;
        }
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView=mylayoutinflater.inflate(R.layout.single_diary_layout,null);
            holder = new ViewHolder((TextView)convertView.findViewById(R.id.title)
                    ,(TextView)convertView.findViewById(R.id.author)
                    ,(TextView)convertView.findViewById(R.id.likes)
                    ,(ImageView) convertView.findViewById(R.id.photo));
            convertView.setTag(holder);
        }
        else{holder = (ViewHolder) convertView.getTag();}
        DIARY diary = (DIARY)getItem(position);
        holder.txtTitle.setText(diary.getTITLE());
        holder.txtAuthor.setText(diary.getAUTHOR());
        String like_temp= Integer.toString(diary.getLIKES());
        holder.txtLikes.setText(like_temp);
        holder.imgPhoto.setImageResource(diary.getImageID());

        return convertView;
    }
    @Override
    public int getCount() {return Diarys.size();}
    @Override
    public Object getItem(int position) {return Diarys.get(position);}
    @Override
    public long getItemId(int position) {return Diarys.indexOf(getItem(position));}
}
