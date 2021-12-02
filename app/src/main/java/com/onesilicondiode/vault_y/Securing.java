package com.onesilicondiode.vault_y;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class Securing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_securing);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Intent toMain = new Intent(Securing.this,MainActivity.class);
            startActivity(toMain);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 15500);
    }
}