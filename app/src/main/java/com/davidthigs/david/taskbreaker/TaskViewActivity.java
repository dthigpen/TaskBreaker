package com.davidthigs.david.taskbreaker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TaskViewActivity extends AppCompatActivity {
    private AlertDialog createTaskDialog;
    private TaskViewListFragment taskViewListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_view);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        taskViewListFragment = (TaskViewListFragment)getSupportFragmentManager().findFragmentById(R.id.task_children);
        Intent intent = getIntent();
        String title = intent.getStringExtra(MainActivity.TASK_TITLE_EXTRA);
        setTitle(title);
        addTaskDialog();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskDialog();
                createTaskDialog.show();
            }
        });

    }

    public void addTaskDialog() {


        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        Context context = this;
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        final TextView titleLabel = new TextView(context);
        titleLabel.setText("Title:");
        titleLabel.setTextSize(26);
        layout.addView(titleLabel);
        final EditText titleBox = new EditText(context);
        titleBox.setText("");
        titleBox.setHint("Title");
        layout.addView(titleBox);
        final TextView descriptionLabel = new TextView(context);
        descriptionLabel.setText("Description:");
        layout.addView(descriptionLabel);
        final EditText descriptionBox = new EditText(context);
        descriptionBox.setText("");
        descriptionBox.setTextSize(26);
        descriptionBox.setHint("Optional");
        layout.addView(descriptionBox);

        dialog.setView(layout);
        dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
           @Override
            public void onClick(DialogInterface dialog,int which){
                createTaskDialog.cancel();
           }
        });
        dialog.setPositiveButton("Confirm" , new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(titleBox.getText().toString().trim().length() == 0){
                    titleBox.setError("Title is required!");
                }
                else if(descriptionBox.getText().toString().trim().length() == 0){
                    Task newTask = new Task(titleBox.getText().toString());
                    Intent intent = getIntent();
                    ArrayList<Integer> taskPositions = (ArrayList<Integer>)intent.getExtras().getSerializable(MainActivity.TASK_POSITION_EXTRA);
                    TaskList taskList = (TaskList)getApplicationContext();

                    Task parentTask  = taskList.getList().get(taskPositions.get(0));

                    for(int i = 1;i<taskPositions.size();i++){
                        parentTask = parentTask.getChildren().get(taskPositions.get(i));
                    }

                        parentTask.addChild(newTask);
                    taskViewListFragment.refreshList();

                }
                else{
                    Task newTask = new Task(titleBox.getText().toString(),descriptionBox.getText().toString());
                    Intent intent = getIntent();
                    ArrayList<Integer> taskPositions = (ArrayList<Integer>)intent.getExtras().getSerializable(MainActivity.TASK_POSITION_EXTRA);
                    TaskList taskList = (TaskList)getApplicationContext();
                    Task parentTask  = taskList.getList().get(taskPositions.get(0));

                    for(int i = 1;i<taskPositions.size();i++){
                        parentTask = parentTask.getChildren().get(taskPositions.get(i));
                    }
                    parentTask.addChild(newTask);
                    taskViewListFragment.refreshList();
                }
            }
            });
        //createTaskDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        createTaskDialog = dialog.create();
    }

}
