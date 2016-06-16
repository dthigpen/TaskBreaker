package com.davidthigs.david.taskbreaker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
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

    private boolean hasCheckedPriority;
    private boolean isDarkTheme;
    private boolean showNumbers;
    public static class ViewHolder{
        TextView name;
        TextView description;
    }
    public TaskAdapter(Context context, ArrayList<Task> tasks){
        super(context,0,tasks);
        loadPreferences();
    }
    private void loadPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        hasCheckedPriority = sharedPreferences.getBoolean("checkPriority", false);
        isDarkTheme = sharedPreferences.getBoolean(MainActivity.PREF_DARK_THEME,false);


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

        if(task.getChecked()){
            TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
            TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);
            text1.setTextColor(Color.GRAY);
            text1.setPaintFlags((text1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG) | Paint.ANTI_ALIAS_FLAG);
            text2.setTextColor(Color.GRAY);

        }
        else{
            TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
            TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);
            text1.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            text1.setTextColor(isDarkTheme?Color.WHITE:Color.BLACK);
            text2.setTextColor(isDarkTheme?Color.WHITE:Color.BLACK);

        }
        if(showNumbers){
            viewHolder.name.setText(position+1+". "+ task.getName());
            viewHolder.description.setText(task.getDescription());
        }
        else{
            viewHolder.name.setText(task.getName());
            viewHolder.description.setText(task.getDescription());
        }



        return convertView;
    }


}
