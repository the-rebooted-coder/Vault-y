package com.onesilicondiode.vault_y;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;

import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private Context context;
    public FingerprintHandler (Context context){
        this.context = context;
    }
    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){
         CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject,cancellationSignal,0,this,null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        this.updateUser("Authentication Error "+errString,false);
    }

    private void updateUser(String s, boolean b) {
        TextView fingerprintText = (TextView) ((Activity)context).findViewById(R.id.aboutFingerprint);
        LottieAnimationView finger = (LottieAnimationView) ((Activity)context).findViewById(R.id.fingerPrintAnim);
        fingerprintText.setText(s);
        if (!b){
            fingerprintText.setTextColor(ContextCompat.getColor(context,R.color.red));
            finger.setAnimation("finger_fail.json");
            finger.setSpeed(4);
            finger.playAnimation();
            finger.setRepeatCount(0);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
               fingerprintText.setText(R.string.use_finger);
               finger.setAnimation("finger_starter.json");
               finger.playAnimation();
            }, 4000);
        }
        else {
             final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                Intent toMain = new Intent(context,MainActivity.class);
                context.startActivity(toMain);
                ((Activity) context).finish();
                CorrectHaptics();
            }, 1200);
             final Handler secondPasser = new Handler(Looper.getMainLooper());
            secondPasser.postDelayed(this::CorrectHaptics, 100);
            final Handler thirdPasser = new Handler(Looper.getMainLooper());
            thirdPasser.postDelayed(this::CorrectHaptics, 600);
            final Handler fourthPasser = new Handler(Looper.getMainLooper());
            fourthPasser.postDelayed(this::CorrectHaptics, 900);
            fingerprintText.setTextColor(ContextCompat.getColor(context,R.color.blue));
            finger.setAnimation("finger_pass.json");
            finger.setSpeed(4);
            finger.playAnimation();
            finger.setRepeatCount(0);
        }
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.updateUser("Error "+helpString,false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.updateUser("Authentication Successful",true);
    }

    @Override
    public void onAuthenticationFailed() {
        this.updateUser("Authentication Failed",false);
    }
    private void CorrectHaptics() {
        Vibrator v3 = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0,25,50};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v3.vibrate(VibrationEffect.createWaveform(pattern,-1));
        } else {
            v3.vibrate(pattern,-1);
        }
    }
}
