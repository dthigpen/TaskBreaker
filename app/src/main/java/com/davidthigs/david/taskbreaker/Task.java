package com.davidthigs.david.taskbreaker;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by David on 5/29/2016.
 */
public class Task implements Serializable{
    private String name;
    private String description;
    private ArrayList<Task> children;

    public Task(String name){
        this.name = name;
        description = "";
        children = new ArrayList<>();
    }

    public Task(String name, String description){
        this.name = name;
        this.description = description;
        children = new ArrayList<>();
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public ArrayList<Task> getChildren(){
        return children;
    }
    public void setChildren(ArrayList<Task> children){
        this.children = children;
    }

    public void addChild(Task child){
        children.add(child);
    }

}
