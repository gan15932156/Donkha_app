package com.example.donkha_app;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.donkha_app.GetterSetter.PreferenceUtils;
import com.example.donkha_app.Helper.WebSevConnect;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText edit_username,edit_password;
    private Button btn_submit;
    private LinearLayout linearLayout;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.dialog_forget_password);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.55);
        dialog.getWindow().setLayout(width,height);

        linearLayout = findViewById(R.id.view_id);
        edit_password = findViewById(R.id.edit_password);
        edit_username = findViewById(R.id.edit_username);
        btn_submit = findViewById(R.id.btn_send);

        PreferenceUtils utils = new PreferenceUtils();
        if (utils.getUsername(this) != null ){
            Intent intent = new Intent(this,MainUser.class);
            finishAffinity();
            startActivity(intent);
            this.finish();
        }
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSubmit()){
                    login();
                }
            }
        });
    }
    public boolean isSubmit(){
        if(edit_username.getText().toString().isEmpty()){
            Toast.makeText(this, "กรุณากรอกชื่อผู้ใช้", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(edit_password.getText().toString().isEmpty()){
            Toast.makeText(this, "กรุณากรอกรหัสผ่าน", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!edit_username.getText().toString().isEmpty() || !edit_password.getText().toString().isEmpty()){
            return true;
        }
        return false;
    }

    public void login(){
        String url = "http://18.140.49.199/Donkha/Service_app/login_check_app";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Username", edit_username.getText().toString()));
        params.add(new BasicNameValuePair("Password", edit_password.getText().toString()));

        String url2 = "http://18.140.49.199/Donkha/Service_app/check_account";
        List<NameValuePair> params2 = new ArrayList<NameValuePair>();

        String response = WebSevConnect.getHttpPost(url,params,MainActivity.this);
        try {
            JSONObject obj = new JSONObject(response);
            if(!obj.getBoolean("error")){
                JSONObject userJson = obj.getJSONObject("user");

                PreferenceUtils.saveUsername(userJson.getString("username"),this);
                PreferenceUtils.savePassword(userJson.getString("password"),this);
                PreferenceUtils.saveMember_id(userJson.getString("id"),this);

                params2.add(new BasicNameValuePair("member_id", userJson.getString("id")));
                String response2 = WebSevConnect.getHttpPost(url2,params2,MainActivity.this);
                JSONObject obj2 = new JSONObject(response2);
                if(!obj2.getBoolean("error")){
                    JSONObject AccountJson = obj2.getJSONObject("account_balance");
                    PreferenceUtils.saveAccount_id(AccountJson.getString("account_id"),this);
                }
                else{
                    Toast.makeText(getApplicationContext(), obj2.getString("message"), Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(MainActivity.this,MainUser.class);
                this.finishAffinity();
                startActivity(intent);

            }
            else{
                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) { e.printStackTrace(); }
    }

    public void forgotpass(View v){
        final Spinner spn = dialog.findViewById(R.id.spn_forgot);
        final EditText edit_forgot = dialog.findViewById(R.id.edit_dialog_forgot_text);
        final Button btn_forgot = dialog.findViewById(R.id.btn_forgot_send);
        final EditText edit_dialog_username = dialog.findViewById(R.id.edit_dialog_forgot_username);

        edit_forgot.setText(null);
        edit_dialog_username.setText(null);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spn_forgot, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adapter);

        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spn.getSelectedItem().equals("นักเรียน")){
                    edit_forgot.setHint("รหัสนักเรียน");
                    Toast.makeText(MainActivity.this, "กรุณากรอกรหัสนักเรียน", Toast.LENGTH_SHORT).show();
                }
                else{
                    edit_forgot.setHint("เลขบัตรประชาชน");
                    Toast.makeText(MainActivity.this, "กรุณากรอกเลขบัตรประชาชน", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_forgot.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this, "กรุณากรอก", Toast.LENGTH_SHORT).show();
                }
                if(edit_dialog_username.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this, "กรุณากรอกชื่อผู้ใช้", Toast.LENGTH_SHORT).show();
                }
                if(!edit_forgot.getText().toString().trim().isEmpty() ||
                        !edit_dialog_username.getText().toString().trim().isEmpty() ||
                        !spn.getSelectedItem().toString().isEmpty())
                {
                    send_forgot(edit_forgot.getText().toString().trim(),spn.getSelectedItem().toString(),edit_dialog_username.getText().toString().trim());
                }
            }
        });
        dialog.show();
    }
    public void send_forgot(String forgot,String state,String username){
        String url = "http://18.140.49.199/Donkha/Service_app/forgot_password";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Forgot", forgot));
        params.add(new BasicNameValuePair("State", state));
        params.add(new BasicNameValuePair("Username", username));

        String response = WebSevConnect.getHttpPost(url,params,MainActivity.this);
        try {
            JSONObject obj = new JSONObject(response);
            if(!obj.getBoolean("error")){
                JSONObject forgotJson = obj.getJSONObject("forgot");

                Snackbar snackbar =  Snackbar.make(linearLayout, "รหัสผ่าน "+forgotJson.getString("password"), Snackbar.LENGTH_LONG);

                View snackbarview = snackbar.getView();
                snackbarview.setBackgroundColor(Color.parseColor("#36a1ff"));
                snackbar.show();
                dialog.dismiss();
            }
            else{
                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) { e.printStackTrace(); }
    }
}
