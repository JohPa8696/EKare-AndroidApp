package com.example.n.myfirstapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

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

        messagesIntent = new Intent(this, MainActivity.class);
        messages = (Button) findViewById(R.id.messagesBtn);
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(messagesIntent);
            }
        });

        contactsIntent = new Intent(this, contactActivity.class);
        contacts = (Button) findViewById(R.id.contactBtn);
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(contactsIntent);
            }
        });
    }


}
