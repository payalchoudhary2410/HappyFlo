package com.example.happyflo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AppMainPage extends AppCompatActivity {

    public TabLayout tabLayout;
    public ViewPager viewPager;
    public Toolbar toolbar;
    myadapter pagerAdapter;
    ProgressBar progressBar;
    private ArrayList<String> interestNames = new ArrayList<String>();

    DatabaseReference RootRef;
    DatabaseReference reff;
    ImageView menU;

    String mobileText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main_page);
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            mobileText = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();






            tabLayout = findViewById(R.id.tabLayout);
            viewPager = findViewById(R.id.pager);
            pagerAdapter =
                    new myadapter(getSupportFragmentManager());
            viewPager.setAdapter(pagerAdapter);
            progressBar = findViewById(R.id.progressBarApp);
            tabLayout.setupWithViewPager(viewPager);

            RootRef = FirebaseDatabase.getInstance().getReference();
            progressBar.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);



        }

        else {
        Intent mainIntent= new Intent(AppMainPage.this, LoginPage.class);
        startActivity(mainIntent);
        finish();
    }

}


    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser()==null){
            Intent mainIntent= new Intent(AppMainPage.this, LoginPage.class);
            startActivity(mainIntent);
            finish();
        }
    }

    private void Logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(AppMainPage.this, MainActivity.class));
        finish();
        finishAffinity();
    }

    public String sendData(){
        return mobileText;
    }
}