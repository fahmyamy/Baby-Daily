package com.health.baby_daily.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.health.baby_daily.R;
import com.health.baby_daily.chatfragment.CGFragment;
import com.health.baby_daily.chatfragment.ChatFragment;
import com.health.baby_daily.chatfragment.DoctorFragment;

public class Page2Adapter extends FragmentPagerAdapter {
    private Context mContext;

    public Page2Adapter(Context mContext, FragmentManager fm){
        super(fm);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new ChatFragment();
        }else if (position == 1){
            return new CGFragment();
        }else if (position == 2){
            return new DoctorFragment();
        }else {
            return new ChatFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return mContext.getString(R.string.chat_fragment);
            case 1:
                return mContext.getString(R.string.caregiver_fragment);
            case 2:
                return mContext.getString(R.string.doctor_fragment);
            default:
                return null;
        }
    }
}
