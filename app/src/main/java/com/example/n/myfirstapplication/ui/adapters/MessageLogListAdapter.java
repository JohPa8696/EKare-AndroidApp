package com.example.n.myfirstapplication.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.n.myfirstapplication.dto.MessageLog;
import com.example.n.myfirstapplication.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
        TextView username = (TextView) v.findViewById(R.id.username_tv);
        TextView latestMessage = (TextView) v.findViewById(R.id.latestmessage_tv);
        TextView timestamp = (TextView) v.findViewById(R.id.timestamp_tv);

        //TODO :change profile pic
        String uri = contactList.get(i).getProfileUri();
        if(uri!=null&&!uri.equals("")){
            StorageReference imageRef = storageReference.child("profile_pictures").child(uri);
//            profilePic.getLayoutParams().height=500;
//            profilePic.getLayoutParams().width=600;
            // Load the image using Glide
            Glide.with(mContext)
                    .using(new FirebaseImageLoader())
                    .load(imageRef)
                    .into(profilePic);
        }

        username.setText(contactList.get(i).getUserName());
        if(!contactList.get(i).getLastMessage().equals("")){
            latestMessage.setText(contactList.get(i).getLastMessage());
        }
        timestamp.setText(contactList.get(i).getTimeStamp());

        v.setTag(contactList.get(i).getMessageLogId());

        return v;
    }
}
