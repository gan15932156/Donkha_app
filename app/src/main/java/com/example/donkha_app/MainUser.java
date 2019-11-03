package com.example.donkha_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.donkha_app.Fragment.HomeFragment;
import com.example.donkha_app.Fragment.MeFragment;
import com.example.donkha_app.Fragment.StatementFragment;
import com.example.donkha_app.GetterSetter.PreferenceUtils;
import com.example.donkha_app.Helper.Constants;
import com.example.donkha_app.Helper.MyWork;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MainUser extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        Data data = new Data.Builder()
                .putString(Constants.KEY_START_SERVICE,"check_tranfer_money")
                .putString(Constants.KEY_SERVICE_ACCOUNT_ID, PreferenceUtils.getAccount_id(MainUser.this))
                .build();

        final OneTimeWorkRequest check = new OneTimeWorkRequest.Builder(MyWork.class).
                setInitialDelay(2, TimeUnit.SECONDS).
                setInputData(data).
                addTag("check_tranfer_money").build();
        WorkManager.getInstance().enqueue(check);

       /* SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.remove(Constants.KEY_TRAN_ID);
        prefsEditor.commit();*/


        Fragment fragment = new HomeFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment).commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null ;
            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_statement:
                    selectedFragment = new StatementFragment();
                    break;
                case R.id.nav_me:
                    selectedFragment = new MeFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,selectedFragment).commit();
            return true;
        }
    };
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.qr_code_scan,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.scan_qr_code:
                startActivity(new Intent(this, Qr_code_scan.class));
                return true;
            case R.id.main_user_tranfer_money:
                startActivity(new Intent(this,TranferMoneyActivity.class));
                return true;
        }
        return  super.onOptionsItemSelected(item);
    }
}
