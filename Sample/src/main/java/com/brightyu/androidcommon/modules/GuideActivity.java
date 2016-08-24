/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brightyu.androidcommon.modules;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.brightyu.androidcommon.R;
import com.brightyu.androidcommon.modules.base.AppBaseActivity;
import com.brightyu.androidcommon.modules.login.LoginActivity;


/**
 * 引导页面
 */
public class GuideActivity extends AppBaseActivity implements View.OnTouchListener, View.OnClickListener {
    private static final String TAG = "GuideActivity";
    private LayoutInflater mLayoutInflater;

    private static final int GUIDE_IMAGES[] = {
            R.drawable.guide_bg_1,
            R.drawable.guide_bg_1,
            R.drawable.guide_bg_3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mLayoutInflater = LayoutInflater.from(this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOnTouchListener(this);
        viewPager.setAdapter(mPagerAdapter);
    }

    /**
     * 当前界面不需要显示升级dialog
     */
    @Override
    protected boolean needShowUpdateVersionDialog() {
        return false;
    }


    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return GUIDE_IMAGES.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View item = mLayoutInflater.inflate(R.layout.item_guide_item, null);
            item.setBackgroundResource(GUIDE_IMAGES[position]);
            View comeIn = item.findViewById(R.id.come_in);
            comeIn.setOnClickListener(GuideActivity.this);
            comeIn.setEnabled(position == 2);
            comeIn.setVisibility(position == 2 ? View.VISIBLE : View.INVISIBLE);

            container.addView(item);
            return item;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object child) {
            View view = (View) child;
            view.setOnClickListener(null);
            container.removeView(view);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.come_in:
                goToNext();
                break;
            case R.id.view_pager:
                resetStatusBar();
                break;
        }

    }

    private void goToNext() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        GuideActivity.this.finish();
    }


    @Override
    protected void resetStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    // remove the following flag for version < API 19
                    | View.SYSTEM_UI_FLAG_IMMERSIVE);

            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }


    protected void hideStatusBar(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    // remove the following flag for version < API 19
                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            hideStatusBar(v);
        }
        return false;
    }
}
