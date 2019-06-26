/*
 * Copyright (C) 2017. The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.common.modules.image.show;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.viewpager.widget.ViewPager;
import com.ishow.common.R;
import com.ishow.common.utils.DeviceUtils;
import com.ishow.common.widget.viewpager.indicator.SizeIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看大图的Dialog
 */
public class ShowPhotoDialog extends Dialog {
    private List<String> mUrls;
    private View mBeforeView;
    private int mCurrentPosition;
    private boolean isShowThumb = true;

    public ShowPhotoDialog(Context context) {
        super(context, R.style.Theme_Dialog_Black);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetStatusBar();
        setContentView(R.layout.widget_show_photo);
        ShowPhotoAdapter adapter = new ShowPhotoAdapter(getContext());
        adapter.setBeforView(mBeforeView);
        adapter.setData(mUrls);
        adapter.setDialog(this);
        adapter.setShowThumb(isShowThumb);

        ViewPager pager = findViewById(R.id.content);
        pager.setAdapter(adapter);
        pager.setCurrentItem(mCurrentPosition);

        SizeIndicator indicator = findViewById(R.id.state);
        indicator.setViewPager(pager);

        // 只有一张图片的时候不需要显示指示器
        if (mUrls.size() == 1) {
            indicator.setVisibility(View.GONE);
        }

    }

    public void setData(String url) {
        if (mUrls == null) {
            mUrls = new ArrayList<>();
        } else {
            mUrls.clear();
        }
        if (!TextUtils.isEmpty(url)) {
            mUrls.add(url);
        }
    }

    public void setData(List<String> urls) {
        if (urls == null) {
            mUrls = new ArrayList<>();
        } else {
            mUrls = urls;
        }
    }

    /**
     * 如果不需要 显示缩略图就不要设置
     */
    public void setBeforeView(View view) {
        mBeforeView = view;
    }

    public void setShowThumb(boolean showThumb) {
        isShowThumb = showThumb;
    }

    public void setCurrentPosition(int position) {
        mCurrentPosition = position;
    }


    @Override
    public void show() {
        super.show();
        int screen[] = DeviceUtils.INSTANCE.getScreenSize();
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = screen[0];
        lp.height = screen[1];
        window.setAttributes(lp);
    }

    private void resetStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    // remove the following flag for version < API 19
                    | View.SYSTEM_UI_FLAG_IMMERSIVE);

            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
