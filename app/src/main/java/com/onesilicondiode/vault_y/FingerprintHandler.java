package com.onesilicondiode.vault_y;

import android.app.Activity;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;

import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
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
            finger.playAnimation();
            finger.setRepeatCount(0);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   fingerprintText.setText(R.string.use_finger);
                }
            }, 4000);
        }
        else {
            fingerprintText.setTextColor(ContextCompat.getColor(context,R.color.green));
            finger.setAnimation("finger_pass.json");
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
}
