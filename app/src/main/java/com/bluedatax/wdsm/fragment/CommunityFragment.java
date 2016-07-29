package com.bluedatax.wdsm.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bluedatax.wdsm.R;
import com.bluedatax.wdsm.adapter.TabAdapter;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

public class CommunityFragment extends Fragment {

    private ViewPager mViewPager;
    private TabPageIndicator mIndicator;
    private TabAdapter mAdapter;
    private Button btCommunity;
    private ArrayList<Fragment> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_community_fragment,null);
//        btCommunity = (Button) view.findViewById(R.id.bt_community);
//        btCommunity.setOnClickListener(this);
        init();

        Context contextThemeWrapper = new ContextThemeWrapper(getActivity(),R.style.MyTheme);
        LayoutInflater layoutInflater = inflater.cloneInContext(contextThemeWrapper);
        View rootView  = layoutInflater.inflate(R.layout.activity_community_fragment, container, false);
        mViewPager = (ViewPager) rootView.findViewById(R.id.id_pager);
        mIndicator = (TabPageIndicator) rootView.findViewById(R.id.id_indicator);
        mAdapter = new TabAdapter(getFragmentManager(),list);
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager,0);

        return rootView;
    }

    private void init() {
        list = new ArrayList<Fragment>();
        BroadcastFragment broadcastFragment = new BroadcastFragment();
        MapFragment mapFragment = new MapFragment();
        ServiceFragment serviceFragment = new ServiceFragment();

        list.add(mapFragment);
        list.add(broadcastFragment);
        list.add(serviceFragment);
    }


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.bt_community:
//                Intent intent = new Intent(getActivity(),CommunityBroadcast.class);
//                startActivity(intent);
//                break;
//        }
//    }
}
