package com.example.n.myfirstapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageLogsActivity extends AppCompatActivity {
    private ListView messageLogsLv;
    private MessageLogListAdapter adapter;
    private List<MessageLog> messageLogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_logs);

        //TODO get the list of contacts from database

        messageLogsLv = (ListView) findViewById(R.id.messagelogs_listview);

        messageLogList = new ArrayList<>();

        messageLogList.add(new MessageLog("1","user1","hello11111111",Long.toString(System.currentTimeMillis())));
        messageLogList.add(new MessageLog("2","user2","hello1",Long.toString(System.currentTimeMillis())));
        messageLogList.add(new MessageLog("3","user3","hello2",Long.toString(System.currentTimeMillis())));
        messageLogList.add(new MessageLog("4","user4","hello3",Long.toString(System.currentTimeMillis())));
        messageLogList.add(new MessageLog("5","user5","hello4",Long.toString(System.currentTimeMillis())));
        messageLogList.add(new MessageLog("6","user6","hello1",Long.toString(System.currentTimeMillis())));
        messageLogList.add(new MessageLog("7","user7","hello2",Long.toString(System.currentTimeMillis())));
        messageLogList.add(new MessageLog("8","user8","hello3",Long.toString(System.currentTimeMillis())));
        messageLogList.add(new MessageLog("9","user9","hello4",Long.toString(System.currentTimeMillis())));
        messageLogList.add(new MessageLog("10","user10","hello3",Long.toString(System.currentTimeMillis())));
        messageLogList.add(new MessageLog("11","user11","hello4",Long.toString(System.currentTimeMillis())));


        adapter = new MessageLogListAdapter(getApplicationContext(), messageLogList);
        messageLogsLv.setAdapter(adapter);

        messageLogsLv.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),"CLICKED" + view.getTag(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
