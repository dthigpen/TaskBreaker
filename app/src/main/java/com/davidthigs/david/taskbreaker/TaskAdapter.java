package com.davidthigs.david.taskbreaker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by David on 5/30/2016.
 */
public class TaskAdapter extends ArrayAdapter<Task> {

    public static class ViewHolder{
        TextView name;
        TextView description;
    }
    public TaskAdapter(Context context, ArrayList<Task> tasks){
        super(context,0,tasks);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ViewHolder viewHolder;
        Task task = getItem(position);
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2,parent,false);

            viewHolder.name = (TextView)convertView.findViewById(android.R.id.text1);
            viewHolder.description = (TextView)convertView.findViewById(android.R.id.text2);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.name.setText(task.getName());
        viewHolder.description.setText(task.getDescription());

        return convertView;
    }


}
