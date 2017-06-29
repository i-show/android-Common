/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.noahark.modules.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import com.ishow.common.utils.log.L;
import com.ishow.common.widget.TopBar;
import com.ishow.noahark.R;
import com.ishow.noahark.modules.base.AppBaseActivity;
import com.ishow.noahark.modules.main.tab1.Tab1Fragment;
import com.ishow.noahark.modules.main.tab4.Tab4Fragment;
import com.ishow.noahark.modules.main.tab2.Tab2Fragment;
import com.ishow.noahark.modules.main.tab3.Tab3Fragment;
import com.ishow.common.widget.BottomBar;
import com.ishow.common.widget.YToast;
import com.ishow.noahark.modules.settings.SettingsActivity;

public class MainActivity extends AppBaseActivity implements BottomBar.OnBottomBarListener {
    private static final String TAG = "MainActivity";

    public static final int TAB_FIRST = R.id.tab_1;
    public static final int TAB_SECOND = R.id.tab_2;
    public static final int TAB_THIRD = R.id.tab_3;
    public static final int TAB_FOURTH = R.id.tab_4;


    private Tab1Fragment mHomeFragment;
    private Tab2Fragment mProductFragment;
    private Tab3Fragment mShoppingFragment;
    private Tab4Fragment mMineFragment;

    private TopBar mTopBar;
    private BottomBar mBottomBar;
    private long mLastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        int type = intent.getIntExtra(KEY_TYPE, TAB_FIRST);
        mBottomBar.setSelectedId(type, true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int type = intent.getIntExtra(KEY_TYPE, TAB_FIRST);
        mBottomBar.setSelectedId(type, true);
    }

    @Override
    protected void initViews() {
        super.initViews();
        mTopBar = (TopBar) findViewById(R.id.top_bar);

        mBottomBar = (BottomBar) findViewById(R.id.bottom_bar);
        mBottomBar.setOnSelectedChangedListener(this);
    }


    @Override
    public void onBackPressed() {
        final long nowTime = System.currentTimeMillis();
        if (nowTime - mLastTime < 2000) {
            super.onBackPressed();
        } else {
            mLastTime = nowTime;
            YToast.show(this, R.string.click_again_to_exit);
        }
    }


    @Override
    public void onSelectedChanged(ViewGroup parent, @IdRes int selectId, int index) {
        selectFragment(selectId);
        updateTopBar(selectId);
    }

    @Override
    public void onClickChlid(View v, boolean isSameView) {

    }

    public void updateTopBar(int selectId) {
        switch (selectId) {
            case R.id.tab_1:
                mTopBar.setText(R.string.tab_1);
                mTopBar.setRightImageVisibility(View.VISIBLE);
                mTopBar.setRightImageResource(R.drawable.ic_list);
                break;
            case R.id.tab_2:
                mTopBar.setText(R.string.tab_2);
                mTopBar.setRightImageVisibility(View.GONE);
                break;
            case R.id.tab_3:
                mTopBar.setText(R.string.tab_3);
                mTopBar.setRightImageVisibility(View.GONE);
                break;
            case R.id.tab_4:
                mTopBar.setText(R.string.tab_4);
                mTopBar.setRightImageVisibility(View.VISIBLE);
                mTopBar.setRightImageResource(R.drawable.ic_settings);
                break;
        }
    }

    public void selectFragment(int selectId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        switch (selectId) {
            case R.id.tab_1:
                if (mHomeFragment == null) {
                    mHomeFragment = Tab1Fragment.newInstance();
                }

                transaction.replace(R.id.content, mHomeFragment);
                transaction.commitAllowingStateLoss();

                break;
            case R.id.tab_2:
                if (mProductFragment == null) {
                    mProductFragment = Tab2Fragment.newInstance();
                }

                transaction.replace(R.id.content, mProductFragment);
                transaction.commitAllowingStateLoss();
                break;
            case R.id.tab_3:
                if (mShoppingFragment == null) {
                    mShoppingFragment = Tab3Fragment.newInstance();
                }
                transaction.replace(R.id.content, mShoppingFragment);
                transaction.commitAllowingStateLoss();
                break;
            case R.id.tab_4:
                if (mMineFragment == null) {
                    mMineFragment = Tab4Fragment.newInstance();
                }
                transaction.replace(R.id.content, mMineFragment);
                transaction.commitAllowingStateLoss();
                break;
        }
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        switch (mBottomBar.getSelectedId()) {
            case R.id.tab_1:
                onRightClickForTab1();
                break;
            case R.id.tab_4:
                onRightClickForTab4();
                break;
        }

    }

    private void onRightClickForTab1() {
        try {
            Intent intent = new Intent("com.yuhaiyang.androidcommon.Test");
            startActivity(intent);
        } catch (Exception e) {
            L.i(TAG, "Exception = " + e);
        }
    }

    private void onRightClickForTab4() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}



