package com.example.donkha_app.Helper;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


import com.example.donkha_app.MainUser;
import com.example.donkha_app.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.support.constraint.Constraints.TAG;
import static com.example.donkha_app.App.CHANNEL_ID;

public class NotificationService extends Service {
    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    boolean relsult ;
    int Your_X_SECS = 2;
   public int counter=0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("asdasd", "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        startTimer(intent);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        Toast.makeText(this, "adsadsadad", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        //stoptimertask();

    }

    final Handler handler = new Handler();
    public void startTimer(Intent intent) {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask(intent);

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 2000, Your_X_SECS * 1000); //
        //timer.schedule(timerTask, 5000,1000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask(final Intent intent) {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {

                        int random = new Random().nextInt(10);

                        String url = "http://18.140.49.199/Donkha/Service_app/check_received_tranfer_money";
                        Boolean rel = false;
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("account_id",intent.getStringExtra("account_id")));

                        String response = WebSevConnect.getHttpPost(url,params,getApplicationContext());
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                Log.d("SUCCESSS","SUCCESSS");

                                JSONObject jsonAccount = obj.getJSONObject("check");
                                ShowNotification(jsonAccount.getString("account_id_tranfer"),"testttttt");

                                stopSelf();
                            }
                            else{
                                Log.d("FAILLLLED","FAILLLLED");

                            }
                        }
                        catch (JSONException e) {
                            Log.d("ERROR",e.getMessage());
                        }
                    }
                });
            }
        };
    }

    @SuppressLint("WrongConstant")
    private void ShowNotification(String Message, String name)
    {
        Intent notificationIntent = new Intent(getApplicationContext(), MainUser.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, notificationIntent, 0);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Stock Market", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationChannel.setSound(null,null );
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(uri)
                .setContentIntent(pendingIntent)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(Message)
                .setContentText(name);

        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
    }

}
