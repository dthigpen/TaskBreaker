package com.davidthigs.david.taskbreaker;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskViewListFragment extends ListFragment {
    private TaskAdapter taskAdapter;
    private ArrayList<Integer> taskPositions;
    private TaskList taskList;

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

        taskAdapter = new TaskAdapter(getActivity(),task.getChildren());
        setListAdapter(taskAdapter);
    }

    public void refreshList(){
        taskAdapter.notifyDataSetChanged();
    }
    @Override
    public void onListItemClick(ListView listView, View v, int position, long id) {
        Intent intent = new Intent(getActivity(),TaskViewActivity.class);

        ArrayList<Integer>newTaskPositions = new ArrayList(taskPositions);

        newTaskPositions.add(position);
        intent.putExtra(MainActivity.TASK_TITLE_EXTRA,taskAdapter.getItem(position).getName());
        intent.putExtra(MainActivity.TASK_POSITION_EXTRA,newTaskPositions);
        startActivity(intent);

    }

}
