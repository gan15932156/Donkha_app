package com.example.donkha_app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donkha_app.GetterSetter.PreferenceUtils;
import com.example.donkha_app.GetterSetter.Statement;
import com.example.donkha_app.MainActivity;
import com.example.donkha_app.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private TextView txt;
    private Button btn_deposit,btn_withdraw,btn_tran,btn_tran_request;
    private RecyclerView mRecycerView;

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
            show_statement_today(account_id);
        }
        else{
            Toast.makeText(mContex, "ไม่สามารถแสดงรายการได้", Toast.LENGTH_SHORT).show();
        }
        btn_tran_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(mContex,TranferActivity.class));
            }
        });
        btn_tran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account_id != null) {
                    if(btn_tranfer_status){
                        txt.setText("รายการรายวัน");
                        Toast.makeText(mContex, "statement today", Toast.LENGTH_SHORT).show();
                        show_statement_today(account_id);
                        btn_tranfer_status = false;
                    }
                    else{
                        txt.setText("รายการโอนรายวัน");
                        Toast.makeText(mContex, "btn_tran today", Toast.LENGTH_SHORT).show();
                        show_tranfer_today(account_id);
                        btn_tranfer_status = true ;
                        btn_deposit_status = false;
                        btn_withdraw_status = false;
                    }
                }
                else{
                    Toast.makeText(mContex, "ไม่สามารถแสดงรายการได้", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account_id != null) {
                    if(btn_withdraw_status){
                        txt.setText("รายการรายวัน");
                        Toast.makeText(mContex, "statement today", Toast.LENGTH_SHORT).show();
                        show_statement_today(account_id);
                        btn_withdraw_status = false ;
                    }
                    else{
                        txt.setText("รายการถอนรายวัน");
                        Toast.makeText(mContex, "btn_withdraw today", Toast.LENGTH_SHORT).show();
                        show_withdraw_today(account_id);
                        btn_withdraw_status = true;
                        btn_deposit_status = false;
                        btn_tranfer_status = false;
                    }
                }
                else{
                    Toast.makeText(mContex, "ไม่สามารถแสดงรายการได้", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account_id != null){
                    if(btn_deposit_status){
                        txt.setText("รายการรายวัน");
                        Toast.makeText(mContex, "statement today", Toast.LENGTH_SHORT).show();
                        show_statement_today(account_id);
                        btn_deposit_status = false;
                    }
                    else{
                        txt.setText("รายการฝากรายวัน");
                        Toast.makeText(mContex, "btn_deposit today", Toast.LENGTH_SHORT).show();
                        show_deposit_today(account_id);
                        btn_deposit_status = true ;
                        btn_withdraw_status = false;
                        btn_tranfer_status = false;
                    }
                }
                else{
                    Toast.makeText(mContex, "ไม่สามารถแสดงรายการได้", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
    private void init(View view){
        mContex = getContext();
        btn_deposit_status = false ;
        btn_tranfer_status = false ;
        btn_withdraw_status = false ;
        account_id = PreferenceUtils.getAccount_id(mContex);
        txt = view.findViewById(R.id.home_txt_transaction);
        btn_deposit = view.findViewById(R.id.home_btn_deposit);
        btn_withdraw = view.findViewById(R.id.home_btn_withdraw);
        btn_tran = view.findViewById(R.id.home_btn_tranfer_money);
        btn_tran_request = view.findViewById(R.id.home_btn_tranfer_money_request);
        mRecycerView = view.findViewById(R.id.home_recyclerview);
    }
    private void show_deposit_today(String account_id){

    }
    private void show_withdraw_today(String account_id){

    }
    private void show_tranfer_today(String account_id){

    }
    private void show_statement_today(String account){

    }

}
