package com.bluedatax.wdsm.adapter;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bluedatax.wdsm.fragment.BroadcastFragment;

import java.util.ArrayList;

/**
 * Created by xuyuanqiang on 7/21/16.
 */
public class TabAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> list;
    private static final String[] TITLE = new String[]{"地图", "社区", "服务"};

    public TabAdapter(FragmentManager fm,ArrayList<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return TITLE.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE[position % TITLE.length];
    }
}
