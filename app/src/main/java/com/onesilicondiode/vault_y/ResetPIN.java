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

import com.google.android.material.button.MaterialButton;

public class ResetPIN extends AppCompatActivity {
    EditText newMPIN;
    MaterialButton saveNewPin;
    public static final String USER_CODE = "userPin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pin);
        init();
        actionsTrigger();
    }

    private void actionsTrigger() {
        saveNewPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                haptics();
                String newPin = newMPIN.getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences(USER_CODE, MODE_PRIVATE).edit();
                editor.putString("userPin",newPin);
                editor.apply();
                Toast.makeText(ResetPIN.this,"MPIN Reset Successfully!",Toast.LENGTH_SHORT).show();
                Intent toSecure = new Intent(ResetPIN.this,Securing.class);
                startActivity(toSecure);
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                finish();
            }
        });
    }

    private void init(){
        newMPIN = findViewById(R.id.newPin);
        saveNewPin = findViewById(R.id.resetButton);
    }
    private void haptics() {
        Vibrator v3 = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0,25,50,35,100};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v3.vibrate(VibrationEffect.createWaveform(pattern,-1));
        } else {
            v3.vibrate(pattern,-1);
        }
    }
}