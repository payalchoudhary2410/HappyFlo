package com.example.happyflo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FirstFrag extends Fragment {

CardView c1,c2,c3;


    public FirstFrag() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        c1=getView().findViewById(R.id.cardView);
        c2=getView().findViewById(R.id.cardView2);
        c3=getView().findViewById(R.id.cardView3);

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/playlist?list=PLAQ7nLSEnhWTEihjeM1I-ToPDJEKfZHZu"));
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);

            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://m.fnp.com/gift/rocher-choco-bouquet?pos=1#/product-page"));
                intent.setPackage("com.android.chrome");
                startActivity(intent);

            }
        });

        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.practo.com/consult/direct/new_consultation?utm_source=google&utm_medium=cpc&utm_campaign=brand-search-practo-consult&sem=true&gclid=Cj0KCQiA0rSABhDlARIsAJtjfCdq7AuqXHVV8Qbmu2TgCp_aNeEexrak3C2mk03VeDVU7SXX5OStVnYaAimZEALw_wcB"));
                intent.setPackage("com.android.chrome");
                startActivity(intent);
            }
        });
    }
}