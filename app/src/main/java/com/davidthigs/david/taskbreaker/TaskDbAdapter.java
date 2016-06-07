package com.davidthigs.david.taskbreaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by David on 5/31/2016.
 */
public class TaskDbAdapter {

    public static final String DATABASE_NAME = "tasks.db";
    public static final int DATABASE_VERSION =1;

    public static final String TASK_LIST_TABLE ="task_list";
    public static final String COLUMN_TASK_ID = "_id";
    public static final String COLUMN_TASK_NAME = "name";
    public static final String COLUMN_TASK_DESCRIPTION="description";
    public static final String COLUMN_TASK_CHILDREN = "children";
    private String[] allColumns = {COLUMN_TASK_ID,COLUMN_TASK_NAME,COLUMN_TASK_DESCRIPTION,COLUMN_TASK_CHILDREN};
    public static final String CREATE_TASK_LIST_TABLE ="create table "+TASK_LIST_TABLE+" ( "
            + COLUMN_TASK_ID + " integer primary key autoincrement, "
            + COLUMN_TASK_NAME + " text not null, "
            + COLUMN_TASK_DESCRIPTION + " text not null, "
            + COLUMN_TASK_CHILDREN
            + ");";


    private SQLiteDatabase sqlDB;
    private Context context;
    private TaskDbHelper taskDbHelper;

    public TaskDbAdapter(Context cxt){
        context = cxt;
    }
    public TaskDbAdapter open() throws android.database.SQLException
    {
        taskDbHelper = new TaskDbHelper(context);
        sqlDB = taskDbHelper.getWritableDatabase();
        return this;
    }
    public void close(){
        taskDbHelper.close();
    }
    public void saveTasks(ArrayList<Task> tasks){

        sqlDB.execSQL("delete from "+TASK_LIST_TABLE);

        for(int i = 0;i<tasks.size();i++){

            JSONArray jsonArrayChildren = new JSONArray();
            for(int j = 0;j<tasks.get(i).getChildren().size();j++){
                jsonArrayChildren.put(tasks.get(i).getChildren().get(j).getJSONObject());
            }


            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_TASK_NAME,tasks.get(i).getName());
            contentValues.put(COLUMN_TASK_DESCRIPTION,tasks.get(i).getDescription());
            contentValues.put(COLUMN_TASK_CHILDREN,jsonArrayChildren.toString());
            long replaceId = sqlDB.replace(TASK_LIST_TABLE,null,contentValues);

        }


        /*
        //convert to json string
        JSONArray jsonArray = new JSONArray();
        if(tasks.size()>0){
            for(int i = 0;i<tasks.size();i++){
                jsonArray.put(tasks.get(i).getJSONObject());
            }
        }
        contentValues.put(COLUMN_TASK_ARRAY_LIST,jsonArray.toString());

        long insertId = sqlDB.insert(TASK_LIST_TABLE,null,contentValues);

        //Cursor cursor = sqlDB.query(TASK_LIST_TABLE,allColumns,COLUMN_TASK_ID+" = "+insertId,null,null,null,null);
        //cursor.moveToFirst()
        */
    }

    public ArrayList<Task> getTasks() throws JSONException {
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor cursor = sqlDB.query(TASK_LIST_TABLE,allColumns,null,null,null,null,null);
        for(cursor.moveToLast();!cursor.isBeforeFirst();cursor.moveToPrevious()){
            tasks.add(cursorToTask(cursor));
        }
        Collections.reverse(tasks)  ;
        return tasks;
    }
    //TODO add update task function

    public Task JSONObjectToTask(JSONObject jsonObject) throws JSONException{
        String name = jsonObject.getString("name");
        String description = jsonObject.getString("description");
        ArrayList<Task> children = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("children");
        for(int i = 0;i < jsonArray.length();i++){
            children.add(JSONObjectToTask(jsonArray.getJSONObject(i)));
        }
        Task task = new Task(name,description,children);
        return task;
    }

    public Task cursorToTask(Cursor cursor) throws JSONException {

        String name = cursor.getString(1);
        String description  = cursor.getString(2);
        JSONArray jsonArray = new JSONArray(cursor.getString(3));
        ArrayList<Task> children = new ArrayList<>();
        for(int i = 0;i<jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Task childTask = JSONObjectToTask(jsonObject);
            children.add(childTask);
        }
        Task task = new Task(name,description,children);

        return task;
    }
    private static class TaskDbHelper extends SQLiteOpenHelper
    {
        TaskDbHelper(Context ctx){
            super(ctx,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(CREATE_TASK_LIST_TABLE);

        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TaskDbHelper.class.getName(),
                    "Upgrading database from version "+oldVersion +
                            " to " + newVersion + ", which will destroy all old data");

            db.execSQL("DROP TABLE IF EXISTS "+CREATE_TASK_LIST_TABLE);
            onCreate(db);
        }
    }
}
