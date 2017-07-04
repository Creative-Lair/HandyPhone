package com.creativelair.handyphone.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.creativelair.handyphone.Fragment.All_Contacts;
import com.creativelair.handyphone.Fragment.Family_Contacts;
import com.creativelair.handyphone.Fragment.Frequent;
import com.creativelair.handyphone.Fragment.Friend_Contacts;
import com.creativelair.handyphone.Fragment.Work_Contacts;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new Frequent();
            case 1:
                return new Family_Contacts();
            case 2:
                return new Friend_Contacts();
            case 3:
                return new Work_Contacts();
            case 4:
                return new All_Contacts();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "FREQUENT";
            case 1:
                return "FAMILY";
            case 2:
                return "FRIEND";
            case 3:
                return "WORK";
            case 4:
                return "ALL CONTACTS";
        }
        return null;
    }
}