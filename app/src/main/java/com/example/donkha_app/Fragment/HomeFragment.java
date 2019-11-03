package com.example.donkha_app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donkha_app.Adapter.StatementRecyclerAdapter;
import com.example.donkha_app.GetterSetter.PreferenceUtils;
import com.example.donkha_app.GetterSetter.Statement;
import com.example.donkha_app.Helper.WebSevConnect;
import com.example.donkha_app.MainActivity;
import com.example.donkha_app.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private TextView txt;
    private Button btn_deposit,btn_withdraw,btn_tran;
    private RecyclerView mRecycerView;
    private StatementRecyclerAdapter statementRecyclerAdapter;

    private String account_id;
    private Context mContex;
    private ArrayList<Statement> arraylist_today;
    private ArrayList<Statement> arrayList_selected;
    private boolean btn_deposit_status,btn_withdraw_status,btn_tranfer_status;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fregment_home, container, false);

        PreferenceUtils utils = new PreferenceUtils();
        if (utils.getUsername(getContext()) == null ){
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        init(view);
        if(account_id != null){
            show_statement_today(account_id,"http://18.140.49.199/Donkha/Service_app/select_statement_today");
        }
        else{
            txt.setTextColor(Color.RED);
            txt.setText("ไม่สามารถแสดงรายการได้");
        }
        btn_tran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account_id != null) {
                    if(btn_tranfer_status){
                        mRecycerView.setAdapter(null);
                        txt.setTextColor(getResources().getColor(R.color.black));
                        txt.setText("รายการรายวัน");
                        statementRecyclerAdapter = new StatementRecyclerAdapter(mContex,arraylist_today);
                        mRecycerView.setAdapter(statementRecyclerAdapter);
                        btn_tranfer_status = false;
                    }
                    else{
                        mRecycerView.setAdapter(null);
                        show_tranfer_today(account_id,"http://18.140.49.199/Donkha/Service_app/select_tranfer_today");
                        btn_tranfer_status = true ;
                        btn_deposit_status = false;
                        btn_withdraw_status = false;
                    }
                }
                else{
                    txt.setTextColor(Color.RED);
                    txt.setText("ไม่สามารถแสดงรายการได้");
                }
            }
        });
        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account_id != null) {
                    if(btn_withdraw_status){
                        mRecycerView.setAdapter(null);
                        txt.setTextColor(getResources().getColor(R.color.black));
                        txt.setText("รายการรายวัน");
                        statementRecyclerAdapter = new StatementRecyclerAdapter(mContex,arraylist_today);
                        mRecycerView.setAdapter(statementRecyclerAdapter);
                        btn_withdraw_status = false ;
                    }
                    else{
                        mRecycerView.setAdapter(null);
                        show_withdraw_today(account_id,"http://18.140.49.199/Donkha/Service_app/select_withdraw_today");
                        btn_withdraw_status = true;
                        btn_deposit_status = false;
                        btn_tranfer_status = false;
                    }
                }
                else{
                    txt.setTextColor(Color.RED);
                    txt.setText("ไม่สามารถแสดงรายการได้");
                }
            }
        });
        btn_deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account_id != null){
                    if(btn_deposit_status){
                        mRecycerView.setAdapter(null);
                        txt.setTextColor(getResources().getColor(R.color.black));
                        txt.setText("รายการรายวัน");
                        statementRecyclerAdapter = new StatementRecyclerAdapter(mContex,arraylist_today);
                        mRecycerView.setAdapter(statementRecyclerAdapter);
                        btn_deposit_status = false;
                    }
                    else{
                        mRecycerView.setAdapter(null);
                        show_deposit_today(account_id,"http://18.140.49.199/Donkha/Service_app/select_deposit_today");
                        btn_deposit_status = true ;
                        btn_withdraw_status = false;
                        btn_tranfer_status = false;
                    }
                }
                else{
                    txt.setTextColor(Color.RED);
                    txt.setText("ไม่สามารถแสดงรายการได้");
                }
            }
        });
        return view;
    }
    private void init(View view){
        mContex = getContext();
        arraylist_today = new ArrayList<>();
        arrayList_selected = new ArrayList<>();
        btn_deposit_status = false ;
        btn_tranfer_status = false ;
        btn_withdraw_status = false ;
        account_id = PreferenceUtils.getAccount_id(mContex);
        txt = view.findViewById(R.id.home_txt_transaction);
        btn_deposit = view.findViewById(R.id.home_btn_deposit);
        btn_withdraw = view.findViewById(R.id.home_btn_withdraw);
        btn_tran = view.findViewById(R.id.home_btn_tranfer_money);
        mRecycerView = view.findViewById(R.id.home_recyclerview);
        mRecycerView.setHasFixedSize(true);
        mRecycerView.setLayoutManager(new LinearLayoutManager(mContex));
    }
    private void show_deposit_today(String account_id,String url){
        arrayList_selected.clear();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("account_id",account_id));

        String response = WebSevConnect.getHttpPost(url,params, mContex);
        try {
            JSONObject obj = new JSONObject(response);
            if(!obj.getBoolean("error")){
                txt.setText("รายการฝากรายวัน");
                JSONArray jsonArraySt = obj.getJSONArray("statement");
                for(int i = 0 ; i < jsonArraySt.length();i++){
                    JSONObject st = jsonArraySt.getJSONObject(i);
                    arrayList_selected.add(new Statement(
                            st.getString("account_id"),
                            st.getString("trans_id"),
                            st.getString("account_id"),
                            st.getString("staff_record_id"),
                            st.getString("action"),
                            st.getString("record_date"),
                            st.getString("record_time"),
                            st.getDouble("account_detail_balance"),
                            st.getDouble("trans_money"),
                            st.getString("account_id_tranfer")
                    ));
                }
                statementRecyclerAdapter = new StatementRecyclerAdapter(mContex,arrayList_selected);
                mRecycerView.setAdapter(statementRecyclerAdapter);
            }
            else{
                txt.setTextColor(Color.RED);
                txt.setText("ไม่สามารถแสดงรายการได้");
                //Toast.makeText(mContex, obj.getString("message"), Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) {
            Toast.makeText(mContex, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void show_withdraw_today(String account_id,String url){
        arrayList_selected.clear();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("account_id",account_id));

        String response = WebSevConnect.getHttpPost(url,params, mContex);
        try {
            JSONObject obj = new JSONObject(response);
            if(!obj.getBoolean("error")){
                JSONArray jsonArraySt = obj.getJSONArray("statement");
                txt.setText("รายการถอนรายวัน");
                for(int i = 0 ; i < jsonArraySt.length();i++){
                    JSONObject st = jsonArraySt.getJSONObject(i);
                    arrayList_selected.add(new Statement(
                            st.getString("account_id"),
                            st.getString("trans_id"),
                            st.getString("account_id"),
                            st.getString("staff_record_id"),
                            st.getString("action"),
                            st.getString("record_date"),
                            st.getString("record_time"),
                            st.getDouble("account_detail_balance"),
                            st.getDouble("trans_money"),
                            st.getString("account_id_tranfer")
                    ));
                }
                statementRecyclerAdapter = new StatementRecyclerAdapter(mContex,arrayList_selected);
                mRecycerView.setAdapter(statementRecyclerAdapter);
            }
            else{
                txt.setTextColor(Color.RED);
                txt.setText("ไม่สามารถแสดงรายการได้");
                //Toast.makeText(mContex, obj.getString("message"), Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) {
            Toast.makeText(mContex, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void show_tranfer_today(String account_id,String url){
        arrayList_selected.clear();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("account_id",account_id));

        String response = WebSevConnect.getHttpPost(url,params, mContex);
        try {
            JSONObject obj = new JSONObject(response);
            if(!obj.getBoolean("error")){
                txt.setText("รายการโอนรายวัน");
                JSONArray jsonArraySt = obj.getJSONArray("statement");
                for(int i = 0 ; i < jsonArraySt.length();i++){
                    JSONObject st = jsonArraySt.getJSONObject(i);
                    arrayList_selected.add(new Statement(
                            st.getString("account_id"),
                            st.getString("trans_id"),
                            st.getString("account_id"),
                            st.getString("staff_record_id"),
                            st.getString("action"),
                            st.getString("record_date"),
                            st.getString("record_time"),
                            st.getDouble("account_detail_balance"),
                            st.getDouble("trans_money"),
                            st.getString("account_id_tranfer")
                    ));
                }
                statementRecyclerAdapter = new StatementRecyclerAdapter(mContex,arrayList_selected);
                mRecycerView.setAdapter(statementRecyclerAdapter);
            }
            else{
                txt.setTextColor(Color.RED);
                txt.setText("ไม่สามารถแสดงรายการได้");
                //Toast.makeText(mContex, obj.getString("message"), Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) {
            Toast.makeText(mContex, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void show_statement_today(String account_id,String url){
        arraylist_today.clear();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("account_id",account_id));

        String response = WebSevConnect.getHttpPost(url,params, mContex);
        try {
            JSONObject obj = new JSONObject(response);
            if(!obj.getBoolean("error")){
                JSONArray jsonArraySt = obj.getJSONArray("statement");
                for(int i = 0 ; i < jsonArraySt.length();i++){
                    JSONObject st = jsonArraySt.getJSONObject(i);
                    arraylist_today.add(new Statement(
                            st.getString("account_id"),
                            st.getString("trans_id"),
                            st.getString("account_id"),
                            st.getString("staff_record_id"),
                            st.getString("action"),
                            st.getString("record_date"),
                            st.getString("record_time"),
                            st.getDouble("account_detail_balance"),
                            st.getDouble("trans_money"),
                            st.getString("account_id_tranfer")
                    ));
                }
                statementRecyclerAdapter = new StatementRecyclerAdapter(mContex,arraylist_today);
                mRecycerView.setAdapter(statementRecyclerAdapter);
            }
            else{
                txt.setTextColor(Color.RED);
                txt.setText("ไม่สามารถแสดงรายการได้");
                //Toast.makeText(mContex, obj.getString("message"), Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e) {
            Toast.makeText(mContex, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
