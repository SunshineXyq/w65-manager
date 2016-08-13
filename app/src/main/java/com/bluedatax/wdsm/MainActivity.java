package com.bluedatax.wdsm;


import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;

import com.bluedatax.wdsm.fragment.CommunityFragment;
import com.bluedatax.wdsm.fragment.DealFragment;
import com.bluedatax.wdsm.fragment.ManageFragment;
import com.bluedatax.wdsm.fragment.MineFragment;
import com.bluedatax.wdsm.service.MyService;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private RadioButton rbtManage;
    private RadioButton rbtDeal;
    private RadioButton rbtCommunity;
    private RadioButton rbtMine;
    private FragmentManager fm;
    private ManageFragment manageFragment;
    private DealFragment dealFragment;
    private CommunityFragment communityFragment;
    private MineFragment mineFragment;
    private FragmentTransaction transaction;
    private Drawable managePress;
    private Drawable dealPress;
    private Drawable communityPress;
    private Drawable minePress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        fm = getSupportFragmentManager();
        setTabSelection(0);
    }



    private void initView() {

        rbtManage = (RadioButton) findViewById(R.id.rbt_manage);
        rbtDeal = (RadioButton) findViewById(R.id.rbt_deal);
        rbtCommunity = (RadioButton) findViewById(R.id.rbt_community);
        rbtMine = (RadioButton) findViewById(R.id.rbt_mine);

        rbtManage.setOnClickListener(this);
        rbtDeal.setOnClickListener(this);
        rbtCommunity.setOnClickListener(this);
        rbtMine.setOnClickListener(this);

        managePress = getResources().getDrawable(R.mipmap.device_manage_press);
        dealPress = getResources().getDrawable(R.mipmap.deal_press);
        communityPress = getResources().getDrawable(R.mipmap.community_press);
        minePress = getResources().getDrawable(R.mipmap.mine_press);
        managePress.setBounds(0, 0, 50, 50);
        dealPress.setBounds(0, 0, 50, 50);
        communityPress.setBounds(0, 0, 50, 50);
        minePress.setBounds(0, 0, 50, 50);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int id = getIntent().getIntExtra("id",4);
        System.out.println(id);
        if (id == 1) {
            setTabSelection(2);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rbt_manage:
                setTabSelection(0);
                break;
            case R.id.rbt_deal:
                setTabSelection(1);
                break;
            case R.id.rbt_community:
                setTabSelection(2);
                break;
            case R.id.rbt_mine:
                setTabSelection(3);
                break;

            default:

                break;

        }
    }

    private void setTabSelection(int index) {
        resetButton();
        transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (index) {
            case 0:
                rbtManage.setCompoundDrawables(null,managePress,null,null);
                if (manageFragment == null) {
                    manageFragment = new ManageFragment();
                    transaction.add(R.id.fragment_content,manageFragment);
                } else {
                    transaction.show(manageFragment);
                }
                break;
            case 1:
                rbtDeal.setCompoundDrawables(null,dealPress,null,null);
                if (dealFragment == null) {
                    dealFragment = new DealFragment();
                    transaction.add(R.id.fragment_content,dealFragment);
                } else {
                    transaction.show(dealFragment);
                }
                break;
            case 2:
                rbtCommunity.setCompoundDrawables(null,communityPress,null,null);
                if (communityFragment == null) {
                    communityFragment = new CommunityFragment();
                    transaction.add(R.id.fragment_content,communityFragment);
                } else {
                    transaction.show(communityFragment);
                }
                break;
            case 3:
                rbtMine.setCompoundDrawables(null,minePress,null,null);
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    transaction.add(R.id.fragment_content,mineFragment);
                } else {
                    transaction.show(mineFragment);
                }
                break;

            default:
                break;
        }
            transaction.commit();
    }

    /**
     * 重置按钮颜色
     */
    private void resetButton() {
        Drawable manageNormal = getResources().getDrawable(R.mipmap.device_magage_normal);
        Drawable dealNormal = getResources().getDrawable(R.mipmap.deal_normal);
        Drawable communityNormal = getResources().getDrawable(R.mipmap.community_normal);
        Drawable mineNormal = getResources().getDrawable(R.mipmap.mine_normal);
        manageNormal.setBounds(0,0,50,50);
        dealNormal.setBounds(0,0,50,50);
        communityNormal.setBounds(0,0,50,50);
        mineNormal.setBounds(0,0,50,50);

        rbtManage.setCompoundDrawables(null,manageNormal,null,null);
        rbtDeal.setCompoundDrawables(null,dealNormal,null,null);
        rbtCommunity.setCompoundDrawables(null,communityNormal,null,null);
        rbtMine.setCompoundDrawables(null,mineNormal,null,null);
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (manageFragment != null) {
            transaction.hide(manageFragment);
        }
        if (dealFragment != null) {
            transaction.hide(dealFragment);
        }
        if (communityFragment != null) {
            transaction.hide(communityFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            MyService.mConnection.disconnect();
        }
        return super.onKeyDown(keyCode, event);
    }
}
