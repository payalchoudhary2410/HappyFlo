package com.example.happyflo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Registration extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public EditText username;
    public Button submitBtn;
    TextView logIn, contactUs,dob,dop;
    public String usernameText, mobileNoText, dopText, dobText;

    FirebaseDatabase rootNode;
    DatabaseReference reference, ConnectionReference;
    private boolean doperiod=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        username=(EditText)findViewById(R.id.name);
        dob=findViewById(R.id.dob);
        dop=findViewById(R.id.dop);
        logIn=(TextView)findViewById(R.id.logInPage);
        contactUs=(TextView)findViewById(R.id.contactReg);

        submitBtn=findViewById(R.id.signupBtn);



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode=FirebaseDatabase.getInstance();
                reference=rootNode.getReference("userDetails");
                ConnectionReference=rootNode.getReference("Connections");
                usernameText=username.getText().toString();
                dobText=dob.getText().toString();
                dopText=dop.getText().toString();
                Intent regIntent=getIntent();
                mobileNoText=regIntent.getStringExtra("mobileText");

                if (usernameText.isEmpty() || dopText.isEmpty() || dobText.isEmpty() || mobileNoText.isEmpty() ) {
                    Toast.makeText(Registration.this, "Please Enter All the Details",Toast.LENGTH_LONG).show();
                }
                else {

                    DatabaseReference mobileNoRef=reference.child(mobileNoText);
                    ValueEventListener eventListener=new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()){
                                Users userDetail = new Users(usernameText, mobileNoText, dobText,dopText);
                                reference.child(mobileNoText).setValue(userDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(Registration.this, "Registration Successful: Login Again to Continue", Toast.LENGTH_LONG).show();
                                        //reference2.child(mobileNoText).setValue(false);
                                        Intent interestIntent =new Intent(Registration.this, LoginPage.class);
                                        interestIntent.putExtra("mobileText", mobileNoText);
                                        startActivity(interestIntent);
                                        finish();


                                    }
                                });
                            }
                            else {
                                Toast.makeText(Registration.this, "Mobile Number already Registered", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d("Tag", databaseError.getMessage());


                        }
                    };
                    mobileNoRef.addListenerForSingleValueEvent(eventListener);


                }



            }
        });


        loginSetListener();
        contactSetListener();
        dobOnClickListener();
        dopOnClickListener();
    }

    public void loginSetListener(){
        FirebaseAuth.getInstance().signOut();
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, LoginPage.class));
                finish();
            }
        });
    }

    public void contactSetListener(){
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, ContactUs.class));
            }
        });
    }
//
//    private void tncOnclickListener(){
//        tnc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (tnc.isChecked()){
//                    submitBtn.setEnabled(true);
//                }
//                else {
//                    submitBtn.setEnabled(false);
//                }
//            }
//        });
//    }

    private void dobOnClickListener(){
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doperiod=false;
                showDatePickerDialog();

            }
        });

    }
    private void dopOnClickListener(){
        dop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doperiod=true;

                showDatePickerDialog();

            }
        });

    }

    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog=new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        );
        datePickerDialog.show();
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dobFromCalendar = dayOfMonth + "/" + (month + 1) + "/" + year;
        if (doperiod == false) {
            dob.setText(dobFromCalendar);
            dob.setTranslationZ(0.0f);
        }

        else{
            dop.setText(dobFromCalendar);
            dop.setTranslationZ(0.0f);

        }
    }



    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Registration.this, LoginPage.class));
        finishAffinity();
        finish();
    }

    public String sendData(){
        return mobileNoText;
    }

}


