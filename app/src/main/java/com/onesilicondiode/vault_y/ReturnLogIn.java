package com.onesilicondiode.vault_y;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;

public class ReturnLogIn extends AppCompatActivity {
    MaterialButton login;
    public static final String USER_CODE = "userPin";
    EditText userEnteredPin;
    String securedKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_log_in);
        init();
        actions();
        getKeyValue();
    }

    private void actions() {
        login.setOnClickListener(view -> {
            if (userEnteredPin.getText().toString().equals(securedKey)){
                //User Entered Correct PIN proceed to Vault
                CorrectHaptics();
                Intent toMain = new Intent(ReturnLogIn.this,MainActivity.class);
                startActivity(toMain);
                finish();
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            }
            else {
                WrongHaptics();
                Toast.makeText(this,"Wrong MPIN Entered!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init(){
        login = findViewById(R.id.enterVaultAgain);
        userEnteredPin = findViewById(R.id.userEnteredmpin);
    }
    private void CorrectHaptics() {
        Vibrator v3 = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0,25,50,35,100};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v3.vibrate(VibrationEffect.createWaveform(pattern,-1));
        } else {
            v3.vibrate(pattern,-1);
        }
    }
    private void WrongHaptics() {
        Vibrator v3 = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0,10,25,40,55,70,85,100};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v3.vibrate(VibrationEffect.createWaveform(pattern,-1));
        } else {
            v3.vibrate(pattern,-1);
        }
    }
    private void getKeyValue() {
        SharedPreferences prefs = getSharedPreferences(USER_CODE, MODE_PRIVATE);
        securedKey = prefs.getString("userPin",null);
    }
}