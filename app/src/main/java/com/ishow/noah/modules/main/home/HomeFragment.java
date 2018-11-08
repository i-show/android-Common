package com.ishow.noah.modules.main.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishow.common.utils.router.AppRouter;
import com.ishow.common.widget.TopBar;
import com.ishow.noah.R;
import com.ishow.noah.modules.base.AppBaseFragment;
import com.ishow.noah.modules.sample.main.SampleMainActivity;

/**
 * Created by yuhaiyang on 2017/4/21.
 * Home Fragment
 */

public class HomeFragment extends AppBaseFragment {

    private View mRootView;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView != null) {
            return mRootView;
        }

        mRootView = inflater.inflate(R.layout.fragement_tab_1, container, false);
        TopBar topBar = mRootView.findViewById(R.id.top_bar);
        topBar.setOnTopBarListener(this);
        return mRootView;
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);

        AppRouter.with(getContext())
                .target(SampleMainActivity.class)
                .start();
    }
}
