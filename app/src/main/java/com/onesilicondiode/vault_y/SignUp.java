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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class SignUp extends AppCompatActivity {

    String mpin;
    EditText mpinHolder;
    public static final String USER_CODE = "userPin";
    Button submit;
    LottieAnimationView vaultAnim;

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
                mpin = mpinHolder.getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences(USER_CODE, MODE_PRIVATE).edit();
                editor.putString("userPin",mpin);
                editor.apply();
                Toast.makeText(this,"MPIN set to "+mpin,Toast.LENGTH_SHORT).show();
                vibrateDeviceSuccess();
                Intent toMain = new Intent(this,MainActivity.class);
                startActivity(toMain);
                finish();
            }
        });
        vaultAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrateOnVault();
                vaultAnim.playAnimation();
                vaultAnim.setRepeatCount(0);
            }
        });
    }

    private void init(){
        mpinHolder = findViewById(R.id.mpin);
        submit = findViewById(R.id.pressButton);
        vaultAnim = findViewById(R.id.meanwhileLoaderAnim);
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