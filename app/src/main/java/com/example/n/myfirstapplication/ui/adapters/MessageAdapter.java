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
import com.example.n.myfirstapplication.dto.Contact;
import com.example.n.myfirstapplication.dto.Message;
import com.example.n.myfirstapplication.dto.User;
import com.example.n.myfirstapplication.untilities.FirebaseReferences;
import com.example.n.myfirstapplication.untilities.FirebaseStrings;
import com.example.n.myfirstapplication.untilities.UserGlobalValues;
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
    private String userEmail;
    private String userProfilePic = "";
    private String otherProfilePic = "";
    private DatabaseReference userRef;
    public MessageAdapter(Context context, List<Message> messages, String email) {
        this.context = context;
        this.messages = messages;
        user = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        this.userEmail = email;
        // Get the current user email
        userEmail = FirebaseReferences.MY_AUTH.getCurrentUser().getEmail();
        userProfilePic = UserGlobalValues.contactProfileURIs.get(userEmail.trim().toLowerCase());
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

        // Get the contact profile pic uri
        if(!messages.get(i).getSender().trim().toLowerCase().equals(userEmail)
                &&otherProfilePic.equals("")){
            otherProfilePic = UserGlobalValues.contactProfileURIs
                                .get(messages.get(i).getSender().toLowerCase().trim());
        }

        String profilePicUri ="";

        // 2 different uis for receiver and sender
        if(!userEmail.trim().toLowerCase().equals(messages.get(i).getSender().trim().toLowerCase())){
            // current user is the receiver
            v = View.inflate(context, R.layout.message_receiver, null);
            profilePic = (CircleImageView) v.findViewById(R.id.profilepicmessage_r_tv);
            messageBody = (TextView) v.findViewById(R.id.message_r_tv);
            timestamp = (TextView) v.findViewById(R.id.timestamp_r_tv);
            if(!messages.get(i).isReceiverSeen()){
                messageBody.setBackgroundResource(R.drawable.chatbubble_receiver_hl);
                timestamp.setTextColor(0x303030);
            }
            image = (ImageView) v.findViewById(R.id.accidentscene_r);
            profilePicUri = otherProfilePic;
        }else{
            // Current user is the sender
            v = View.inflate(context, R.layout.message_sender, null);
            profilePic = (CircleImageView) v.findViewById(R.id.profilepicmessage_s_tv);
            messageBody = (TextView) v.findViewById(R.id.message_s_tv);
            timestamp = (TextView) v.findViewById(R.id.timestamp_s_tv);
            if(!messages.get(i).isSenderSeen()){
                messageBody.setBackgroundResource(R.drawable.chatbubble_sender_hl);
            }
            image = (ImageView) v.findViewById(R.id.accidentscene_s);
            profilePicUri = userProfilePic;
        }
        String uri= messages.get(i).getImageURL();

        // Set customer fonts for the textview components
        Typeface robotoLightFont = Typeface.createFromAsset(context.getAssets(),"font/robotolight.ttf");
        //Typeface robotoRegularFont = Typeface.createFromAsset(context.getAssets(),"font/robotoregular.ttf");
        Typeface robotoThinFont = Typeface.createFromAsset(context.getAssets(),"font/robotothin.ttf");

        messageBody.setTypeface(robotoLightFont);
        timestamp.setTypeface(robotoThinFont);

        // GetProfile picture for user,
        if(!profilePicUri.equals("default_profilepic.png")){
            StorageReference profilePicRef = storageReference.child("profile_pictures").child(profilePicUri);
            // Load the image using Glide
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(profilePicRef)
                    .into(profilePic);
        }

        // Display image of accident
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
        //timestamp.setText(messages.get(i).getDate()+ "\n" +messages.get(i).getTime());
        timestamp.setText(messages.get(i).getTime());

        return v;
    }

}
