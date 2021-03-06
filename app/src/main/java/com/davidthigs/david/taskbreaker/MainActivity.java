package com.davidthigs.david.taskbreaker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    public static final String TASK_TITLE_EXTRA = "TASK_TITLE_EXTRA";
    public static final String TASK_POSITION_EXTRA ="TASK_POSITION_EXTRA";
    public static final String TASK_PARENT_CHECKED_EXTRA ="TASK_PARENT_CHECKED_EXTRA";
    public static final String TASK_FOR_DIAGRAM_EXTRA ="TASK_FOR_DIAGRAM_EXTRA";
    public static final String PREF_DARK_THEME ="background_color";
    private AlertDialog createTaskDialog;
    private MainActivityFragment mainActivityFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadPreferences();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
    private boolean             _doubleBackToExitPressedOnce    = false;

    @Override
    public void onBackPressed() {

        //Log.i(TAG, "onBackPressed--");
        if (_doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this._doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to quit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                _doubleBackToExitPressedOnce = false;
            }
        }, 2000);
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
            Intent intent = new Intent(this, com.davidthigs.david.taskbreaker.AppPreferences.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
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
        //titleLabel.setLayoutParams(params);
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

        dialog.setView(layout,28,16,28,16);

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
    public int convertDpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
    private void loadPreferences() {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDarkTheme = sharedPreferences.getBoolean(PREF_DARK_THEME,false);
        if(isDarkTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }
        else{
            setTheme(R.style.AppTheme_NoActionBar);
        }
    }

}
