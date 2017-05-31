package com.ishow.noahark.modules.main.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishow.noahark.R;
import com.ishow.noahark.modules.base.AppBaseFragment;
import com.ishow.noahark.modules.settings.SettingsActivity;
import com.ishow.common.widget.StatusView;
import com.ishow.common.widget.TopBar;

/**
 * Created by yuhaiyang on 2017/4/21.
 * Home Fragment
 */

public class MineFragment extends AppBaseFragment {

    private View mRootView;

    public static MineFragment newInstance() {

        Bundle args = new Bundle();

        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView != null) {
            return mRootView;
        }

        mRootView = inflater.inflate(R.layout.fragement_mine, container, false);
        TopBar topBar = (TopBar) mRootView.findViewById(R.id.top_bar);
        topBar.setOnTopBarListener(this);

        StatusView statusView = (StatusView) mRootView.findViewById(R.id.status_view);
        statusView.showError();
        return mRootView;
    }

    @Override
    public void onRightClick(View v) {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }
}
