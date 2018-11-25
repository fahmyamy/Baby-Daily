package com.health.baby_daily.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.health.baby_daily.R;
import com.health.baby_daily.fragment.BabyFragment;
import com.health.baby_daily.fragment.ParentFragment;

public class PageAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public PageAdapter(Context mContext, FragmentManager fm){
        super(fm);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new BabyFragment();
        }else {
            return new ParentFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return mContext.getString(R.string.baby_fragment);
            case 1:
                return mContext.getString(R.string.parent_fragment);
            default:
                return null;
        }
    }
}
