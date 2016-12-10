package com.example.mareklaskowski.finalworkshopstarter;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import static android.provider.LiveFolders.INTENT;
import static java.util.logging.Level.parse;

public class MyService extends Service {
    int notificationCount;
    public static final String ACTION_STOP = "action_stop";

    /** Intent Actions **/
    public static final String ACTION_REMINDER = "action_reminder";
    public static final String ACTION_LOCATION = "action_location";

    /*
    AVOID CALLING ANY Android APIs from here ex. Context.whatever
     */
    public MyService() {
        notificationCount = 1;
    }

    /*
    onStartCommand() is a better place to do initialization code
     */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

/*
        //show a notification when the service starts
        Notification.Builder mBuilder = new Notification.Builder(this).setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Service was started").setContentText("Like us on Facebook!");
        //create an implicit Intent that will be broadcast when the user clicks on the Notification
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/senecacollege/"));
        //create a PendingIntent to package the above intent
        PendingIntent notificationPendingIntent = PendingIntent.getBroadcast(this, 123, notificationIntent, 0);



      /*  AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 60); // first time
        long frequency= 60 * 1000; // in ms
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequency, notificationPendingIntent);
        notificationIntent.setAction(Long.toString(System.currentTimeMillis()));

        //associate the two using our Notifaction.Builder instance
        mBuilder.setContentIntent(notificationPendingIntent);
        //get an instacnce of NoficationManger
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //notify the user (fire off the notification)s
        notificationManager.notify(notificationCount++, mBuilder.build());

        return super.onStartCommand(intent, flags, startId);
*/
        if(intent.getAction().equals(ACTION_REMINDER)) {
            //show a notification when the service starts
            Notification.Builder mBuilder = new Notification.Builder(this).setSmallIcon(R.drawable.ic_stat_name)
                    .setContentText("Like Us On FaceBook!");

            Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/senecacollege/"));
            //create a PendingIntent to package the above intent
            PendingIntent notificationPendingIntent =
                    PendingIntent.getActivity(this, 123, notificationIntent, 0);
            //associate the two using our Notifaction.Builder instance
            mBuilder.setContentIntent(notificationPendingIntent);
            //get an instacnce of NoficationManger
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //notify the user (fire off the notification)
            notificationManager.notify(notificationCount++, mBuilder.build());
        } else if(intent.getAction().equals(ACTION_LOCATION)) {
            //show a notification when the service starts
            Notification.Builder mBuilder = new Notification.Builder(this).setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle("Final WorkShop")
                    .setContentText("Like Us On FaceBook!")
                    .setAutoCancel(true);

            Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/senecacollege/"));
            //create a PendingIntent to package the above intent
            PendingIntent notificationPendingIntent =
                    PendingIntent.getActivity(this, 123, notificationIntent, 0);
            //associate the two using our Notifaction.Builder instance
            mBuilder.setContentIntent(notificationPendingIntent);
            //get an instacnce of NoficationManger
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //notify the user (fire off the notification)
            notificationManager.notify(notificationCount++, mBuilder.build());
        }


        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "service stopping", Toast.LENGTH_LONG).show();

        /*
        WARNING: if another android component (ex. a system service) has a reference or is sending Intents to this service
        it won't actually be stopped.
        MAKE SURE you deregister with System Services (here) ex. AlarmManager, LocationManager
        (if you have BroadcastRecievers, deregister them too!)
         */
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
