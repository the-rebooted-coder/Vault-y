package com.onesilicondiode.vault_y;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

public class MainActivity extends AppCompatActivity {
    boolean isSessionActive = true;
    public static final String USER_CODE = "userPin";
    public static final String UI_MODE = "uiMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Back again to close Vault-y", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }
    @Override
    protected void onPause() {
        super.onPause();
        haptics();
        isSessionActive = false;
        Toast.makeText(this,"Vault Locked",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isSessionActive) {
            Intent toReturn = new Intent(this,ReturnLogIn.class);
            startActivity(toReturn);
            overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.moreEncryptions:
                haptics();
                Toast.makeText(this,"More to be implemented (:",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                haptics();
                isSessionActive = false;
                Toast.makeText(this,"Securely closing Vault-y",Toast.LENGTH_SHORT).show();
                Intent toReturn = new Intent(this,ReturnLogIn.class);
                startActivity(toReturn);
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                finish();
                return true;
            case R.id.clearPin:
                haptics();
                SharedPreferences.Editor editor = getSharedPreferences(USER_CODE, MODE_PRIVATE).edit();
                editor.putString("userPin",null);
                editor.apply();
                Toast.makeText(this,"MPIN cleared successfully!",Toast.LENGTH_SHORT).show();
                Intent toSignUp = new Intent(this,SignUp.class);
                startActivity(toSignUp);
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                finish();
                return true;
            case R.id.aboutApp:
                haptics();
                Toast.makeText(this,"About to be written",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.theme:
                int nightModeFlags =
                        this.getResources().getConfiguration().uiMode &
                                Configuration.UI_MODE_NIGHT_MASK;
                new MaterialDialog.Builder(this)
                        .cancelable(false)
                        .title("Choose how you want Vault-y")
                        .positiveText("Dark")
                        .negativeText("Light")
                        .autoDismiss(true)
                        .neutralText("System Default")
                        .onPositive((dialog, which) -> {
                            haptics();
                            switch (nightModeFlags) {
                                case Configuration.UI_MODE_NIGHT_YES:
                                    Toast.makeText(getApplicationContext(),"Already in Dark Mode \uD83C\uDF19",Toast.LENGTH_SHORT).show();
                                    break;
                                case Configuration.UI_MODE_NIGHT_NO:
                                    haptics();
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                    Toast.makeText(getApplicationContext(),"Switched to Dark Mode \uD83C\uDF19️",Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor editor1 = getSharedPreferences(UI_MODE, MODE_PRIVATE).edit();
                                    editor1.putString("uiMode","Dark");
                                    editor1.apply();
                                    reStart();
                                    break;
                            }
                        })
                        .onNeutral((dialog, which) -> {
                            haptics();
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                            Toast.makeText(getApplicationContext(),"Switched to System Default️ \uD83C\uDF17",Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor12 = getSharedPreferences(UI_MODE, MODE_PRIVATE).edit();
                            editor12.putString("uiMode","System");
                            editor12.apply();
                            reStart();
                        })
                        .onNegative((dialog, which) -> {
                            switch (nightModeFlags) {
                                case Configuration.UI_MODE_NIGHT_YES:
                                    haptics();
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                    Toast.makeText(getApplicationContext(), "Switched to Light Mode️ ☀", Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor editor13 = getSharedPreferences(UI_MODE, MODE_PRIVATE).edit();
                                    editor13.putString("uiMode", "Light");
                                    editor13.apply();
                                    reStart();
                                    break;
                                case Configuration.UI_MODE_NIGHT_NO:
                                    Toast.makeText(getApplicationContext(), "Already in Light Mode ☀️", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        })
                        .show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void reStart() {
        Intent toReturn = new Intent(this,ReturnLogIn.class);
        startActivity(toReturn);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}