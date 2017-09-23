package com.ishow.noahark.modules.main.tab3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishow.noahark.R;
import com.ishow.noahark.modules.base.AppBaseFragment;
import com.ishow.common.widget.StatusView;
import com.ishow.common.widget.TopBar;

/**
 * Created by yuhaiyang on 2017/4/21.
 * Home Fragment
 */

public class Tab3Fragment extends AppBaseFragment {

    private View mRootView;

    public static Tab3Fragment newInstance() {

        Bundle args = new Bundle();

        Tab3Fragment fragment = new Tab3Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView != null) {
            return mRootView;
        }

        mRootView = inflater.inflate(R.layout.fragement_tab_3, container, false);

        StatusView statusView = (StatusView) mRootView.findViewById(R.id.status_view);
        statusView.showEmpty();
        return mRootView;
    }
}
