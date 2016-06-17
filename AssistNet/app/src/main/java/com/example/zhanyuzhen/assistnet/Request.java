package com.example.zhanyuzhen.assistnet;

/**
 * Created by zhanyuzhen on 2016/6/14.
 */
public class Request {
    private String Title;
    private String Content;
    private String author;
    private int id;

    public Request(String Title, String Content, String author, int id){
        this.Title = Title;
        this.Content = Content;
        this.author = author;
        this.id = id;
    }
    public String getTitle(){ return this.Title; }
    public String getContent(){ return this.Content; }
    public String getAuthor(){ return this.author; }
    public int getId(){ return this.id; }

    public void setTitle(String Title){ this.Title = Title; }
    public void setContent(String Content){ this.Content = Content; }
    public void setAuthor(String author){ this.author = author; }
    public void setId(int id){ this.id = id; }

}
