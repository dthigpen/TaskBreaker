package com.davidthigs.david.taskbreaker;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskViewListFragment extends ListFragment {
    private TaskAdapter taskAdapter;
    private ArrayList<Integer> taskPositions;
    private TaskList taskList;
    private int rowPosition;
    private String taskName,taskDescription;
    private AlertDialog createTaskDialog;
    private Task parentTask;
    public TaskViewListFragment() {
        // Required empty public constructor
    }
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Intent intent = getActivity().getIntent();
        taskPositions = (ArrayList<Integer>)intent.getExtras().getSerializable(MainActivity.TASK_POSITION_EXTRA);
        Log.d("TASK FRAG","Position");
        for(int i = 0;i<taskPositions.size();i++){
            Log.d("TASK FRAG",String.valueOf(taskPositions.get(i)));
        }
        taskList = (TaskList)getActivity().getApplicationContext();
        //ArrayList<Task> allTasks = taskList.getList();
        Task task = taskList.getList().get(taskPositions.get(0));
        for(int i = 1;i<taskPositions.size();i++){
            task = task.getChildren().get(taskPositions.get(i));
            //Log.d("TASK FRAG",task.getName());
        }
        final ListView listView = getListView();
        final SwipeDetector swipeDetector = new SwipeDetector();
        listView.setOnTouchListener(swipeDetector);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView <? > parent, View view,
                                    int position, long id) {
                parentTask  = taskList.getList().get(taskPositions.get(0));

                for(int i = 1;i<taskPositions.size();i++){
                    parentTask = parentTask.getChildren().get(taskPositions.get(i));
                }
                parentTask = parentTask.getChildren().get(position);

                if (swipeDetector.swipeDetected()) {
                    if (swipeDetector.getAction() == SwipeDetector.Action.LR) {

                        Toast.makeText(getContext(),"Done!", Toast.LENGTH_SHORT).show();
                        parentTask.setChecked(true);
                        refreshList();
                    }
                    if (swipeDetector.getAction() == SwipeDetector.Action.RL) {

                        Toast.makeText(getContext(),"Not Done!", Toast.LENGTH_SHORT).show();
                        parentTask.setChecked(false);
                        refreshList();
                    }
                }
                else{
                    Intent intent = new Intent(getActivity(),TaskViewActivity.class);

                    ArrayList<Integer>newTaskPositions = new ArrayList(taskPositions);

                    newTaskPositions.add(position);
                    intent.putExtra(MainActivity.TASK_TITLE_EXTRA,parentTask.getName());
                    intent.putExtra(MainActivity.TASK_POSITION_EXTRA,newTaskPositions);
                    startActivity(intent);
                }

            }
        });
        registerForContextMenu(getListView());
        taskAdapter = new TaskAdapter(getActivity(),task.getChildren());
        setListAdapter(taskAdapter);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu,v,menuInfo);

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.long_press_menu,menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        rowPosition = info.position;
        //Task task = (Task)getListAdapter().getItem(rowPosition);
        switch (item.getItemId()){
            case R.id.diagram:
                Intent intent = new Intent(getActivity(),DiagramView.class);
                intent.putExtra(MainActivity.TASK_FOR_DIAGRAM_EXTRA,taskAdapter.getItem(rowPosition));
                startActivity(intent);
                return true;
            case R.id.edit:
                /*
                parentTask  = taskList.getList().get(taskPositions.get(0));
                for(int i = 1;i<taskPositions.size();i++){
                    parentTask = parentTask.getChildren().get(taskPositions.get(i));
                }
                parentTask = parentTask.getChildren().get(rowPosition);
                taskName = parentTask.getName();
                taskDescription = parentTask.getDescription();
                */
                taskName=taskAdapter.getItem(rowPosition).getName();
                taskDescription=taskAdapter.getItem(rowPosition).getDescription();
                addTaskDialog();
                createTaskDialog.show();
                return true;
            case R.id.delete:
                //TODO add confirmation dialog or snackbar undo

                taskAdapter.remove(taskAdapter.getItem(rowPosition));
                taskList.saveDatabase();
                refreshList();
                break;
        }
        return super.onContextItemSelected(item);
    }
    public void refreshList(){
        taskAdapter.notifyDataSetChanged();
    }

    public void addTaskDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        Context context = getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMarginStart(convertDpToPx(16));
        params.setMarginEnd(convertDpToPx(16));
        final TextView titleLabel = new TextView(context);
        titleLabel.setText("Title:");
        titleLabel.setTextSize(26);
        titleLabel.setTextColor(Color.BLACK);

        layout.addView(titleLabel);
        final EditText titleBox = new EditText(context);
        titleBox.setText(taskName);
        titleBox.setHint("Title");

        titleBox.setLayoutParams(params);
        titleBox.setSingleLine();
        layout.addView(titleBox);
        final TextView descriptionLabel = new TextView(context);
        descriptionLabel.setTextSize(26);
        descriptionLabel.setTextColor(Color.BLACK);

        layout.addView(descriptionLabel);
        final EditText descriptionBox = new EditText(context);
        descriptionBox.setText(taskDescription);
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
                else{

                    //edit task

                    //parentTask.setName(titleBox.getText().toString());
                    //parentTask.setDescription(descriptionBox.getText().toString());
                    taskAdapter.getItem(rowPosition).setName(titleBox.getText().toString());
                    taskAdapter.getItem(rowPosition).setDescription(descriptionBox.getText().toString());
                    taskList.saveDatabase();
                    refreshList();
                }
            }
        });
        createTaskDialog = dialog.create();
    }
    public int convertDpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
