package com.example.n.myfirstapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyMessagingService extends FirebaseMessagingService {
    public MyMessagingService() {
    }

    private static final String TAG = "MyFirebaseMsgService";

    // [START receive_message]
    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Message containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            final NotificationManager notif = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            final Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            /*
            Notification notify=new Notification.Builder(getApplicationContext())
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setSmallIcon(R.drawable.ic_launcher).build();
            */
            // Phone a number (emergency services)
            Intent phoneCall = new Intent(Intent.ACTION_CALL);
            phoneCall.setData(Uri.parse("tel: 0278272086"));

            final PendingIntent phoneCallIntent = PendingIntent.getActivity(getApplicationContext(), 0, phoneCall, PendingIntent.FLAG_UPDATE_CURRENT);

            String imageURL = remoteMessage.getData().get("img_url");
            final Bitmap[] bmp = new Bitmap[1];

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/myfirstapplication-5ad99.appspot.com/o/image%2F5s9kRal7NpU2taXF3TeQRplVtPC3-248524188.png?alt=media&token=77a54874-93c7-4042-9f21-d5d26c75408d");
            httpsReference.getBytes(10485760).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    bmp[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    NotificationCompat.Builder notify = new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentText(remoteMessage.getData().get("message"))
                            .setContentTitle(remoteMessage.getData().get("title"))
                            .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                            .setLights(Color.RED, 3000, 3000)
                            .setSound(alarmSound)
                            .addAction(R.drawable.ic_call_emergency, "Call Emergency Services", phoneCallIntent)
                            .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bmp[0]));

                    //notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    notif.notify(0, notify.build());
                }
            });


        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]
}
