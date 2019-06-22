package com.example.anle.nhatrovungtau.XemKhuTroPhongTro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int numOfTab;

    public PagerAdapter(FragmentManager fm,int numOfTab){
        super(fm);
        this.numOfTab=numOfTab;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                FragmentKhuTro fragmentKhuTro=new FragmentKhuTro();
                return fragmentKhuTro;
            case 1:
                FragmentPhongTro fragmentPhongTro=new FragmentPhongTro();
                return fragmentPhongTro;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTab;
    }
}
