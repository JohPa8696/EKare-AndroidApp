package com.example.n.myfirstapplication.ui.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.n.myfirstapplication.R;
import com.example.n.myfirstapplication.dto.Message;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.List;

/**
 * Created by johnn on 7/17/2017.
 */

public class MessageAdapter extends BaseAdapter {
    private Context context;
    private List<Message> messages;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference= firebaseStorage.getReference();
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
        ImageView image = (ImageView) v.findViewById(R.id.accidentscene);
        String uri= messages.get(i).getImageURL();

        if(!uri.equals("")){
            StorageReference imageRef = storageReference.child("image").child(uri);
            image.getLayoutParams().height=500;
            image.getLayoutParams().width=600;
            // Load the image using Glide
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(imageRef)
                    .into(image);
        }
        //Download the image, take alot of time
        sender.setText(messages.get(i).getSender());
        messageBody.setText(messages.get(i).getMessage());
        timestamp.setText(messages.get(i).getTime());
        return v;
    }

}
