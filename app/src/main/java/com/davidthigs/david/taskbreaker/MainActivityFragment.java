package com.davidthigs.david.taskbreaker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
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
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends ListFragment{
    private TaskAdapter taskAdapter;
    private TaskList taskList;
    private AlertDialog createTaskDialog;
    private  ArrayList<Integer> taskPositions;
    private String taskName,taskDescription;
    private int rowPosition;
    private boolean hasCheckedPriority;
    public MainActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle bundle){

        super.onActivityCreated(bundle);

        taskList = (TaskList)getActivity().getApplicationContext();

        taskAdapter = new TaskAdapter(getActivity(), taskList.getList());
        taskPositions= new ArrayList<>();
        setListAdapter(taskAdapter);


        final ListView listView = getListView();
        final SwipeDetector swipeDetector = new SwipeDetector();
        listView.setOnTouchListener(swipeDetector);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView <? > parent, View view,
                                    int position, long id) {

                if (swipeDetector.swipeDetected()) {
                    if (swipeDetector.getAction() == SwipeDetector.Action.LR) {

                        if(!taskAdapter.getItem(position).getChecked()) {
                            Toast.makeText(getContext(), "Done!", Toast.LENGTH_SHORT).show();
                            taskList.getList().get(position).setChecked(true);
                            taskList.saveDatabase();
                            refreshList();
                        }
                    }
                    if (swipeDetector.getAction() == SwipeDetector.Action.RL) {
                        if(taskAdapter.getItem(position).getChecked()) {
                            Toast.makeText(getContext(), "Not Done!", Toast.LENGTH_SHORT).show();
                            taskList.getList().get(position).setChecked(false);
                            //taskAdapter.getItem(position).setChecked(false);
                            taskList.saveDatabase();
                            refreshList();
                        }
                    }
                }
                else{
                    Intent intent = new Intent(getActivity(),TaskViewActivity.class);

                    taskPositions.clear();
                    taskPositions.add(position);

                    intent.putExtra(MainActivity.TASK_TITLE_EXTRA,taskAdapter.getItem(position).getName());
                    intent.putExtra(MainActivity.TASK_POSITION_EXTRA,taskPositions);
                    startActivity(intent);
                }

            }
        });

        registerForContextMenu(getListView());
    }
    public void refreshList(){
        taskAdapter.notifyDataSetChanged();
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
                taskName = taskList.getList().get(rowPosition).getName();
                taskDescription = taskList.getList().get(rowPosition).getDescription();
                addTaskDialog();
                createTaskDialog.show();
                return true;
            case R.id.delete:
                //TODO add confirmation dialog or snackbar undo
                taskList.removeTask(rowPosition);
                refreshList();
                break;
        }
        return super.onContextItemSelected(item);
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
                    //taskList.getList().get(rowPosition).setName(titleBox.getText().toString());
                    //taskList.getList().get(rowPosition).setDescription(descriptionBox.getText().toString());
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
