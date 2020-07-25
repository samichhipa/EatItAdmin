package com.example.eatitadmin.Notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.eatitadmin.Common.Common;
import com.example.eatitadmin.MainActivity;
import com.example.eatitadmin.NotificationActivity;
import com.example.eatitadmin.OrderStatusActivity;
import com.example.eatitadmin.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.security.PrivateKey;
import java.util.Map;
import java.util.Random;

public class FirebaseMessaging extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        SharedPreferences pref=this.getSharedPreferences("USER_INFO", MODE_PRIVATE);

        String currentUserId=pref.getString("current_id","");

        String sent_id = remoteMessage.getData().get("sent_id");
        String user_id = remoteMessage.getData().get("user_id");


        if (Common.currentUser.getPhone()!=null && sent_id.equals(Common.currentUser.getPhone())){

            if (!currentUserId.equals(user_id)){

                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

                     OreoAndAboveNotification(remoteMessage);

                }else{

                   sendNotification(remoteMessage);
                }

            }



        }







    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void OreoAndAboveNotification(RemoteMessage remoteMessage) {


        String sent_id = remoteMessage.getData().get("sent_id");
        String user_id = remoteMessage.getData().get("user_id");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String notification_type = remoteMessage.getData().get("notification_type");


        Intent in=new Intent(this, NotificationActivity.class);
        in.putExtra("title",title);
        in.putExtra("body",body);
        in.putExtra("notification_type",notification_type);
        in.putExtra("user_id",user_id);
        in.putExtra("sent_id",sent_id);

        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);




        Intent intent=new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper=new NotificationHelper(this);

        NotificationCompat.Builder builder=null;

        if (notification_type.equals("Call")){

            builder=notificationHelper.getEatItChannelNotification(title,"Table No " +body+ "Calling...",pendingIntent,defaultSoundUri);
        }
        else if (notification_type.equals("Billing")){

             builder=notificationHelper.getEatItChannelNotification(title,"Table No " +body+ "Calling for Bill...",pendingIntent,defaultSoundUri);

        }else if (notification_type.equals("Order")){

            builder=notificationHelper.getEatItChannelNotification(title,"New Order Order ID" +body,pendingIntent,defaultSoundUri);
        }
        else if (notification_type.equals("OrderChange")){

            builder=notificationHelper.getEatItChannelNotification(title,"Order AddOn Order ID" +body,pendingIntent,defaultSoundUri);

        }



        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        String sent_id = remoteMessage.getData().get("sent_id");
        String user_id = remoteMessage.getData().get("user_id");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String notification_type = remoteMessage.getData().get("notification_type");


        Intent in=new Intent(this, NotificationActivity.class);
        in.putExtra("title",title);
        in.putExtra("body",body);
        in.putExtra("notification_type",notification_type);
        in.putExtra("user_id",user_id);
        in.putExtra("sent_id",sent_id);

        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);

        Intent intent=new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder builder=null;

        if (notification_type.equals("Call")){

           builder= new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText("Table No " +body+ "Calling...")
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);


        }
        else if (notification_type.equals("Billing")){

            builder= new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText("Table No " +body+ "Calling for Bill...")
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);


        }else if (notification_type.equals("Order")){

            builder= new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText("New Order Order ID" +body)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

        }else if (notification_type.equals("OrderChange")){

            builder= new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText("Order AddOn Order ID" +body)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

        }


        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());
    }


}
