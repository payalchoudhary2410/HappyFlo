package com.example.happyflo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.RemoteInput;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


import java.util.concurrent.TimeUnit;

public class LoginPage extends AppCompatActivity {

    private EditText phone, optEnter;
    CountryCodePicker countryCodePicker;
    Button next;
    PhoneAuthCredential credential;
    Boolean verificationOnProgress = false;
    ProgressBar progressBar;
    TextView state, resend, signUp, contactUs;
    PhoneAuthProvider.ForceResendingToken token;
    FirebaseAuth fAuth;
    String otpCode;
    String verificationId;
    public String phoneNum;
    DatabaseReference user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        createNotificationChannel();

        Button notifyBtn= findViewById(R.id.notify_button);
        notifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginPage.this, "Notifications allowed!",Toast.LENGTH_SHORT ).show();
                Intent intent =  new Intent(LoginPage.this, Notifications.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(LoginPage.this, 0,intent,0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                long start_time = System.currentTimeMillis();
                long ten_seconds = 10*1000;

                alarmManager.set(AlarmManager.RTC_WAKEUP, start_time + ten_seconds, pendingIntent);
//                alarmManager.set(AlarmManager.RTC_WAKEUP, start_time + 5*ten_seconds, pendingIntent);
//                alarmManager.set(AlarmManager.RTC_WAKEUP, start_time + 10*ten_seconds, pendingIntent);
//                alarmManager.set(AlarmManager.RTC_WAKEUP, start_time + 15*ten_seconds, pendingIntent);
//                alarmManager.set(AlarmManager.RTC_WAKEUP, start_time + 20*ten_seconds, pendingIntent);



            }
        });

        phone = findViewById(R.id.phone1);
        optEnter = findViewById(R.id.codeEnter1);
        countryCodePicker = findViewById(R.id.ccp1);
        next = findViewById(R.id.nextBtn1);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar1);
        state = findViewById(R.id.state1);
        resend = findViewById(R.id.resendOtpBtn1);
        signUp = findViewById(R.id.signup);
        contactUs = findViewById(R.id.contact);

        user = FirebaseDatabase.getInstance().getReference("userDetails");

        String phoneText = phone.getText().toString();
        phoneNum = "+" + countryCodePicker.getSelectedCountryCode() + phoneText;


        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginPage.this, "Please Enter MObile No Again", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginPage.this, LoginPage.class));
            }
        });

        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
           //     Toast.makeText(LoginPage.this, "Verify Clicked", Toast.LENGTH_SHORT).show();
                if (!phone.getText().toString().isEmpty() && phone.getText().toString().length() == 10) {
                    if (!verificationOnProgress) {
                        next.setEnabled(false);


                        String phoneText = phone.getText().toString();
                        phoneNum = "+" + countryCodePicker.getSelectedCountryCode() + phoneText;


                        DatabaseReference mobileNoRef = user.child(phoneNum);
                        ValueEventListener eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    final String phoneNum2 = phoneNum;
                                    progressBar.setVisibility(View.VISIBLE);
                                    state.setText("Sending OTP");
                                    state.setVisibility(View.VISIBLE);
                                    //interestSelected=dataSnapshot.child(phoneNum2).child("choiceSelected").getValue().toString();
                                    Log.d("phone", "Phone No.: " + phoneNum2);
                                    requestPhoneAuth(phoneNum2);


                                } else {
                                    Toast.makeText(LoginPage.this, "Mobile Number not Registered", Toast.LENGTH_SHORT).show();
                                    next.setEnabled(true);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d("Tag", databaseError.getMessage());

                            }
                        };
                        mobileNoRef.addListenerForSingleValueEvent(eventListener);

                    } else {
                        next.setEnabled(false);
                        optEnter.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        state.setText("Verfying OTP");
                        state.setVisibility(View.VISIBLE);
                        otpCode = optEnter.getText().toString();
                        if (otpCode.isEmpty()) {
                            optEnter.setError("OTP Required");
                            return;
                        }

                        credential = PhoneAuthProvider.getCredential(verificationId, otpCode);
                        verifyAuth(credential);
                    }

                } else {
                    phone.setError("Valid Phone Number Required");
                }
            }
        });

        SignUpSetOnClickListener();
        ContactSetListener();
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "HappyFlow Notification";
            String description = "Channel for Notification";
            int importance  = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notify",name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);


        }

    }

    private void requestPhoneAuth(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 45L, TimeUnit.SECONDS, this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                        resend.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        token = forceResendingToken;
                        verificationOnProgress = true;
                        progressBar.setVisibility(View.GONE);
                        state.setVisibility(View.GONE);
                        next.setText("Verify");
                        next.setEnabled(true);
                        optEnter.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                        // called if otp is automatically detected by the app
                        verifyAuth(phoneAuthCredential);

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(LoginPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void verifyAuth(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginPage.this, "Phone Verified.", Toast.LENGTH_SHORT).show();
                    Intent appMainPage_intent = new Intent(LoginPage.this, AppMainPage.class);
                    appMainPage_intent.putExtra("mobileText", phoneNum);
                    startActivity(appMainPage_intent);
                    finish();
                } else {
                    progressBar.setVisibility(View.GONE);
                    state.setVisibility(View.GONE);
                    Toast.makeText(LoginPage.this, "Can not Verify phone", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.AuthStateListener mAuthStateListener;

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = fAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    final String mobileNo = mFirebaseUser.getPhoneNumber();
                    Intent appMainPage_intent = new Intent(LoginPage.this, AppMainPage.class);
                    appMainPage_intent.putExtra("mobileText", mobileNo);
                    startActivity(appMainPage_intent);
                    finish();
                    finishAffinity();
                    finishAndRemoveTask();

                }
            }

        };
        fAuth.addAuthStateListener(mAuthStateListener);
    }
    public void SignUpSetOnClickListener(){
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this, Registration.class));

            }
        });
    }

    public void ContactSetListener(){
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this, ContactUs.class));
            }
        });
    }
}