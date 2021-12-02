package com.onesilicondiode.vault_y;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    public static final String USER_CODE = "userPin";
    int anshu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences(USER_CODE, MODE_PRIVATE);
        anshu = prefs.getInt("userPin",0);
        if (anshu !=0){
            //user pin set, proceed to main-activity
            Intent toMain = new Intent(this,MainActivity.class);
            startActivity(toMain);
            finish();
        }
        else {
            //user pin not set, proceed to setting
            Intent toSignUp = new Intent(this,SignUp.class);
            startActivity(toSignUp);
            finish();
        }
    }
}