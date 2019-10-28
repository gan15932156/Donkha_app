package com.example.donkha_app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.donkha_app.Fragment.HomeFragment;
import com.example.donkha_app.Fragment.MeFragment;
import com.example.donkha_app.Fragment.StatementFragment;

public class MainUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

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
