package com.onesilicondiode.vault_y;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class SignUp extends AppCompatActivity {

    String mpin;
    EditText mpinHolder;
    public static final String USER_CODE = "userPin";
    public static final String USER_NAME = "userName";
    Button submit;
    TextView aboutDES;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        init();
        actions();
    }

    @SuppressLint("SetTextI18n")
    private void actions() {
        submit.setOnClickListener(view -> {
            if (validate()) {
                storeKeyAndName();
                vibrateDeviceSuccess();
                Intent toSecuring = new Intent(this,Securing.class);
                startActivity(toSecuring);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
        aboutDES.setOnClickListener(view -> {
            vibrateOnVault();
            Uri uri = Uri.parse("https://en.wikipedia.org/wiki/Data_Encryption_Standard");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            Toast.makeText(this,"Go back to return to Vault-y",Toast.LENGTH_SHORT).show();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    private void storeKeyAndName() {
        mpin = mpinHolder.getText().toString();
        SharedPreferences.Editor editor = getSharedPreferences(USER_CODE, MODE_PRIVATE).edit();
        editor.putString("userPin",mpin);
        editor.apply();
        SharedPreferences.Editor nameEditor = getSharedPreferences(USER_NAME, MODE_PRIVATE).edit();
        nameEditor.putString("userName",name.toString());
        nameEditor.apply();
    }

    private void init(){
        mpinHolder = findViewById(R.id.mpin);
        name = findViewById(R.id.name);
        submit = findViewById(R.id.pressButton);
        aboutDES = findViewById(R.id.aboutDES);
    }
    private void vibrateDeviceSuccess() {
        Vibrator v3 = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0,25,70,38};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v3.vibrate(VibrationEffect.createWaveform(pattern,-1));
        } else {
            v3.vibrate(pattern,-1);
        }
    }
    boolean validate() {
        if (name.getText().toString().isEmpty()) {
            name.setError("Name required to continue");
            vibrateDeviceError();
            return false;
        }
        else if (mpinHolder.getText().toString().isEmpty()) {
            mpinHolder.setError("4 Digit MPIN is required");
            vibrateDeviceError();
            return false;
        }
        else {
            return true;
        }
    }
    private void vibrateDeviceError() {
        Vibrator v3 = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0,25,50,35,100};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v3.vibrate(VibrationEffect.createWaveform(pattern,-1));
        } else {
            v3.vibrate(pattern,-1);
        }
    }
    private void vibrateOnVault() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(32, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(28);
        }
    }
}