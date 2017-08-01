package com.example.n.myfirstapplication.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.n.myfirstapplication.dto.MessageLog;
import com.example.n.myfirstapplication.R;

import java.util.List;

/**
 * Created by johnn on 7/15/2017.
 */

public class MessageLogListAdapter extends BaseAdapter{

    private Context mContext;
    private List<MessageLog> contactList;

    public MessageLogListAdapter(Context mContext, List<MessageLog> conversations) {
        this.mContext = mContext;
        this.contactList = conversations;
    }


    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int i) {
        return contactList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.message_log,null);
        TextView profilePic = (TextView) v.findViewById(R.id.profilepic_tv);
        TextView username = (TextView) v.findViewById(R.id.username_tv);
        TextView latestMessage = (TextView) v.findViewById(R.id.latestmessage_tv);
        TextView timestamp = (TextView) v.findViewById(R.id.timestamp_tv);

        //TODO :change profile pic
        profilePic.setText("PROFILE PIC");
        username.setText(contactList.get(i).getUserName());
        latestMessage.setText(contactList.get(i).getLastMessage());
        timestamp.setText(contactList.get(i).getTimeStamp());

        v.setTag(contactList.get(i).getMessageLogId());

        return v;
    }
}
