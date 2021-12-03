package com.onesilicondiode.vault_y;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class Securing extends AppCompatActivity {
    public static final String USER_CODE = "userPin";
    String securedKey;
    int binary1;
    int binary2;
    int binary3;
    int binary4;
    int binary5;
    int binary6;
    int binary7;
    int binary8;
    int splitValue1;
    int splitValue2;
    int splitValue3;
    int splitValue4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_securing);
        getKeyValue();
        startEncryptionProcess();
/*
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Intent toMain = new Intent(Securing.this,MainActivity.class);
            startActivity(toMain);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 15500);

 */
    }

    private void startEncryptionProcess() {
        splitMPIN();
        convertToBinary();
    }

    private void convertToBinary() {
        if (splitValue1 == 1){
            
        }
    }

    private void splitMPIN() {
           splitValue1 = securedKey.charAt(0);
           splitValue2 = securedKey.charAt(1);
           splitValue3 = securedKey.charAt(2);
           splitValue4 = securedKey.charAt(3);
        }

    private void getKeyValue() {
        SharedPreferences prefs = getSharedPreferences(USER_CODE, MODE_PRIVATE);
        securedKey = prefs.getString("userPin",null);
    }
}