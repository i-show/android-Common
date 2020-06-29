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
    private static final int originalColor = Color.GRAY;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        final float alpha = Math.abs(Float.valueOf(verticalOffset)) / appBarLayout.getTotalScrollRange();
        final View view = appBarLayout.findViewById(R.id.large_title);
        view.setAlpha(1 - alpha);


        final int nowColor = Color.argb((int) (alpha * 255), Color.red(originalColor), Color.green(originalColor), Color.blue(originalColor));
        final Toolbar toolbar = appBarLayout.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(nowColor);
    }
}
