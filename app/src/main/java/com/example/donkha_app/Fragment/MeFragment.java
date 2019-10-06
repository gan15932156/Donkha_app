package com.example.donkha_app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.donkha_app.GetterSetter.Member;
import com.example.donkha_app.GetterSetter.PreferenceUtils;
import com.example.donkha_app.Helper.Helper;
import com.example.donkha_app.Helper.WebSevConnect;
import com.example.donkha_app.MainActivity;
import com.example.donkha_app.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MeFragment extends Fragment {
    private Button btn_logout;
    private Context mContext;
    private TextInputEditText txt_name,txt_id_card,txt_stu_code,txt_edu_level,txt_job,txt_birth_date,txt_address;
    private ImageView img_profile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fregment_me, container, false);
        initView(view);
        PreferenceUtils utils = new PreferenceUtils();
        if (utils.getUsername(mContext) == null ){
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtils.savePassword(null, mContext);
                PreferenceUtils.saveUsername(null, mContext);
                PreferenceUtils.saveMember_id(null, mContext);
                PreferenceUtils.saveAccount_id(null, mContext);
                Intent intent = new Intent(mContext,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        get_member_detail(PreferenceUtils.getMember_id(mContext));
        return view;
    }

    private void initView(View view){
        mContext = getContext();
        btn_logout = view.findViewById(R.id.btn_logout);
        txt_name =(TextInputEditText) view.findViewById(R.id.edit_name);
        txt_id_card = (TextInputEditText)view.findViewById(R.id.edit_id_card);
        txt_stu_code = (TextInputEditText)view.findViewById(R.id.edit_std_code);
        txt_edu_level = (TextInputEditText)view.findViewById(R.id.edit_edu_level);
        txt_job = (TextInputEditText)view.findViewById(R.id.edit_job);
        txt_birth_date =(TextInputEditText) view.findViewById(R.id.edit_birth);
        txt_address = (TextInputEditText)view.findViewById(R.id.edit_address);
        img_profile = view.findViewById(R.id.freg_me_img);
    }
    private void get_member_detail(String member_id){
        String url = "http://18.140.49.199/Donkha/Service_app/get_member_detail";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("member_id",member_id));

        String response = WebSevConnect.getHttpPost(url,params,mContext);
        try {
            JSONObject obj = new JSONObject(response);
            if(!obj.getBoolean("error")){

                JSONObject jsonMember = obj.getJSONObject("member");
                Member member = new Member(
                        jsonMember.getString("member_id"),
                        jsonMember.getString("level_id"),
                        jsonMember.getString("edu_id"),
                        jsonMember.getString("std_code"),
                        jsonMember.getString("member_id_card"),
                        jsonMember.getString("member_name"),
                        jsonMember.getString("member_birth_date"),
                        jsonMember.getString("member_yofadmis"),
                        jsonMember.getString("address"),
                        jsonMember.getString("phone_number"),
                        jsonMember.getString("member_pic"),
                        jsonMember.getString("member_signa_pic"),
                        jsonMember.getString("member_regis_date"),
                        jsonMember.getString("member_title"),
                        jsonMember.getString("zipcode"),
                        jsonMember.getString("DISTRICT_NAME"),
                        jsonMember.getString("AMPHUR_NAME"),
                        jsonMember.getString("PROVINCE_NAME"),
                        jsonMember.getString("job_name")
                );
                String edu = ((member.getEdu_id().equals("0")) ? "ไม่มี" : "ม."+member.getEdu_id());
                String std_code = ((member.getStd_code().equals("0")) ? "ไม่มี" : member.getStd_code());
                Glide.with(mContext)
                        .load(member.getMember_pic())
                        .fitCenter()
                        .into(img_profile);
                txt_name.setText(member.getMember_title()+""+member.getMember_name());
                txt_id_card.setText(member.getMember_id_card());
                txt_stu_code.setText(std_code);
                txt_edu_level.setText(edu);
                txt_job.setText(member.getJob_name());
                txt_birth_date.setText(Helper.dateThai(member.getMember_birth_date()));
                txt_address.setText(member.getAddress()+" ต."+member.getDISTRICT_NAME()+" อ."+member.getAMPHUR_NAME()+" จ."+member.getPROVINCE_NAME()+" รหัสไปรษณีย์ "+member.getZipcode());

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
