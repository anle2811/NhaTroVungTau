package com.example.anle.nhatrovungtau;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class SlidePagerAdapter extends FragmentStatePagerAdapter {

    private String [] tabtitle;
    private List<Fragment> fragmentList;

    public SlidePagerAdapter(FragmentManager fm,List<Fragment> fragmentList){
        super(fm);
        this.fragmentList=fragmentList;
    }

    public void setTabtitle(String [] tabtitle){
        this.tabtitle=tabtitle;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitle[position];
    }
}
