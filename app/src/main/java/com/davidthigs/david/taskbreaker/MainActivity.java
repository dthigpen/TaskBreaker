package com.davidthigs.david.taskbreaker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TASK_OBJECT_EXTRA ="TASK_OBJECT_EXTRA";
    public static final String TASK_LIST_EXTRA ="TASK_LIST_EXTRA";
    public static final String TASK_BUNDLE_EXTRA ="TASK_BUNDLE_EXTRA";
    public static final String TASK_TITLE_EXTRA = "TASK_TITLE_EXTRA";
    public static final String TASK_POSITION_EXTRA ="TASK_POSITION_EXTRA";
    public static final int TASK_REQUEST_CODE = 1;
    private AlertDialog createTaskDialog;
    private MainActivityFragment mainActivityFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // TaskList taskList = (TaskList)getApplicationContext();



        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        mainActivityFragment = new MainActivityFragment();
        fragmentTransaction.add(R.id.task_list_container,mainActivityFragment,"CREATE TASK LIST");
        fragmentTransaction.commit();
        addTaskDialog();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskDialog();
                createTaskDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
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
        descriptionLabel.setTextSize(26);
        layout.addView(descriptionLabel);
        final EditText descriptionBox = new EditText(context);
        descriptionBox.setText("");
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

                    TaskList taskList = (TaskList)getApplicationContext();
                    taskList.addTask(newTask);
                    mainActivityFragment.refreshList();

                }
                else{
                    Task newTask = new Task(titleBox.getText().toString(),descriptionBox.getText().toString());

                    TaskList taskList = (TaskList)getApplicationContext();
                    taskList.addTask(newTask);
                    mainActivityFragment.refreshList();
                }
            }
        });
        //createTaskDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        createTaskDialog = dialog.create();
    }
}
