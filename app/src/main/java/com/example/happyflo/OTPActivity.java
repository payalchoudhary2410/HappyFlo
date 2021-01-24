package com.example.happyflo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    String otpCode;
    String verificationId;
    EditText phone, optEnter;
    Button next;
    CountryCodePicker countryCodePicker;
    PhoneAuthCredential credential;
    Boolean verificationOnProgress = false;
    ProgressBar progressBar;
    TextView state, resend;
    PhoneAuthProvider.ForceResendingToken token;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

     String phoneNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);

        phone = findViewById(R.id.phone);
        optEnter = findViewById(R.id.codeEnter);
        countryCodePicker = findViewById(R.id.ccp);
        next = findViewById(R.id.nextBtn);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        state = findViewById(R.id.state);
        resend = findViewById(R.id.resendOtpBtn);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("userDetails");


        final String phoneText = phone.getText().toString();
         phoneNum = "+" + countryCodePicker.getSelectedCountryCode() + phoneText;

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OTPActivity.this, "Please Enter MObile No Again", Toast.LENGTH_LONG).show();
                startActivity(new Intent(OTPActivity.this, OTPActivity.class));
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


                        DatabaseReference mobileNoRef = reference.child(phoneNum);
                        ValueEventListener eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    final String phoneNum2 = phoneNum;
                                    progressBar.setVisibility(View.VISIBLE);
                                    state.setText("Sending OTP");
                                    state.setVisibility(View.VISIBLE);
                                    //interestSelected=dataSnapshot.child(phoneNum2).child("choiceSelected").getValue().toString();
                                    Log.d("phone", "Phone No.: " + phoneNum2);
                                    requestPhoneAuth(phoneNum2);


                                } else {
                                    Toast.makeText(OTPActivity.this, "Mobile Number Already Exists", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(OTPActivity.this, MainActivity.class));
                                    finish();
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
                        Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void verifyAuth(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(OTPActivity.this, "Phone Verified.", Toast.LENGTH_SHORT).show();
                    Intent appMainPage_intent = new Intent(OTPActivity.this, Registration.class);
                    appMainPage_intent.putExtra("mobileText", phoneNum);
                    startActivity(appMainPage_intent);
                    finish();
                } else {
                    progressBar.setVisibility(View.GONE);
                    state.setVisibility(View.GONE);
                    Toast.makeText(OTPActivity.this, "Can not Verify phone", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}