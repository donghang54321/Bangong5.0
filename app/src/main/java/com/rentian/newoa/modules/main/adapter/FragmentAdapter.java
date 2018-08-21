package com.rentian.newoa.modules.main.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;

import java.util.ArrayList;

public class FragmentAdapter extends FragmentPagerAdapter {

    private Context context;
    private LayoutInflater inflate;
    private ArrayList<Fragment> pagerList;

    public FragmentAdapter(FragmentManager fm, Context context, ArrayList<Fragment> pagerList) {
        super(fm);
        this.context = context;
        inflate = LayoutInflater.from(context);
        this.pagerList = pagerList;
    }

    @Override
    public Fragment getItem(int position) {
        return pagerList.get(position);
    }

    @Override
    public int getCount() {
        return pagerList.size();
    }
}
