package com.onesilicondiode.vault_y;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class ReturnLogIn extends AppCompatActivity {
    MaterialButton login;
    public static final String USER_CODE = "userPin";
    public static final String USER_NAME = "userName";
    String userName;
    EditText userEnteredPin;
    String securedKey;
    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "AndroidKey";
    TextView greeting;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_log_in);
        getKeyValue();
        init();
        actions()
        ;
    }

    private void actions() {
        login.setOnClickListener(view -> {
            if (userEnteredPin.getText().toString().equals(securedKey)) {
                //User Entered Correct PIN proceed to Vault
                CorrectHaptics();
                Intent toMain = new Intent(this, MainActivity.class);
                startActivity(toMain);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            } else if (userEnteredPin.getText().toString().isEmpty()) {
                WrongHaptics();
                userEnteredPin.setError("Enter MPIN");
            } else {
                WrongHaptics();
                Toast.makeText(this, "Wrong MPIN Entered", Toast.LENGTH_SHORT).show();
            }
        });
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        if (!fingerprintManager.isHardwareDetected()) {
            Toast.makeText(this, "Fingerprint sensor not detected", Toast.LENGTH_SHORT).show();
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission denied to use fingerprint sensor", Toast.LENGTH_SHORT).show();
        } else if (!keyguardManager.isKeyguardSecure()) {
            Toast.makeText(this, "Add device lock to use", Toast.LENGTH_SHORT).show();
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
            Toast.makeText(this, "No fingerprints enrolled in settings", Toast.LENGTH_SHORT).show();
        } else {
            generateKey();
            if (cipherInit()) {
                FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                FingerprintHandler fingerprintHandler = new FingerprintHandler(this);
                fingerprintHandler.startAuth(fingerprintManager, cryptoObject);
            }
        }
    }

    private void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT |
                    KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (KeyStoreException | IOException | CertificateException
                | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }
        try {
            keyStore.load(null);

            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;

        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @SuppressLint("SetTextI18n")
    private void init(){
        login = findViewById(R.id.enterVaultAgain);
        greeting = findViewById(R.id.appNameHolder);
        SharedPreferences nameGet = getSharedPreferences(USER_NAME, MODE_PRIVATE);
        userName = nameGet.getString("userName","Vault-y User");
        greeting.setText(userName+", welcome back to Vault-y!");
        userEnteredPin = findViewById(R.id.userEnteredmpin);
    }
    private void CorrectHaptics() {
        Vibrator v3 = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0,25,50,35,100};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v3.vibrate(VibrationEffect.createWaveform(pattern,-1));
        } else {
            v3.vibrate(pattern,-1);
        }
    }
    private void WrongHaptics() {
        Vibrator v3 = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0,10,25,40,55,70,85,100};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v3.vibrate(VibrationEffect.createWaveform(pattern,-1));
        } else {
            v3.vibrate(pattern,-1);
        }
    }
    private void getKeyValue() {
        SharedPreferences prefs = getSharedPreferences(USER_CODE, MODE_PRIVATE);
        securedKey = prefs.getString("userPin",null);
    }
}