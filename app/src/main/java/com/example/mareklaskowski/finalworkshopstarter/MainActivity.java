package com.example.mareklaskowski.finalworkshopstarter;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MY_ACCESS_FINE_LOCATION = 1;
    MyBroadcastReceiver myBroadcastReceiver;
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    Intent alarmIntent;
    Location firstLocation;

    /**
     * This BroadcastReciever is for recieving intents while the Activity is running
     * it's instantiated in onCreate
     * it's registerd in the onResume() method
     * and it's removed in onPause() method
     */
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleIntentBroadcast(intent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = (Button) findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMyService();
            }

        });

        Button stopButton = (Button) findViewById(R.id.button_stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopMyService();
            }

        });

        myBroadcastReceiver = new MyBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //register the myBroadcastReceiver
        //to receive broadcast Intents we need to do a couple of things.
        //create an IntentFilter
        IntentFilter filter = new IntentFilter("com.example.marek.NOTIFICATION_CLICKED");
        //register the receiver
        registerReceiver(myBroadcastReceiver, filter);

        getCurrentLocation();
    }

    private void getCurrentLocation() {
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location;

        if (network_enabled) {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(location!=null){
                firstLocation = location;
                Log.d("Location Twigz", "getCurrentLocation: " + firstLocation.getLongitude());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister the myBroadcastReceiver
        unregisterReceiver(myBroadcastReceiver);
    }

    /*
    starts our service
     */
    void startMyService(){
        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //openImageIntent();
            //explicit intent to start the MyService
            //Intent intent = new Intent(this, MyService.class);
            //start the service by calling Context.startService()
            //startService(intent);
            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
            getCurrentLocation();

            startReminderAlarm();
        } else {
            // Permission denied
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_ACCESS_FINE_LOCATION);
        }
    }

    /*
    stops our service
     */
    void stopMyService(){
        Intent intent = new Intent(this, MyService.class);
        //Context.stopService()
        stopService(intent);
    }

    void handleIntentBroadcast(Intent intent){
        //System.out.println("Activity received an intent: "+ intent.toString());
        //TextView textView = (TextView) findViewById(R.id.textView);
        //textView.setText(intent.toString());
        //TODO: process the extras in the intent
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //openImageIntent();
                    //explicit intent to start the MyService
                    startReminderAlarm();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void startReminderAlarm() {
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int interval = 60000;

        // Retrieve a PendingIntent that will perform a broadcast
        alarmIntent = new Intent(this, MyService.class).setAction(MyService.ACTION_REMINDER);
        pendingIntent = PendingIntent.getService(this,
                0, alarmIntent, 0);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
    }

}
