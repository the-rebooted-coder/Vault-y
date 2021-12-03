package com.onesilicondiode.vault_y;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class Securing extends AppCompatActivity {
    public static final String USER_CODE = "userPin";
    String securedKey;
    int binary1;
    int binary2;
    int binary3;
    int binary4;
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
        Log.i("Generated Keypair",binary1+"");
        Log.i("Generated Keypair",binary2+"");
        Log.i("Generated Keypair",binary3+"");
        Log.i("Generated Keypair",binary4+"");
    }

    private void convertToBinary() {
       splitValueOne();
       splitValueTwo();
       splitValueThree();
       splitValueFour();
    }
    private void splitValueOne() {
        if (splitValue1== 0){
            binary1 = 0000;
        }
        else if (splitValue1 == 1){
            binary1 = 0001;
        }
        else if (splitValue1 == 2){
            binary1 = 1011;
        }
        else if (splitValue1 == 3){
            binary1 = 0011;
        }
        else if (splitValue1 == 4){
            binary1 = 0101;
        }
        else if (splitValue1 == 5){
            binary1 = 0011;
        }
        else if (splitValue1 == 6){
            binary1 = 1100;
        }
        else if (splitValue1 == 7){
            binary1 = 1110;
        }
        else if (splitValue1 == 8){
            binary1 = 1010;
        }
        else if (splitValue1 == 9){
            binary1 = 0111;
        }
    }
    private void splitValueTwo() {
        if (splitValue2 <4){
            binary2 = 00;
        }
        else if (splitValue2 < 7){
            binary2 = 01;
        }
        else if (splitValue2 <10){
            binary2 = 10;
        }
    }
    private void splitValueThree() {
        if (splitValue3 <4){
            binary3 = 10;
        }
        else if (splitValue3 < 7){
            binary3 = 00;
        }
        else if (splitValue3 <10){
            binary3 = 01;
        }
    }
    private void splitValueFour() {
        if (splitValue4 <4){
            binary4 = 01;
        }
        else if (splitValue4 < 7){
            binary4 = 10;
        }
        else if (splitValue4 <10){
            binary4 = 00;
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