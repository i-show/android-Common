package com.brightyu.androidcommon.modules.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightyu.androidcommon.R;
import com.brightyu.androidcommon.modules.base.AppBaseFragment;
import com.ishow.common.utils.log.L;
import com.ishow.common.widget.TopBar;

/**
 * Created by yuhaiyang on 2017/4/21.
 * Home Fragment
 */

public class HomeFragment extends AppBaseFragment {
    private static final String TAG = "HomeFragment";

    private View mRootView;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView != null) {
            return mRootView;
        }

        mRootView = inflater.inflate(R.layout.fragement_home, container, false);
        TopBar topBar = (TopBar) mRootView.findViewById(R.id.top_bar);
        topBar.setOnTopBarListener(this);
        return mRootView;
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        try {
            Intent intent = new Intent("com.yuhaiyang.androidcommon.Test");
            startActivity(intent);
        } catch (Exception e) {
            L.i(TAG, "Exception = " + e);
        }

    }
}
