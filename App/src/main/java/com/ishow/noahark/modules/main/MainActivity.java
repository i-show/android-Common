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

import com.ishow.noahark.R;
import com.ishow.noahark.modules.base.AppBaseActivity;
import com.ishow.noahark.modules.main.home.HomeFragment;
import com.ishow.noahark.modules.main.mine.MineFragment;
import com.ishow.noahark.modules.main.product.ProductFragment;
import com.ishow.noahark.modules.main.shopping.ShoppingFragment;
import com.ishow.common.widget.BottomBar;
import com.ishow.common.widget.YToast;

public class MainActivity extends AppBaseActivity implements BottomBar.OnBottomBarListener {
    private static final String TAG = "MainActivity";

    public static final int TYPE_HOME = R.id.home;
    public static final int TYPE_PROD = R.id.prod;
    public static final int TYPE_SHIPPING = R.id.shopping;
    public static final int TYPE_MINE = R.id.mine;


    private HomeFragment mHomeFragment;
    private ProductFragment mProductFragment;
    private ShoppingFragment mShoppingFragment;
    private MineFragment mMineFragment;

    private BottomBar mBottomBar;
    private long mLastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        int type = intent.getIntExtra(KEY_TYPE, TYPE_HOME);
        mBottomBar.setSelectedId(type, true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int type = intent.getIntExtra(KEY_TYPE, TYPE_HOME);
        mBottomBar.setSelectedId(type, true);
    }

    @Override
    protected void initViews() {
        super.initViews();
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
    }

    @Override
    public void onClickChlid(View v, boolean isSameView) {

    }

    public void selectFragment(int selectId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        switch (selectId) {
            case R.id.home:
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.newInstance();
                }

                transaction.replace(R.id.content, mHomeFragment);
                transaction.commitAllowingStateLoss();

                break;
            case R.id.prod:
                if (mProductFragment == null) {
                    mProductFragment = ProductFragment.newInstance();
                }

                transaction.replace(R.id.content, mProductFragment);
                transaction.commitAllowingStateLoss();
                break;
            case R.id.shopping:
                if (mShoppingFragment == null) {
                    mShoppingFragment = ShoppingFragment.newInstance();
                }
                transaction.replace(R.id.content, mShoppingFragment);
                transaction.commitAllowingStateLoss();
                break;
            case R.id.mine:
                if (mMineFragment == null) {
                    mMineFragment = MineFragment.newInstance();
                }
                transaction.replace(R.id.content, mMineFragment);
                transaction.commitAllowingStateLoss();
                break;
        }
    }
}



