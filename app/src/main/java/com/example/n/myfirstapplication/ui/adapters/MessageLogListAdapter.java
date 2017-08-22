package com.example.n.myfirstapplication.ui.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.n.myfirstapplication.dto.MessageLog;
import com.example.n.myfirstapplication.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by johnn on 7/15/2017.
 */

public class MessageLogListAdapter extends BaseAdapter{

    private Context mContext;
    private List<MessageLog> contactList;
    private StorageReference storageReference;
    public MessageLogListAdapter(Context mContext, List<MessageLog> conversations) {
        this.mContext = mContext;
        this.contactList = conversations;
        storageReference = FirebaseStorage.getInstance().getReference();
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
        CircleImageView profilePic = (CircleImageView) v.findViewById(R.id.profilepic_iv);
        TextView usernameTv= (TextView) v.findViewById(R.id.username_tv);
        TextView latestMessageTv = (TextView) v.findViewById(R.id.latestmessage_tv);
        TextView timestampTv = (TextView) v.findViewById(R.id.timestamp_tv);
        TextView numNotificationsTv = (TextView) v.findViewById(R.id.num_notifications_tv);
        LinearLayout notificationLo = (LinearLayout) v.findViewById(R.id.notification_layout);
        RelativeLayout layout =(RelativeLayout) v.findViewById(R.id.messageloginfo_layout);
        //TODO :change profile pic
        String uri = contactList.get(i).getProfileUri();
        if(uri!=null&&!uri.equals("")){
            StorageReference imageRef = storageReference.child("profile_pictures").child(uri);

            // Load the image using Glide
            Glide.with(mContext)
                    .using(new FirebaseImageLoader())
                    .load(imageRef)
                    .into(profilePic);
        }

        // Set the background color to indicate new notifications
        int numNoti = contactList.get(i).getNumNotifications();
        if(numNoti>0){
            layout.setBackgroundColor(ContextCompat.getColor(mContext,R.color.lightgreen));
            latestMessageTv.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
            numNotificationsTv.setText(Integer.toString(numNoti));
            notificationLo.setBackgroundResource(R.drawable.ic_notifications_black_24dp);
        }
        usernameTv.setText(contactList.get(i).getUserName());
        if(!contactList.get(i).getLastMessage().equals("")){
            latestMessageTv.setText(contactList.get(i).getLastMessage());
        }

        timestampTv.setText(contactList.get(i).getTimeStamp());
        if(numNoti>0){
            timestampTv.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
        }

        v.setTag(contactList.get(i).getMessageLogId());

        return v;
    }
}
