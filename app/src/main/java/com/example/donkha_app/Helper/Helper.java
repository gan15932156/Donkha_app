package com.example.donkha_app.Helper;

import android.content.Context;
import android.util.Log;

import com.example.donkha_app.GetterSetter.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Helper {
    public Helper(){}

    public static String dateThai(String strDate) {
        String Months[] = {
                "ม.ค", "ก.พ", "มี.ค", "เม.ย",
                "พ.ค", "มิ.ย", "ก.ค", "ส.ค",
                "ก.ย", "ต.ค", "พ.ย", "ธ.ค"};
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int year=0,month=0,day=0;
        try {
            Date date = df.parse(strDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);

            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DATE);

        }
        catch (ParseException e) { e.printStackTrace(); }
        return String.format("%s %s %s", day,Months[month],year+543);
    }

    public static String customFormat(String pattern, double value) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String output = myFormatter.format(value);
        return output;
    }

    public static boolean check_tranfer(Context context){
        String url = "http://18.140.49.199/Donkha/Service_app/check_received_tranfer_money";
        boolean responseboo = false;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("account_id", PreferenceUtils.getAccount_id(context)));
        JSONObject obj = null;
        String response = WebSevConnect.getHttpPost(url,params,context);
        try {
          obj = new JSONObject(response);
          Log.d("Helperrrrr",obj+"");
          responseboo = obj.getBoolean("error");
            /*if(!obj.getBoolean("error")){
                Log.d("SUCCESSS","SUCCESSS");

                JSONObject jsonAccount = obj.getJSONObject("check");


            }
            else{
                Log.d("FAILLLLED","FAILLLLED");

            }*/
        }
        catch (JSONException e) {
            Log.d("ERROR",e.getMessage());
        }
        return responseboo;
    }
}
