package com.example.n.myfirstapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.n.myfirstapplication.ui.activities.MessageLogsActivity;

public class NavigationActivity extends AppCompatActivity {

    private Button messages;
    private Button contacts;
    private Intent messagesIntent;
    private Intent contactsIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        messagesIntent = new Intent(this, MessageLogsActivity.class);
        messages = (Button) findViewById(R.id.messagesBtn);
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(messagesIntent);
            }
        });

        contactsIntent = new Intent(this, ContactActivity.class);
        contacts = (Button) findViewById(R.id.contactBtn);
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(contactsIntent);
            }
        });
    }


}
