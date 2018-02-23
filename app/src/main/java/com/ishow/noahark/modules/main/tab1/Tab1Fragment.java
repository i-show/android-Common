package com.ishow.noahark.modules.main.tab1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishow.noahark.R;
import com.ishow.noahark.modules.base.AppBaseFragment;

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
        return mRootView;
    }


}
