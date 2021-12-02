package com.onesilicondiode.vault_y;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends AppCompatActivity {

    int mpin;
    EditText mpinHolder;
    public static final String USER_CODE = "userPin";
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        init();
        actions();
    }

    private void actions() {
        submit.setOnClickListener(view -> {
            if (validate()) {
                mpin = Integer.parseInt(mpinHolder.getText().toString());
                SharedPreferences.Editor editor = getSharedPreferences(USER_CODE, MODE_PRIVATE).edit();
                editor.putInt("userPin",mpin);
                editor.apply();
                vibrateDeviceSuccess();
            }
        });
    }

    private void init(){
        mpinHolder = findViewById(R.id.mpin);
        submit = findViewById(R.id.pressButton);
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
        if (mpinHolder.getText().toString().isEmpty()) {
            mpinHolder.setError("4 Digit MPIN is required");
            vibrateDeviceError();
            return false;
        } else {
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
}