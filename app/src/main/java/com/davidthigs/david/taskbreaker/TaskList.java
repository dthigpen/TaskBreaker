package com.davidthigs.david.taskbreaker;

import android.app.Application;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by David on 5/31/2016.
 */
public class TaskList extends Application {

    private static ArrayList<Task> list;
    @Override
    public void onCreate(){
        super.onCreate();
        list = new ArrayList<>();
        list.add(new Task("task1"));
        list.add(new Task("task2"));
        list.add(new Task("task3"));
        list.get(0).addChild(new Task("task1A"));
        list.get(0).addChild(new Task("task1B"));
        list.get(1).addChild(new Task("task2A"));
        list.get(0).getChildren().get(0).addChild(new Task("fdgdg"));
    }
    public void setList(ArrayList<Task> list){
        this.list = list;
    }
    public void addTask(Task task){
        list.add(task);
    }
    public void removeTask(int index){
        list.remove(index);
    }
    public boolean isEmpty(){
        return (list.size()>0)?true:false;
    }
    public ArrayList<Task> getList(){
        return list;
    }
}
