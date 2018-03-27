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
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.ishow.common.utils.ToastUtils;
import com.ishow.common.utils.router.AppRouter;
import com.ishow.common.widget.BottomBar;
import com.ishow.common.widget.TopBar;
import com.ishow.noahark.R;
import com.ishow.noahark.modules.base.AppBaseActivity;
import com.ishow.noahark.modules.main.tab1.Tab1Fragment;
import com.ishow.noahark.modules.main.tab2.Tab2Fragment;
import com.ishow.noahark.modules.main.tab3.Tab3Fragment;
import com.ishow.noahark.modules.main.tab4.Tab4Fragment;
import com.ishow.noahark.modules.settings.SettingsActivity;

import java.util.List;

public class MainActivity extends AppBaseActivity implements BottomBar.OnBottomBarListener {
    private static final String TAG = "MainActivity";

    public static final int TAB_FIRST = R.id.tab_1;
    public static final int TAB_SECOND = R.id.tab_2;
    public static final int TAB_THIRD = R.id.tab_3;
    public static final int TAB_FOURTH = R.id.tab_4;

    private Fragment mBeforeFragment;
    private Tab1Fragment mTab1Fragment;
    private Tab2Fragment mTab2Fragment;
    private Tab3Fragment mTab3Fragment;
    private Tab4Fragment mTab4Fragment;

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

        PackageManager pm = getPackageManager();
        /*List<ApplicationInfo> listAppcations = pm.getInstalledApplications(PackageManager.MATCH_UNINSTALLED_PACKAGES);
        for (ApplicationInfo app : listAppcations) {
            Log.i(TAG, "onCreate: ============================");

            String title = (String) app.loadLabel(pm);
            Drawable icon= app.loadIcon(pm);
            app.
            Log.i(TAG, "onCreate: title = " + title);
            Log.i(TAG, "onCreate: icon = " + icon);
        }*/

        final Intent intent2 = new Intent(Intent.ACTION_MAIN);
        intent2.addCategory(Intent.CATEGORY_LAUNCHER);
//        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent2, 0);
        Log.i(TAG, "onCreate: size = " + resolveInfos.size());
        for (ResolveInfo resolveInfo : resolveInfos) {
            Log.i(TAG, "onCreate3: ============================");

            String title = (String) resolveInfo.loadLabel(pm);

            Log.i(TAG, "onCreate3: title = " + title);
            if (resolveInfo.activityInfo == null) {
                Log.i(TAG, "onCreate: info is null");
            } else {
                Log.i(TAG, "onCreate3: pkg = " + resolveInfo.activityInfo.packageName);
            }
        }

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
        mTopBar = findViewById(R.id.top_bar);

        mBottomBar = findViewById(R.id.bottom_bar);
        mBottomBar.setOnSelectedChangedListener(this);
    }


    @Override
    public void onBackPressed() {
        final long nowTime = System.currentTimeMillis();
        if (nowTime - mLastTime < 2000) {
            super.onBackPressed();
        } else {
            mLastTime = nowTime;
            ToastUtils.show(this, R.string.click_again_to_exit);
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
        if (mBeforeFragment != null) {
            transaction.hide(mBeforeFragment);
        }
        switch (selectId) {
            case R.id.tab_1:
                if (mTab1Fragment == null) {
                    mTab1Fragment = Tab1Fragment.newInstance();
                }
                if (mTab1Fragment.isAdded()) {
                    transaction.show(mTab1Fragment);
                } else {
                    transaction.add(R.id.content, mTab1Fragment);
                }
                transaction.commit();

                mBeforeFragment = mTab1Fragment;
                break;
            case R.id.tab_2:
                if (mTab2Fragment == null) {
                    mTab2Fragment = Tab2Fragment.newInstance();
                }

                if (mTab2Fragment.isAdded()) {
                    transaction.show(mTab2Fragment);
                } else {
                    transaction.add(R.id.content, mTab2Fragment);
                }
                transaction.commit();

                mBeforeFragment = mTab2Fragment;
                break;
            case R.id.tab_3:
                if (mTab3Fragment == null) {
                    mTab3Fragment = Tab3Fragment.newInstance();
                }
                if (mTab3Fragment.isAdded()) {
                    transaction.show(mTab3Fragment);
                } else {
                    transaction.add(R.id.content, mTab3Fragment);
                }
                transaction.commit();

                mBeforeFragment = mTab3Fragment;
                break;
            case R.id.tab_4:
                if (mTab4Fragment == null) {
                    mTab4Fragment = Tab4Fragment.newInstance();
                }
                if (mTab4Fragment.isAdded()) {
                    transaction.show(mTab4Fragment);
                } else {
                    transaction.add(R.id.content, mTab4Fragment);
                }
                transaction.commit();

                mBeforeFragment = mTab4Fragment;
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
        AppRouter.with(this)
                .action("com.yuhaiyang.androidcommon.Test")
                .start();

    }

    private void onRightClickForTab4() {
        AppRouter.with(this)
                .target(SettingsActivity.class)
                .start();
    }
}



