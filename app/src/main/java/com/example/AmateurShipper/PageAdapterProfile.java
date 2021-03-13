package com.example.AmateurShipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapterProfile extends FragmentPagerAdapter {

    int behavior;
    public PageAdapterProfile(@NonNull FragmentManager fm) {
        super(fm);
    }

    public PageAdapterProfile(@NonNull FragmentManager fm, int behavior) {
        super(fm);
        this.behavior = behavior;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new tab_profile();
            case 1:
                return new tab_statis();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return behavior;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
