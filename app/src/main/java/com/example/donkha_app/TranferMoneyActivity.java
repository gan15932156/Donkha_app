package com.example.donkha_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.bumptech.glide.Glide;
import com.example.donkha_app.GetterSetter.PreferenceUtils;
import com.example.donkha_app.GetterSetter.Statement;
import com.example.donkha_app.Helper.Constants;
import com.example.donkha_app.Helper.Helper;
import com.example.donkha_app.Helper.MyWork;
import com.example.donkha_app.Helper.WebSevConnect;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TranferMoneyActivity extends AppCompatActivity {
    private Button btn_submit;
    private TextView txt_ac_balance;
    private ImageView img_singature;
    private TextInputEditText txt_ac_code,txt_ac_name,txt_tranfer_money,txt_total_money;
    private AutoCompleteTextView auto_ac_code;
    private List<String> account_list;
    private Context mContext;
    private String account_id;
    private Double tranfer_money,total_money,ac_balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranfer_money);
        init();
        get_account();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txt_tranfer_money.getText().toString().isEmpty() &&
                        !txt_ac_balance.getText().toString().isEmpty() &&
                        !txt_ac_code.getText().toString().isEmpty() &&
                        !txt_ac_name.getText().toString().isEmpty() &&
                        !auto_ac_code.getText().toString().isEmpty()){


                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setCancelable(false);
                        builder.setMessage("กรุณาตรวจสอบข้อมูลก่อนส่ง");
                        builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                insert_tranfer_money();
                                //Toast.makeText(mContext, txt_tranfer_money.getText()+" "+txt_total_money.getText(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                }
                else{
                    Toast.makeText(mContext, "ไม่สามารถทำรายการได้", Toast.LENGTH_SHORT).show();
                }
            }
        });
        auto_ac_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals(account_id)){
                    Toast.makeText(mContext, "เลขที่บัญชีซ้ำ", Toast.LENGTH_SHORT).show();
                    auto_ac_code.setText(null);
                }
            }
        });
        txt_tranfer_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    if(!ac_balance.equals(0.0)){
                        if(Double.parseDouble(s.toString()) > ac_balance){
                            txt_tranfer_money.setText(null);
                            total_money = 0.0;
                            Toast.makeText(mContext, "กรอกจำนวนเงินไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            tranfer_money = Double.parseDouble(s.toString());
                            total_money = ac_balance - tranfer_money;
                            txt_total_money.setText(total_money.toString());
                        }
                    }
                }
                else{ txt_total_money.setText(null); }
            }
        });
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
    private void init(){
        btn_submit = findViewById(R.id.tranfer_btn_submit);
        txt_ac_balance = findViewById(R.id.tranfer_txt_ac_balance);
        img_singature = findViewById(R.id.tranfer_img_singature);
        txt_ac_code = findViewById(R.id.tranfer_edit_ac_code);
        txt_ac_name = findViewById(R.id.tranfer_edit_ac_name);
        txt_tranfer_money = findViewById(R.id.tranfer_edit_tranfer_money);
        txt_total_money = findViewById(R.id.tranfer_edit_total);
        auto_ac_code = findViewById(R.id.tranfer_auto_txt_ac_code);
        account_list = new ArrayList<>();
        mContext = TranferMoneyActivity.this;
        account_id = PreferenceUtils.getAccount_id(mContext);
        tranfer_money = 0.0;
        total_money = 0.0;
        ac_balance = 0.0;

        get_autocomplete_account_id();

    }
    private void get_autocomplete_account_id(){
        String url = "http://18.140.49.199/Donkha/Service_app/select_autocomplete_account_id";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("account_id",account_id));
        String response = WebSevConnect.getHttpPost(url,params,mContext);
        try {
            JSONObject obj = new JSONObject(response);
            if(!obj.getBoolean("error")){
                JSONArray jsonArrayAc = obj.getJSONArray("account_id");
                for(int i = 0 ; i < jsonArrayAc.length();i++){
                    JSONObject ac = jsonArrayAc.getJSONObject(i);
                    account_list.add(ac.getString("account_id"));
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,android.R.id.text1,account_list);
                auto_ac_code.setAdapter(adapter);

            }
            else{
                Toast.makeText(mContext, obj.getString("message"), Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void get_account(){
        if(account_id == null){
            Toast.makeText(this, "ไม่พบบัญชี ไม่สามารถทำรายการได้", Toast.LENGTH_LONG).show();
        }
        else{
            String url = "http://18.140.49.199/Donkha/Service_app/select_account";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("account_id",account_id));

            String response = WebSevConnect.getHttpPost(url,params,mContext);
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
                        Glide.with(mContext)
                                .load(jsonAccount.getString("member_signa_pic"))
                                .fitCenter()
                                .into(img_singature);
                    }

                }
                else{
                    Toast.makeText(mContext, obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private void insert_tranfer_money(){
        String url = "http://18.140.49.199/Donkha/Service_app/receive_tranfer_insert";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("account_id",account_id));
        params.add(new BasicNameValuePair("tranfer_money",txt_tranfer_money.getText().toString().trim()));
        params.add(new BasicNameValuePair("new_balance",txt_total_money.getText().toString().trim()));
        params.add(new BasicNameValuePair("account_id_tranfer",auto_ac_code.getText().toString().trim()));

        String response = WebSevConnect.getHttpPost(url,params,mContext);
        try {
            JSONObject obj = new JSONObject(response);
            if(!obj.getBoolean("error")){
                Toast.makeText(mContext, obj.getString("message"), Toast.LENGTH_LONG).show();

                this.finishAffinity();
                startActivity(new Intent(mContext,MainUser.class));
            }
            else{
                Toast.makeText(mContext, obj.getString("message"), Toast.LENGTH_SHORT).show();
                Log.d("tagggggg",obj.getString("message"));
            }
        }
        catch (JSONException e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
