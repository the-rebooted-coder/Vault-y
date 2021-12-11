package com.onesilicondiode.vault_y;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity {
    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;
    public int secondsLeft = 60;
    TextView timeLeftIndi;
    // variable for our text input
    // field for phone and OTP.
    private EditText edtPhone, edtOTP;
    public static final String PHONE_AUTH = "userPhone";
    public static final String USER_PHONE_NUMBER = "userPhoneNumber";

    // buttons for generating OTP and verifying OTP
    private Button verifyOTPBtn, generateOTPBtn;

    // string for storing our verification ID
    private String verificationId;

    TextInputLayout numberHolder,otpHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        mAuth = FirebaseAuth.getInstance();
        edtPhone = findViewById(R.id.idEdtPhoneNumber);
        edtOTP = findViewById(R.id.idEdtOtp);
        verifyOTPBtn = findViewById(R.id.idBtnVerify);
        generateOTPBtn = findViewById(R.id.idBtnGetOtp);
        numberHolder = findViewById(R.id.phoneHolder);
        otpHolder = findViewById(R.id.otpHandlerHolder);

        generateOTPBtn.setOnClickListener(v -> {
            // below line is for checking weather the user
            // has entered his mobile number or not.
            if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                // when mobile number text field is empty
                numberHolder.setError("Phone number should not be blank");
            } else {
                // if the text field is not empty we are calling our
                // send OTP method for getting OTP from Firebase.
                String phone = "+91" + edtPhone.getText().toString();
                sendVerificationCode(phone);
                View parentLayout = findViewById(R.id.phoneAuthLayout);
                Snackbar.make(parentLayout, "If provided number is present in phone, Vault-y will try to auto fill it", Snackbar.LENGTH_LONG)
                        .show();
                timeLeftIndi = findViewById(R.id.timeLeft);
                timeLeftIndi.setVisibility(View.VISIBLE);
                startTime();
                haptics();
                settingUI();
                addToPrefs();
            }
        });
        verifyOTPBtn.setOnClickListener(v -> {
            // validating if the OTP text field is empty or not.
            if (TextUtils.isEmpty(edtOTP.getText().toString())) {
                // if the OTP text field is empty display
                // a message to user to enter OTP
                otpHolder.setError("Enter the OTP");
            } else {
                // if OTP field is not empty calling
                // method to verify the OTP.
                haptics();
                verifyCode(edtOTP.getText().toString());
            }
        });
    }

    private void startTime() {
        Timer t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    timeLeftIndi.setText("OTP will expire in " + secondsLeft + " secs");
                    secondsLeft -= 1;
                    if(secondsLeft == 0)
                    {
                        recreate();
                    }
                });
            }

        }, 0, 1000);
    }

    private void addToPrefs() {
        SharedPreferences.Editor editor = getSharedPreferences(USER_PHONE_NUMBER, MODE_PRIVATE).edit();
        editor.putString("userPhoneNumber",edtPhone.getText().toString());
        editor.apply();
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            Intent i = new Intent(PhoneAuth.this, Securing.class);
                            SharedPreferences.Editor editor = getSharedPreferences(PHONE_AUTH, MODE_PRIVATE).edit();
                            editor.putString("userPhone","yes");
                            editor.apply();
                            startActivity(i);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(PhoneAuth.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                edtOTP.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(PhoneAuth.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

    private void settingUI() {
        generateOTPBtn.setVisibility(View.INVISIBLE);
        verifyOTPBtn.setVisibility(View.VISIBLE);
        edtPhone.setVisibility(View.INVISIBLE);
        edtOTP.setVisibility(View.VISIBLE);
        numberHolder.setVisibility(View.INVISIBLE);
        otpHolder.setVisibility(View.VISIBLE);
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