package com.ishow.noah.modules.main.tab1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishow.common.utils.StorageUtils;
import com.ishow.common.utils.router.AppRouter;
import com.ishow.common.widget.TopBar;
import com.ishow.noah.R;
import com.ishow.noah.modules.base.AppBaseFragment;
import com.ishow.noah.modules.sample.main.SampleMainActivity;

import java.util.concurrent.TimeUnit;

/**
 * Created by yuhaiyang on 2017/4/21.
 * Home Fragment
 */

public class Tab1Fragment extends AppBaseFragment {
    private static final String TAG = "Tab1Fragment";

    private View mRootView;

    public static Tab1Fragment newInstance() {

        Bundle args = new Bundle();

        Tab1Fragment fragment = new Tab1Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView != null) {
            return mRootView;
        }

        mRootView = inflater.inflate(R.layout.fragement_tab_1, container, false);
        TopBar topBar = mRootView.findViewById(R.id.top_bar);
        topBar.setOnTopBarListener(this);
        return mRootView;
    }

    @Override
    public void onLeftClick(View v) {
        super.onLeftClick(v);
        StorageUtils.with(getContext())
                .expire(20, TimeUnit.SECONDS)
                .param("hello", "world")
                .save();
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);

        String result = StorageUtils.with(getContext())
                .key("hello")
                .get("defult");
        Log.i(TAG, "onRightClick: result = " + result);
        //AppRouter.with(getContext())
        //        .target(SampleMainActivity.class)
        //        .start();
    }
}
