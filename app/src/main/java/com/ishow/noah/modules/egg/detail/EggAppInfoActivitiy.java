
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

package com.ishow.noah.modules.egg.detail;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ishow.common.utils.AppUtils;
import com.ishow.common.utils.DateUtils;
import com.ishow.common.utils.DeviceUtils;
import com.ishow.common.utils.StringUtils;
import com.ishow.common.widget.textview.TextViewPro;
import com.ishow.noah.BuildConfig;
import com.ishow.noah.R;
import com.ishow.noah.modules.base.AppBaseActivity;

/**
 * Created by yuhaiyang on 2017/6/1.
 * 彩蛋详情
 */

public class EggAppInfoActivitiy extends AppBaseActivity {
    private LinearLayout mRootView;
    private int mItemMinHeight;
    private int mCateTextSize;
    private int mCateTextColor;
    private int mTipMinWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egg_app_info);
        init();
    }

    @Override
    protected void initNecessaryData() {
        super.initNecessaryData();
        mItemMinHeight = getResources().getDimensionPixelSize(R.dimen.item_height);
        mCateTextSize = getResources().getDimensionPixelSize(R.dimen.I_title);
        mCateTextColor = getResources().getColor(R.color.text_grey_light_normal);
        mTipMinWidth = getResources().getDimensionPixelSize(R.dimen.dp_100);
    }

    @Override
    protected void initViews() {
        super.initViews();
        mRootView = findViewById(R.id.root);
    }


    private void init() {
        int[] screen = DeviceUtils.getScreenSize();
        addCate("Android");
        addItem("分辨率：", StringUtils.plusString(screen[1], "*", screen[0]));
        addItem("手机型号：", DeviceUtils.model());
        addItem("手机版本：", DeviceUtils.version());
        addItem("最小宽度：", getResources().getConfiguration().smallestScreenWidthDp);

        addCate("App");
        addItem("包名：", getPackageName());
        addItem("版本号：", AppUtils.getVersionName(this));
        addItem("版本编号：", AppUtils.getVersionCode(this));
        addItem("版本类型：", BuildConfig.VERSION_DESCRIPTION);
        addItem("发布时间：", DateUtils.format(BuildConfig.RELEASE_TIME, DateUtils.FORMAT_YMDHMS));
    }

    private String getVersionType() {
        String versionType = null;
        switch (BuildConfig.VERSION_TYPE) {
            case BuildConfig.VERSION_DEV:
                versionType = "开发版本";
                break;
            case BuildConfig.VERSION_SIT:
                versionType = "SIT版本";
                break;
            case BuildConfig.VERSION_UAT:
                versionType = "UAT版本";
                break;
            case BuildConfig.VERSION_PROD:
                versionType = "正式版本";
                break;
        }
        return versionType;
    }


    private void addCate(String name) {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setGravity(Gravity.CENTER);
        container.setBackgroundResource(android.R.color.white);
        container.setMinimumHeight((int) (mItemMinHeight * 0.6f));

        View line = new View(this);
        line.setBackgroundResource(R.color.line);
        line.setLayoutParams(getLineParams());
        container.addView(line);

        TextView textView = new AppCompatTextView(this);
        textView.setTextColor(mCateTextColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mCateTextSize);
        textView.setText(name);
        container.addView(textView);

        line = new View(this);
        line.setBackgroundResource(R.color.line);
        line.setLayoutParams(getLineParams());
        container.addView(line);
        mRootView.addView(container);
    }


    private void addItem(String name, int value) {
        addItem(name, String.valueOf(value));
    }

    private void addItem(String name, String value) {
        TextViewPro textview = new TextViewPro(this);
        textview.setMinimumHeight(mItemMinHeight);
        textview.setBackgroundResource(android.R.color.white);
        textview.setLeftText(name);
        textview.setLeftTextMinWidth(mTipMinWidth);
        textview.setLeftTextGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        textview.setText(value);
        textview.setTextColor(mCateTextColor);
        textview.setLeftImageVisibility(View.GONE);
        textview.setRightImageVisibility(View.GONE);
        mRootView.addView(textview);
    }


    private LinearLayout.LayoutParams getLineParams() {
        final int screenWidth = DeviceUtils.getScreenSize()[0];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 1);
        lp.weight = 1;
        lp.leftMargin = screenWidth / 10;
        lp.rightMargin = screenWidth / 10;
        return lp;
    }
}
