package com.example.donkha_app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donkha_app.Adapter.StatementRecyclerAdapter;
import com.example.donkha_app.GetterSetter.PreferenceUtils;
import com.example.donkha_app.GetterSetter.Statement;
import com.example.donkha_app.Helper.Helper;
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

public class StatementFragment extends Fragment {
    private Spinner spn;
    private RecyclerView mRecyclerView;
    private StatementRecyclerAdapter mStatementAdapter;
    private ArrayList<Statement> mStatementList;
    private Context mContext;
    private TextView txt_account_balance;
    private String account_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        PreferenceUtils utils = new PreferenceUtils();
        View view = inflater.inflate(R.layout.fregment_statement, container, false);
        if (utils.getUsername(mContext) == null){
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        account_id = PreferenceUtils.getAccount_id(mContext);
        txt_account_balance = view.findViewById(R.id.txt_balance_money);
        spn = view.findViewById(R.id.spn_statement_recyclerview);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.spn_action_statement, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adapter);

        mRecyclerView = view.findViewById(R.id.statement_recycler_view_horizontal);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mStatementList = new ArrayList<>();

        request_statement();
        setAccount_balance();
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String action = spn.getSelectedItem().toString();
                if(!action.equals("เลือกรายการ")){
                    mRecyclerView.setAdapter(null);
                    mStatementList.clear();

                    String action_request ;
                    if(action.equals("ทั้งหมด")){ action_request = "all"; }
                    else if(action.equals("ฝาก")){ action_request = "deposit"; }
                    else if(action.equals("ถอน")){ action_request = "withdraw"; }
                    else{ action_request = "tranfer_money"; }
                    getShow_Filter(action_request);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        return view;
    }
    public void getShow_Filter(String action){
        String url = "http://18.140.49.199/Donkha/Service_app/get_filter_statement";
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        if(account_id != null){
            params.add(new BasicNameValuePair("account_id", account_id));
            params.add(new BasicNameValuePair("action", action));
            String response = WebSevConnect.getHttpPost(url,params,mContext);
            try {
                JSONObject obj = new JSONObject(response);
                if(!obj.getBoolean("error")){

                    JSONArray jsonArraySt = obj.getJSONArray("statement");
                    for(int i = 0 ; i < jsonArraySt.length();i++){
                        JSONObject st = jsonArraySt.getJSONObject(i);
                        mStatementList.add(new Statement(
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
                    mStatementAdapter = new StatementRecyclerAdapter(mContext,mStatementList);
                    mRecyclerView.setAdapter(mStatementAdapter);
                }
                else{
                    Toast.makeText(mContext, obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(mContext, "ไม่พบบัญชี", Toast.LENGTH_SHORT).show();
        }


    }
    public void setAccount_balance(){
        String url = "http://18.140.49.199/Donkha/Service_app/get_account_balance";
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        if(account_id != null){
            params.add(new BasicNameValuePair("account_id",account_id));
            String response = WebSevConnect.getHttpPost(url,params,mContext);
            try {
                JSONObject obj = new JSONObject(response);
                if(!obj.getBoolean("error")){

                    JSONObject jsonAccount = obj.getJSONObject("account_balance");
                    PreferenceUtils.saveAccount_id(jsonAccount.getString("account_id"),mContext);
                    txt_account_balance.setText("ยอดเงินคงเหลือ "+ Helper.customFormat("###,###.###",jsonAccount.getDouble("balance"))+" บาท");
                }
                else{
                    Toast.makeText(mContext, obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(mContext, "ไม่พบบัญชี", Toast.LENGTH_SHORT).show();
        }

    }
    public void request_statement(){
        String url = "http://18.140.49.199/Donkha/Service_app/get_statement";
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        if(account_id != null){
            params.add(new BasicNameValuePair("account_id", account_id));
            String response = WebSevConnect.getHttpPost(url,params,mContext);
            try {
                JSONObject obj = new JSONObject(response);
                Log.d("TAG",obj+"");
                if(!obj.getBoolean("error")){

                    JSONArray jsonArraySt = obj.getJSONArray("statement");
                    for(int i = 0 ; i < jsonArraySt.length();i++){
                        JSONObject st = jsonArraySt.getJSONObject(i);
                        mStatementList.add(new Statement(
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
                    mStatementAdapter = new StatementRecyclerAdapter(mContext,mStatementList);
                    mRecyclerView.setAdapter(mStatementAdapter);
                }
                else{
                    Toast.makeText(mContext, obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(mContext, "ไม่พบบัญชี", Toast.LENGTH_SHORT).show();
        }

    }
}
