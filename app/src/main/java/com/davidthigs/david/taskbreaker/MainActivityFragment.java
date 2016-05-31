package com.davidthigs.david.taskbreaker;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends ListFragment {
    private TaskAdapter taskAdapter;
    private TaskList taskList;

    private  ArrayList<Integer> taskPositions;
    public MainActivityFragment() {
    }


    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);

        taskList = (TaskList)getActivity().getApplicationContext();

       taskAdapter = new TaskAdapter(getActivity(), taskList.getList());

        setListAdapter(taskAdapter);

    }
    public void refreshList(){
        taskAdapter.notifyDataSetChanged();
    }
    @Override
    public void onListItemClick(ListView listView,View v, int position,long id){
        super.onListItemClick(listView,v,position,id);
        //launch task activity
         //currentTask = taskAdapter.getItem(position);
        //taskAdapter.clear();
        //taskAdapter.addAll(currentTask.getChildren());

        Intent intent = new Intent(getActivity(),TaskViewActivity.class);

        taskPositions = new ArrayList<>();
        taskPositions.add(position);
        //Bundle bundle = new Bundle();
         //bundle.putSerializable(MainActivity.TASK_LIST_EXTRA,taskList);
        //bundle.putSerializable(MainActivity.TASK_OBJECT_EXTRA,taskList.getList().get(position));
        intent.putExtra(MainActivity.TASK_TITLE_EXTRA,taskAdapter.getItem(position).getName());
        intent.putExtra(MainActivity.TASK_POSITION_EXTRA,taskPositions);
        //intent.putExtra(MainActivity.TASK_BUNDLE_EXTRA,bundle);
        startActivity(intent);


    }
    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == MainActivity.TASK_REQUEST_CODE){

            if(resultCode == MainActivity.RESULT_OK){
                ArrayList<Task> children = (ArrayList<Task>)data.getSerializableExtra(MainActivity.TASK_OBJECT_EXTRA);
                taskAdapter.getItem(pos).setChildren(children);

                taskAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    */

}