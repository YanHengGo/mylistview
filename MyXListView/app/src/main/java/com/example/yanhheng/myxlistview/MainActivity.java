package com.example.yanhheng.myxlistview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import android.os.Handler;

public class MainActivity extends Activity implements MyXListView.ListViewListener{

    private MyXListView myListview;
    private ArrayList<String> dataList = new ArrayList<String>();
    private int start_index =0;
    private int MAX_VALUE = 20;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myListview = (MyXListView)findViewById(R.id.my_listview);
        myListview.setListener(this);
        loadData();
        setListAdapter();
    }

    private void setListAdapter() {
        if(arrayAdapter==null){
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, dataList);
            myListview.setAdapter(arrayAdapter);
        }else{
            arrayAdapter.notifyDataSetChanged();
        }
    }

    private void loadData() {
        loadDataFrom(start_index);
    }
    private void loadDataFrom(int start) {
        for(int index = start;index<MAX_VALUE;index++){
            dataList.add("yanheng list index is "+index+".");
        }
    }

    Handler hander = new Handler();
    @Override
    public void setRefreshing() {
        hander.postDelayed(new Runnable() {
            @Override
            public void run() {
                dataList.clear();
                start_index--;
                loadData();
                setListAdapter();
                myListview.setRefreshingFinished();
            }
        }, 2000);
    }

    @Override
    public void setLoading() {
        hander.postDelayed(new Runnable() {
            @Override
            public void run() {
                int startWith=MAX_VALUE;
                MAX_VALUE+=10;
                loadDataFrom(startWith);
                setListAdapter();
                myListview.setLoadingFinished();
            }
        }, 2000);
    }
}
