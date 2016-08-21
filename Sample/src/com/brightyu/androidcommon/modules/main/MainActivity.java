/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brightyu.androidcommon.modules.main;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.bright.common.app.BaseActivity;
import com.bright.common.constant.Position;
import com.bright.common.widget.TopBar;
import com.bright.common.widget.prompt.IPrompt;
import com.bright.common.widget.prompt.PromptImageView;
import com.bright.common.widget.prompt.PromptTextView;
import com.brightyu.androidcommon.R;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initViews() {
        super.initViews();
        TopBar topBar = (TopBar) findViewById(R.id.top_bar);

        PromptImageView image = topBar.getRightImageView();
        image.setMode(IPrompt.MODE_GRAPH)
                .setPromptPosition(Position.RIGHT)
                .commit();

        PromptImageView image2 = topBar.getRightImageView2();
        image2.setMode(IPrompt.MODE_GRAPH)
                .setPromptPosition(Position.RIGHT)
                .commit();

        IPrompt rightText = topBar.getRightTextView();
        rightText.setMode(IPrompt.MODE_GRAPH)
                .setPromptPosition(Position.RIGHT)
                .commit();

        PromptTextView left = topBar.getLeftTextView();
        left.setPadding(left.getPaddingLeft(), left.getPaddingTop(), left.getPaddingRight() * 4, left.getPaddingBottom());
        left.setMode(IPrompt.MODE_TEXT)
                .setPromptPosition(Position.RIGHT)
                .setPromptText(1)
                .setPromptHeightPaddingScale(0.06f)
                .setPromptWidthPaddingScale(0.06f)
                .commit();

        LinearLayout ll = (LinearLayout) findViewById(R.id.content);

        ll.addView(test());
    }


    private PromptTextView test() {
        PromptTextView mLeftTextView = new PromptTextView(this);
        mLeftTextView.setId(com.bright.common.R.id.leftText);
        mLeftTextView.setText("Hello");
        mLeftTextView.setPadding(10, 10, 10, 10);
        mLeftTextView.setGravity(Gravity.CENTER);
        mLeftTextView.setTextColor(Color.GREEN);
        mLeftTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40);
        //mLeftTextView.setOnClickListener(this);
        mLeftTextView.setLines(1);
        // 至少要这么宽 位了美观
        mLeftTextView.setMinWidth(100);
        mLeftTextView.setEllipsize(TextUtils.TruncateAt.END);

        mLeftTextView.setBackgroundResource(android.R.color.holo_orange_light);
        return mLeftTextView;
    }

    @Override
    public void onLeftClick(View v) {
        super.onLeftClick(v);
        Log.i(TAG, "onLeftClick: id = " + v.getId());
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        Log.i(TAG, "onRightClick: id = " + v.getId());
    }

    @Override
    public void onTitleClick(View v) {
        super.onTitleClick(v);
        Log.i(TAG, "onTitleClick: id = " + v.getId());
    }
}
