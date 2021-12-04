package com.onesilicondiode.vault_y;

import android.app.Activity;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;

import android.os.CancellationSignal;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

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
        ImageView finger = (ImageView) ((Activity)context).findViewById(R.id.fingerprintView);
        fingerprintText.setText(s);
        if (!b){
            fingerprintText.setTextColor(ContextCompat.getColor(context,R.color.red));
            finger.setImageResource(R.drawable.ic_baseline_cancel);
        }
        else {
            fingerprintText.setTextColor(ContextCompat.getColor(context,R.color.green));
            finger.setImageResource(R.drawable.ic_baseline_check_pass);
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
