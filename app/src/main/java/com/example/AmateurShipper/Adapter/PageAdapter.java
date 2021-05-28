package com.example.AmateurShipper.Adapter;

import com.example.AmateurShipper.Fragment.tab_dang_giao;
import com.example.AmateurShipper.Fragment.tab_lich_su;
import com.example.AmateurShipper.Fragment.tab_nhan;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {
    int behavior;
    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm,behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new tab_nhan();
            case 1:
                return new tab_dang_giao();
            case 2:
                return new tab_lich_su();
            default: return new tab_nhan();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

}
