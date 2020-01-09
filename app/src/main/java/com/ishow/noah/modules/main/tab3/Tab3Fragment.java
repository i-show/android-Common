package com.ishow.noah.modules.main.tab3;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishow.noah.R;
import com.ishow.noah.modules.base.AppBaseFragment;
import com.ishow.common.widget.StatusView;

import org.jetbrains.annotations.NotNull;

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

        mRootView = inflater.inflate(R.layout.fragment_tab_3, container, false);

        StatusView statusView = (StatusView) mRootView.findViewById(R.id.status_view);
        statusView.showEmpty();
        return mRootView;
    }

    @Override
    public void onViewCreated(@NotNull View view, @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
