package com.example.zhanyuzhen.assistnet;

import java.util.ArrayList;

/**
 * Created by zhanyuzhen on 2016/6/19.
 */
public class RequestQueue {
    private static ArrayList<Integer> rqueue = new ArrayList<>();

    public static ArrayList<Integer> getRqueue(){
        return rqueue;
    }
    public static void setRqueue(ArrayList<Integer> queue){
        rqueue = queue;
    }
    public static void addRequest(int request){
        rqueue.add(request);
    }
    public static void rmRequest(int id){
        rqueue.remove(id);
    }
    public static Integer getRequest(int id){
        return rqueue.get(id);
    }
    public static boolean isEmpty(){
        return rqueue.isEmpty();
    }
}
