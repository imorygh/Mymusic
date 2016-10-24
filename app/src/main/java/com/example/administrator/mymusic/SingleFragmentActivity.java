package com.example.administrator.mymusic;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.mymusic.Model.Constants;

/**
 * Created by Administrator on 2016/9/20.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.fragment_container);
        if (fragment==null){
            fragment=createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit();
        }
//        notifyMyNoti(this,false," "," ");

    }
    public static void notifyMyNoti(Context context, boolean isPlaying,
                                    String musicName,String musicDetail){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)


                // Set required fields, including the small icon, the
                // notification title, and text.
                .setSmallIcon(R.drawable.ic_stat_new_message)
                .setContentTitle(context.getResources().getString(
                        R.string.new_message_notification_title_template, musicName))
                .setContentText(musicDetail)

                // All fields below this line are optional.

                // Use a default priority (recognized on devices running Android
                // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)


                // Set ticker text (preview) information for this notification.
                .setTicker(musicName)

                // Show a number. This is useful when stacking notifications of
                // a single type.
//                .setNumber(number)

                // If this notification relates to a past or upcoming event, you
                // should set the relevant time information using the setWhen
                // method below. If this call is omitted, the notification's
                // timestamp will by set to the time at which it was shown.
                // TODO: Call setWhen if this notification relates to a past or
                // upcoming event. The sole argument to this method should be
                // the notification timestamp in milliseconds.
                //.setWhen(...)

                // Set the pending intent to be initiated when the user touches
                // the notification.
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                new Intent(context,MyMusicActivity.class),
                                PendingIntent.FLAG_UPDATE_CURRENT))

                // Example additional actions for this notification. These will
                // only show on devices running Android 4.1 or later, so you
                // should ensure that the activity in this notification's
                // content intent provides access to the same actions in
                // another way.
                .addAction(
                        R.drawable.ic_action_noti_previous,
                        null,
                        PendingIntent.getBroadcast(
                                context,
                                0,
                                new Intent(Constants.HELP_PREVIOUS_BUTTON),
                                0))

                .addAction(
                        isPlaying?R.drawable.ic_action_noti_pause:R.drawable.ic_action_noti_play,
                        null,
                        PendingIntent.getBroadcast(
                                context,
                                0,
                                new Intent(Constants.HELP_PLAY_BUTTON),
                                0))
                .addAction(
                        R.drawable.ic_action_noti_next,
                        null,
                        PendingIntent.getBroadcast(
                                context,
                                0,
                                new Intent(Constants.HELP_NEXT_BUTTON),
                                0))

                // Automatically dismiss the notification when it is touched.
                .setAutoCancel(false)
                .setOngoing(true);
        NotificationManager nm= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0,builder.build());
    }


}
