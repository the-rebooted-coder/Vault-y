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
    public static final String UI_MODE = "uiMode";
    String anshu;
    String theme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences setTheme = getSharedPreferences(UI_MODE, MODE_PRIVATE);
        theme= setTheme.getString("uiMode", "System");
        applyUI();
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

    private void applyUI() {
        if (theme.equals("Dark")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else if (theme.equals("Light")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }
}