package com.davidthigs.david.taskbreaker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskViewActivity extends AppCompatActivity {
    private AlertDialog createTaskDialog;
    private TaskViewListFragment taskViewListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadPreferences();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_view);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        taskViewListFragment = (TaskViewListFragment) getSupportFragmentManager().findFragmentById(R.id.task_children);
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
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMarginStart(convertDpToPx(16));
        params.setMarginEnd(convertDpToPx(16));
        final TextView titleLabel = new TextView(context);
        titleLabel.setText("Title:");
        titleLabel.setTextSize(26);
        titleLabel.setTextColor(Color.BLACK);
        titleLabel.setLayoutParams(params);
        layout.addView(titleLabel);
        final EditText titleBox = new EditText(context);
        titleBox.setText("");
        titleBox.setHint("Title");

        titleBox.setLayoutParams(params);
        titleBox.setSingleLine();
        layout.addView(titleBox);
        final TextView descriptionLabel = new TextView(context);
        descriptionLabel.setText("Description:");
        descriptionLabel.setTextSize(26);
        descriptionLabel.setTextColor(Color.BLACK);
        //descriptionLabel.setLayoutParams(params);
        layout.addView(descriptionLabel);
        final EditText descriptionBox = new EditText(context);
        descriptionBox.setText("");
        descriptionBox.setHint("Optional");
        descriptionBox.setMaxLines(2);
        descriptionBox.setLayoutParams(params);
        layout.addView(descriptionBox);

        dialog.setView(layout, 28, 16, 28, 16);

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createTaskDialog.cancel();
            }
        });
        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (titleBox.getText().toString().trim().length() == 0) {
                    titleBox.setError("Title is required!");
                } else if (descriptionBox.getText().toString().trim().length() == 0) {
                    Task newTask = new Task(titleBox.getText().toString());
                    Intent intent = getIntent();
                    ArrayList<Integer> taskPositions = (ArrayList<Integer>) intent.getExtras().getSerializable(MainActivity.TASK_POSITION_EXTRA);
                    TaskList taskList = (TaskList) getApplicationContext();

                    Task parentTask = taskList.getList().get(taskPositions.get(0));

                    for (int i = 1; i < taskPositions.size(); i++) {
                        parentTask = parentTask.getChildren().get(taskPositions.get(i));
                    }

                    parentTask.addChild(newTask);
                    taskList.saveDatabase();
                    taskViewListFragment.refreshList();

                } else {
                    Task newTask = new Task(titleBox.getText().toString(), descriptionBox.getText().toString());
                    Intent intent = getIntent();
                    ArrayList<Integer> taskPositions = (ArrayList<Integer>) intent.getExtras().getSerializable(MainActivity.TASK_POSITION_EXTRA);
                    TaskList taskList = (TaskList) getApplicationContext();
                    Task parentTask = taskList.getList().get(taskPositions.get(0));

                    for (int i = 1; i < taskPositions.size(); i++) {
                        parentTask = parentTask.getChildren().get(taskPositions.get(i));
                    }
                    parentTask.addChild(newTask);

                    taskList.saveDatabase();
                    taskViewListFragment.refreshList();
                }
            }
        });
        //createTaskDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        createTaskDialog = dialog.create();
    }

    public int convertDpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    private void loadPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDarkTheme = sharedPreferences.getBoolean(MainActivity.PREF_DARK_THEME, false);
        if (isDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
