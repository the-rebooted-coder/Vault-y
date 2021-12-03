package com.onesilicondiode.vault_y;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class ReturnLogIn extends AppCompatActivity {
    MaterialButton login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_log_in);
        init();
        actions();
    }

    private void actions() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                haptics();
                Intent toMain = new Intent(ReturnLogIn.this,MainActivity.class);
                startActivity(toMain);
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            }
        });
    }

    private void init(){
        login = findViewById(R.id.enterVaultAgain);
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