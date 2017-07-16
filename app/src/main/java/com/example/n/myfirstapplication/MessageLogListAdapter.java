package com.example.n.myfirstapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by johnn on 7/15/2017.
 */

public class MessageLogListAdapter extends BaseAdapter{

    private Context mContext;
    private List<MessageLog> messageLogList;

    public MessageLogListAdapter(Context mContext, List<MessageLog> conversations) {
        this.mContext = mContext;
        this.messageLogList = conversations;
    }


    @Override
    public int getCount() {
        return messageLogList.size();
    }

    @Override
    public Object getItem(int i) {
        return messageLogList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext,R.layout.message_log,null);
        TextView profilePic = (TextView) v.findViewById(R.id.profilepic_tv);
        TextView username = (TextView) v.findViewById(R.id.username_tv);
        TextView latestMessage = (TextView) v.findViewById(R.id.latestmessage_tv);
        TextView timestamp = (TextView) v.findViewById(R.id.timestamp_tv);

        //TODO :change profile pic
        profilePic.setText("PROFILE PIC");
        username.setText(messageLogList.get(i).getUserName());
        latestMessage.setText(messageLogList.get(i).getLastMessage());
        timestamp.setText(messageLogList.get(i).getTimeStamp());

        v.setTag(messageLogList.get(i).getMessageLogId());

        return v;
    }
}
