package com.brightyu.androidcommon.modules.main.shopping;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightyu.androidcommon.R;
import com.brightyu.androidcommon.modules.base.AppBaseFragment;
import com.ishow.common.widget.TopBar;

/**
 * Created by yuhaiyang on 2017/4/21.
 * Home Fragment
 */

public class ShoppingFragment extends AppBaseFragment {

    private View mRootView;

    public static ShoppingFragment newInstance() {

        Bundle args = new Bundle();

        ShoppingFragment fragment = new ShoppingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView != null) {
            return mRootView;
        }

        mRootView = inflater.inflate(R.layout.fragement_shopping, container, false);
        TopBar topBar = (TopBar) mRootView.findViewById(R.id.top_bar);
        topBar.setOnTopBarListener(this);
        return mRootView;
    }
}
