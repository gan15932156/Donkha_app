package com.example.donkha_app.Helper;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.GetChars;
import android.util.Log;
import android.widget.Toast;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.donkha_app.GetterSetter.PreferenceUtils;
import com.example.donkha_app.GetterSetter.Statement;
import com.example.donkha_app.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MyWork extends Worker {
    private String account_id;
    private Set<String> newset ;
    private Set<String> tran_id_set ;
    public MyWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        account_id = getInputData().getString(Constants.KEY_SERVICE_ACCOUNT_ID);
        String service_type = getInputData().getString(String.valueOf(Constants.KEY_START_SERVICE));
        if(service_type.equals("check_statement")){
            boolean relll =  check_statement_confirm(account_id);
            if(relll){
                return Result.SUCCESS;
            }
            else{
                return Result.RETRY;
            }
        }
        else{
            boolean relll2 =  check_tranfer_money(account_id);
            if(relll2){
                return Result.SUCCESS;
            }
            else{
                return Result.RETRY;
            }
        }
    }
    private void StartNewRequest_check_tranfer(String account_id)
    {
        Data data = new Data.Builder()
                .putString(Constants.KEY_START_SERVICE,"check_tranfer_money")
                .putString(Constants.KEY_SERVICE_ACCOUNT_ID, account_id)
                .build();

        OneTimeWorkRequest re_check = new OneTimeWorkRequest.Builder(MyWork.class).
                setInitialDelay(2, TimeUnit.SECONDS).
                setInputData(data).
                addTag("check_tranfer_money").build();
        WorkManager.getInstance().enqueue(re_check);
    }
    private void StartNewRequest_check_statement(String account_id)
    {
        Data data = new Data.Builder()
                .putString(Constants.KEY_START_SERVICE,"check_statement")
                .putString(Constants.KEY_SERVICE_ACCOUNT_ID, account_id)
                .build();

        OneTimeWorkRequest re_check = new OneTimeWorkRequest.Builder(MyWork.class).
                setInitialDelay(2, TimeUnit.SECONDS).
                setInputData(data).
                addTag("check_statement").build();
        WorkManager.getInstance().enqueue(re_check);
    }
    private Boolean check_statement_confirm(String account_id){
        String url = "http://18.140.49.199/Donkha/Service_app/check_statement_confirm";
        Boolean rel = false;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("account_id",account_id));

        String response = WebSevConnect.getHttpPost(url,params,getApplicationContext());
        try {
            JSONObject obj = new JSONObject(response);
            if(obj.getString("relsult_check").equals("0")){
                //Log.d("FAILLLLLLLLLL","fail");
                StartNewRequest_check_statement(account_id);
                rel = false;
            }
            else{
                //Log.d("SUCESSSSSSSSSSSS","SUCCESSSSSSSSSSS");
                JSONObject jsonAccount = obj.getJSONObject("account");
                ShowNotification("ยืนยันรายการสำเร็จ","ยืนยันรายการเลขที่ธุรกรรม "+jsonAccount.getString("trans_id"));
                WorkManager.getInstance().cancelAllWorkByTag("check_statement");
                Log.d("SUCESSSSSSSSSSSS","STOP check_statement");
                rel = true;
            }
        }
        catch (JSONException e) {
            Log.d("ERROR",e.getMessage());
        }
        return rel;
    }

    private Boolean check_tranfer_money(String account_id){
        String url = "http://18.140.49.199/Donkha/Service_app/check_received_tranfer_money";
        Boolean rel = false;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("account_id",account_id));


        tran_id_set = PreferenceUtils.getTran_id(getApplicationContext());
        for (String tt : tran_id_set) {
            //Log.d("SET STRINGGGGGGGGGGG",tt);
            params.add(new BasicNameValuePair("tran_id[]",tt));
        }

        String response = WebSevConnect.getHttpPost(url,params,getApplicationContext());
        try {
            JSONObject obj = new JSONObject(response);
            if(!obj.getBoolean("error")){
                //Log.d("SUCESSSSSSSSSSSS","SUCCESSSSSSSSSSS");
                JSONObject jsonAccount = obj.getJSONObject("check");


                newset = PreferenceUtils.getTran_id(getApplicationContext());
                newset.add(jsonAccount.getString("trans_id"));

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.remove(Constants.KEY_TRAN_ID);
                prefsEditor.commit();

                PreferenceUtils.saveTran_id(newset,getApplicationContext());
                Log.d("SUCESSSSSSSSSSSS",newset.toString());
                ShowNotification("ได้รับการโอนเงินจากบัญชี "+jsonAccount.getString("account_id_tranfer"),"จำนวนเงิน "+jsonAccount.getString("trans_money")+" บาท");
                WorkManager.getInstance().cancelAllWorkByTag("check_tranfer_money");
                Log.d("SUCESSSSSSSSSSSS","STOP check_tranfer_money");
                StartNewRequest_check_tranfer(account_id);
                //Log.d("SUCESSSSSSSSSSSS",jsonAccount+"");
                rel = true;

            }
            else{
                //Log.d("FAILLLLLLLLLL","fail");
                StartNewRequest_check_tranfer(account_id);
                rel = false;
            }
        }
        catch (JSONException e) {
            Log.d("ERROR",e.getMessage());
        }
        return rel;
    }

    @SuppressLint("WrongConstant")
    private void ShowNotification(String Message, String name)
    {
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
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(Message)
                .setContentText(name);

        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
    }
}
