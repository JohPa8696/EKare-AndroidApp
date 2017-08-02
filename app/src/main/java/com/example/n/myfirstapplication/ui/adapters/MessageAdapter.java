package com.example.n.myfirstapplication.ui.adapters;

import android.content.Context;

import com.bumptech.glide.Glide;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.n.myfirstapplication.Authentication;
import com.example.n.myfirstapplication.R;
import com.example.n.myfirstapplication.dto.Message;
import com.example.n.myfirstapplication.dto.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by johnn on 7/17/2017.
 */

public class MessageAdapter extends BaseAdapter {
    private Context context;
    private List<Message> messages;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth user;
    private StorageReference storageReference;
    private String userName;
    private DatabaseReference userRef;
    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
        user = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference= firebaseStorage.getReference();
        // Get the current user name
        userRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(user.getCurrentUser().getUid());


//        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot child : dataSnapshot.getChildren()){
//                    if(child.getKey().equals("name")) {
//                        userName = child.getValue().toString();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
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
        View v;
        CircleImageView profilePic;
        TextView messageBody;
        TextView timestamp;
        ImageView image;

//        if(!userName.toLowerCase().equals(messages.get(i).getSender().trim().toLowerCase())){
        if(!user.getCurrentUser().getEmail().equals("kj@gmail.com")){
            v = View.inflate(context, R.layout.message_receiver, null);
            profilePic = (CircleImageView) v.findViewById(R.id.profilepicmessage_r_tv);
            messageBody = (TextView) v.findViewById(R.id.message_r_tv);
            timestamp = (TextView) v.findViewById(R.id.timestamp_r_tv);
            image = (ImageView) v.findViewById(R.id.accidentscene_r);
        }else{
            v = View.inflate(context, R.layout.message_sender, null);
            profilePic = (CircleImageView) v.findViewById(R.id.profilepicmessage_s_tv);
            messageBody = (TextView) v.findViewById(R.id.message_s_tv);
            timestamp = (TextView) v.findViewById(R.id.timestamp_s_tv);
            image = (ImageView) v.findViewById(R.id.accidentscene_s);
        }
        String uri= messages.get(i).getImageURL();

        // Set customer fonts for the textview components
        Typeface robotoLightFont = Typeface.createFromAsset(context.getAssets(),"font/robotolight.ttf");
        //Typeface robotoRegularFont = Typeface.createFromAsset(context.getAssets(),"font/robotoregular.ttf");
        Typeface robotoThinFont = Typeface.createFromAsset(context.getAssets(),"font/robotothin.ttf");

        messageBody.setTypeface(robotoLightFont);
        timestamp.setTypeface(robotoThinFont);

        // GetProfile picture for user,
//        String profilePicUri ="1501160313943.png";
//        if(!profilePicUri.equals("default_profilepic.png")){
//            StorageReference profilePicRef = storageReference.child("profile_pictures").child(profilePicUri);
////            profilePic.getLayoutParams().height=500;
////            profilePic.getLayoutParams().width=600;
//            // Load the image using Glide
//            Glide.with(context)
//                    .using(new FirebaseImageLoader())
//                    .load(profilePicRef)
//                    .into(profilePic);
//        }

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
        if(!messages.get(i).getMessage().trim().equals("")) {
            messageBody.setText(messages.get(i).getMessage());
        }
        timestamp.setText(messages.get(i).getTime());
        return v;
    }

}
