package com.example.n.myfirstapplication.ui.adapter;

import android.content.Context;
import com.example.n.myfirstapplication.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.n.myfirstapplication.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by johnn on 7/17/2017.
 */

public class MessageAdapter extends BaseAdapter {
    private Context context;
    private List<Message> messages;
    private String senderUsername;
    public MessageAdapter(Context context, List<Message> messages,String sennderUsername) {
        this.context = context;
        this.messages = messages;
        this.senderUsername =sennderUsername;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.message, null);
        TextView sender = (TextView) v.findViewById(R.id.profilepicmessage_tv);
        TextView messageBody = (TextView) v.findViewById(R.id.message_tv);
        TextView timestamp = (TextView) v.findViewById(R.id.timestamp_tv);

        sender.setText(senderUsername);
        messageBody.setText(messages.get(i).getMessage());
        timestamp.setText(messages.get(i).getTime());

        return v;
    }
}
