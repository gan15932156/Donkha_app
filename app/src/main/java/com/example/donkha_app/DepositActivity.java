package com.example.donkha_app;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.bumptech.glide.Glide;
import com.example.donkha_app.GetterSetter.PreferenceUtils;
import com.example.donkha_app.Helper.Constants;
import com.example.donkha_app.Helper.Helper;
import com.example.donkha_app.Helper.MyWork;
import com.example.donkha_app.Helper.WebSevConnect;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DepositActivity extends AppCompatActivity {
    private Button btn_submit;
    private TextView txt_ac_balance;
    private ImageView img_singature;
    private TextInputEditText txt_ac_code,txt_ac_name,txt_deposit_money,txt_total_money;

    private Double deposit_money,total_money,ac_balance;
    private String account_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        initView();

        account_id = PreferenceUtils.getAccount_id(DepositActivity.this);

        deposit_money = 0.0;
        total_money = 0.0;
        ac_balance = 0.0;

        get_account();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txt_deposit_money.getText().toString().isEmpty() &&
                        !txt_ac_balance.getText().toString().isEmpty() &&
                        !txt_ac_code.getText().toString().isEmpty() &&
                        !txt_ac_name.getText().toString().isEmpty()){
                    insert_deposit();
                    //Toast.makeText(DepositActivity.this, PreferenceUtils.getAccount_id(DepositActivity.this)+" "+txt_deposit_money.getText()+" "+txt_total_money.getText(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(DepositActivity.this, "ไม่สามารถทำรายการได้", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_deposit_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    if(!ac_balance.equals(0.0)){
                        deposit_money = Double.parseDouble(s.toString());
                        total_money = deposit_money + ac_balance;
                        txt_total_money.setText(total_money.toString());
                    }
                }
                else{ txt_total_money.setText(null); }
            }
        });
    }
    private void initView(){
        btn_submit = findViewById(R.id.deposit_btn_submit);
        txt_ac_balance = findViewById(R.id.deposit_txt_ac_balance);
        img_singature = findViewById(R.id.deposit_img_singature);
        txt_ac_code =(TextInputEditText) findViewById(R.id.deposit_edit_ac_code);
        txt_ac_name = (TextInputEditText) findViewById(R.id.deposit_edit_ac_name);
        txt_deposit_money = (TextInputEditText) findViewById(R.id.deposit_edit_money);
        txt_total_money = (TextInputEditText) findViewById(R.id.deposit_edit_total_money);
    }
    private void insert_deposit(){
        String url = "http://18.140.49.199/Donkha/Service_app/receive_deposit_insert";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("account_id",account_id));
        params.add(new BasicNameValuePair("deposit_money",txt_deposit_money.getText().toString().trim()));
        params.add(new BasicNameValuePair("new_balance",txt_total_money.getText().toString().trim()));

        String response = WebSevConnect.getHttpPost(url,params,DepositActivity.this);
        try {
            JSONObject obj = new JSONObject(response);
            if(!obj.getBoolean("error")){
                Toast.makeText(DepositActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();

                Data data = new Data.Builder()
                        .putString(Constants.KEY_START_SERVICE,"check_statement")
                        .putString(Constants.KEY_SERVICE_ACCOUNT_ID, PreferenceUtils.getAccount_id(DepositActivity.this))
                        .build();

                final OneTimeWorkRequest check = new OneTimeWorkRequest.Builder(MyWork.class).
                        setInitialDelay(2, TimeUnit.SECONDS).
                        setInputData(data).
                        addTag("check_statement").build();
                WorkManager.getInstance().enqueue(check);

                this.finishAffinity();
                startActivity(new Intent(DepositActivity.this,MainUser.class));
            }
            else{
                Toast.makeText(DepositActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) {
            Toast.makeText(DepositActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.user_home:
                this.finishAffinity();
                startActivity(new Intent(this, MainUser.class));
                return true;
        }
        return  super.onOptionsItemSelected(item);
    }
    private void get_account(){
        if(account_id == null){
            Toast.makeText(this, "ไม่พบบัญชี ไม่สามารถทำรายการได้", Toast.LENGTH_LONG).show();
        }
        else{
            String url = "http://18.140.49.199/Donkha/Service_app/select_account";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("account_id",account_id));

            String response = WebSevConnect.getHttpPost(url,params,DepositActivity.this);
            try {
                JSONObject obj = new JSONObject(response);
                if(!obj.getBoolean("error")){
                    if(obj.getString("result_check").equals("0")){
                        Toast.makeText(this, "ไม่สามารถทำรายการได้ เนื่องจากรายการก่อนหน้ายังไม่ได้ยืนยัน", Toast.LENGTH_LONG).show();
                    }
                    else{
                        JSONObject jsonAccount = obj.getJSONObject("account");
                        ac_balance = jsonAccount.getDouble("account_balance");
                        txt_ac_balance.setText(Helper.customFormat("###,###.###",jsonAccount.getDouble("account_balance")));
                        txt_ac_code.setText(jsonAccount.getString("account_id"));
                        txt_ac_name.setText(jsonAccount.getString("account_name"));
                        Glide.with(DepositActivity.this)
                                .load(jsonAccount.getString("member_signa_pic"))
                                .fitCenter()
                                .into(img_singature);
                    }

                }
                else{
                    Toast.makeText(DepositActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e) {
                Toast.makeText(DepositActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }
}
