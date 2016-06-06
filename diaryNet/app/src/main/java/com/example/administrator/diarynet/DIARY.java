package com.example.administrator.diarynet;

import android.media.Image;


public class DIARY {
    private String TITLE;
    private String AUTHOR;
    private int LIKES;
    private int ImageID;
    public DIARY(String t, String a,int l,int i){
        this.LIKES=l;
        this.TITLE=t;
        this.AUTHOR=a;
        this.ImageID=i;
    }
    public String getTITLE(){return TITLE;}
    public String getAUTHOR(){return AUTHOR;}
    public int getLIKES(){return LIKES;}
    public int getImageID(){return ImageID;}
    public void setTITLE(String t){this.TITLE=t;return;}
    public void setAUTHOR(String s){this.AUTHOR=s;return;}
    public void setLIKES(int l){this.LIKES=l;return;}
    public  void setImageID(int i){this.ImageID=i;return;}
}
