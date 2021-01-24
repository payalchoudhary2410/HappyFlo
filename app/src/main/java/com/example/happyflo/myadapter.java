package com.example.happyflo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class myadapter extends FragmentPagerAdapter {

    public myadapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new FirstFrag();
            case 1:
                return new SecondFrag();
            case 2:
                return new ThirdFrag();
                default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0: return "What Can I Do?";
            case 1: return "My Graphs";
            case 2: return "My Period Tracker";

            default: return null;

        }
    }

}
