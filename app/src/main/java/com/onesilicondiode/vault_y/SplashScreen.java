package com.onesilicondiode.vault_y;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    public static final String USER_CODE = "userPin";
    String anshu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences(USER_CODE, MODE_PRIVATE);
        anshu = prefs.getString("userPin",null);
        if (anshu != null){
            //user pin set, proceed to main-activity
            Intent toReturn = new Intent(this,ReturnLogIn.class);
            startActivity(toReturn);
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