package com.fortnow.upstairs;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;


public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;



    private static final int SERVERPORT = 4998;
    private static final String SERVER_IP = "192.168.1.33";
    //public Timer timer;
    // private static final long timerLength = 4*60*60*1000; // 4 hour delay




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // turnOff2(); // Turn off speakers when run - removed
       /* Timer code: removed since won't work if app not active
        timer = new Timer();
        */

        /* Future alarm set up

        // Turn off every day at 3 AM
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 3); // For 3 AM
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pi = PendingIntent.getService(MainActivity.this, 0,
                new Intent(MainActivity.Al, MyClass.class),PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);

         */



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public void updateUpstairs(String state) {


        Socket socket;
        PrintWriter basement;
        // String checkValue; // Don't need to check
        BufferedReader basementCheck;


        try {
            // InetAddress serverAddr = InetAddress.getByName(SERVER_IP); user Server IP directly

           // socket = new Socket(serverAddr, SERVERPORT);

            socket = new Socket(SERVER_IP,SERVERPORT); // Open socket

          /* basement = new PrintWriter(new BufferedWriter(
           new OutputStreamWriter(socket.getOutputStream())),
           true);
           */

            basement = new PrintWriter(socket.getOutputStream(),true); // set up output socket
            basementCheck = new BufferedReader(new InputStreamReader(socket.getInputStream()));





            basement.println("setstate,1:1,"+state+"\r"); // set speakers set 1

            basementCheck.readLine(); // await confirmation

            basement.println("setstate,1:2,"+state+"\r"); // set speaker set 2

            basementCheck.readLine(); // await confirmation


           /*
           basement.println("setstate,1:1," + state);
            basement.println("setstate,1:2," + state);
            */

            socket.close();
        }

        catch (Exception e) {
            e.printStackTrace();
        }


    }




    public void turnOn(View view) {
        /* Timer code removed
        timer.cancel();  // Cancel and restart timer
        timer = new Timer();
        */
        new Thread(new Runnable() {
            public void run() {
                updateUpstairs("1");
            }
        }).start();

          //      timer.schedule(new turnOffTask(), timerLength);
    }

    /* TurnOffTask no longer used


    class turnOffTask extends TimerTask {
        public void run() {
            turnOff2();
        }
    }
    */


    public void turnOff(View view) {
        turnOff2();
    }


    // seperate to call from timer as well as button
    public void turnOff2() {
        new Thread(new Runnable() {
            public void run() {
                updateUpstairs("0");
            }
        }).start();
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }







    @Override
    public void onStart() {
        super.onStart();

       // turnOff2(); No longer turn off when started

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

