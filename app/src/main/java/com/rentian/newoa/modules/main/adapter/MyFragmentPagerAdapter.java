package com.rentian.newoa.modules.main.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2016/8/11.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {


    private Context context;
    private LayoutInflater inflate;
    private ArrayList<Fragment> pagerList;
    private ArrayList<String> list;

    public MyFragmentPagerAdapter(FragmentManager fm, Context context, ArrayList<Fragment> pagerList
                                 , ArrayList<String> list) {
        super(fm);
        this.context = context;
        inflate = LayoutInflater.from(context);
        this.pagerList = pagerList;
        this.list=list;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
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
