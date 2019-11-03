package com.example.donkha_app.Helper;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.donkha_app.MainUser;
import com.example.donkha_app.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MyJobIntentService extends JobIntentService {
    private static final String TAG = "MyJobIntentService";
    /**
     * Unique job ID for this service.
     */
    private static final int JOB_ID = 2;
    final Handler mHandler = new Handler();
    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, MyJobIntentService.class, JOB_ID, intent);
    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        boolean relllll = false;
        int maxCount = intent.getIntExtra("maxCountValue", -1);
        /**
         * Suppose we want to print 1 to 1000 number with one-second interval, Each task will take time 1 sec, So here now sleeping thread for one second.
         */
        for (int i = 0; i < maxCount; i++) {
            Log.d(TAG, "onHandleWork: The number is: " + i);
            ShowNotification("ควยไร",i+"");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //JSONObject rell = Helper.check_tranfer(MyJobIntentService.this);

       /* Log.d("dasdasdasd","555555555555555555555555555");
        int count = 1;
        do {
            try {
                Thread.sleep(5000);

                relllll = Helper.check_tranfer(MyJobIntentService.this);
                ShowNotification("ควยไร","ยากชิยหายเลยสัสเอ้ยยยยย");
                showToast(relllll+" ");
                count++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (relllll);*/
    }

    @Override
    public void onCreate() {
        super.onCreate();
        showToast("Job Execution Started");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        showToast("Job Execution Finished");
    }

    void showToast(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyJobIntentService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
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
