package com.davidthigs.david.taskbreaker;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONException;

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
        TaskDbAdapter taskDbAdapter = new TaskDbAdapter(getApplicationContext());
        taskDbAdapter.open();
        try{
            list = taskDbAdapter.getTasks();
        }catch (JSONException e){
            e.printStackTrace();
        }
        taskDbAdapter.close();
    }

    public void saveDatabase(){
        TaskDbAdapter taskDbAdapter = new TaskDbAdapter(getApplicationContext());
        taskDbAdapter.open();
        taskDbAdapter.saveTasks(list);
        taskDbAdapter.close();
    }
    public void setList(ArrayList<Task> list){
        this.list = list;
    }
    public void addTask(Task task){
        list.add(task);
        saveDatabase();
    }
    public void removeTask(int index){
        list.remove(index);
        saveDatabase();
    }
    public boolean isEmpty(){
        return (list.size()>0)?true:false;
    }
    public ArrayList<Task> getList(){
        return list;
    }
}
