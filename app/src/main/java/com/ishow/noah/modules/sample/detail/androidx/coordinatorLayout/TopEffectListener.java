package com.ishow.noah.modules.sample.detail.androidx.coordinatorLayout;

import android.graphics.Color;
import android.view.View;


import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.ishow.noah.R;

/**
 * Created by yuhaiyang on 2020/6/29.
 */
public class TopEffectListener implements AppBarLayout.OnOffsetChangedListener {

    /**
     * ToolsBar 标题的颜色
     */
    private static final int TITLE_COLOR = Color.DKGRAY;

    private int mToolBarId;

    public TopEffectListener(int toolBarId) {
        mToolBarId = toolBarId;
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        final float alpha = Math.abs(Float.valueOf(verticalOffset)) / appBarLayout.getTotalScrollRange();
        final View view = appBarLayout.findViewById(R.id.large_title);
        view.setAlpha(1 - alpha);

        final int nowColor = Color.argb((int) (alpha * 255), Color.red(TITLE_COLOR), Color.green(TITLE_COLOR), Color.blue(TITLE_COLOR));
        final Toolbar toolbar = appBarLayout.findViewById(mToolBarId);
        if (toolbar != null) {
            toolbar.setTitleTextColor(nowColor);
        }
    }
}
