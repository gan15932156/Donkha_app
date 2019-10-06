package com.example.donkha_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Qr_code_scan extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView zXingScannerView;
    private String resultt= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        zXingScannerView = new ZXingScannerView(this);
        setContentView(zXingScannerView);
    }

    @Override
    public void handleResult(Result result) {
        resultt = result.getText(); /* Retrieving text from QR Code */

        if(resultt.equals("deposit")){
            this.finishAffinity();
            startActivity(new Intent(Qr_code_scan.this,DepositActivity.class));
        }
        else if(resultt.equals("withdraw")){
            this.finishAffinity();
            startActivity(new Intent(Qr_code_scan.this,WithdrawActivity.class));
        }
        else if(resultt.equals("tranfer_money")){

        }
        else{
            Toast.makeText(this, "QR code ผิดพลาด กรุณาสแกนใหม่", Toast.LENGTH_SHORT).show();
            zXingScannerView.resumeCameraPreview(this);
            resultt = null;
        }
        //scannerView.resumeCameraPreview(this);  /* If you want resume scanning, call this method */
        //resultText = null;
        //onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            zXingScannerView.setResultHandler(this); /* Set handler for ZXingScannerView */
            zXingScannerView.startCamera(); /* Start camera */
        } else {
            ActivityCompat.requestPermissions(Qr_code_scan.this, new
                    String[]{Manifest.permission.CAMERA}, 1024);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }
}
