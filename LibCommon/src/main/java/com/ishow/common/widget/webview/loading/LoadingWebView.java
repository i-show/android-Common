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

package com.ishow.common.widget.webview.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.ishow.common.R;


public class LoadingWebView extends WebView {
    private static final String TAG = "LoadingWebView";
    private LoadingWebChromeClient mLoadingWebChromeClient;
    private Paint mLoadingPaint;
    private int mProgress;
    private int mLoadingHeight;

    public LoadingWebView(Context context) {
        super(context);
        init(context, null);
    }

    public LoadingWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LoadingWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingWebView);
        int color = a.getColor(R.styleable.LoadingWebView_strokeColor, getDefaultLoadingColor());
        mLoadingHeight = a.getColor(R.styleable.LoadingWebView_strokeHeight, getDefaultLoadingHeight());
        a.recycle();


        mLoadingWebChromeClient = new LoadingWebChromeClient(this);
        super.setWebChromeClient(mLoadingWebChromeClient);

        mLoadingPaint = new Paint();
        mLoadingPaint.setDither(true);
        mLoadingPaint.setAntiAlias(true);
        mLoadingPaint.setColor(color);
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        mLoadingWebChromeClient.setRealWebChromeClient(client);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLoading(canvas);
    }

    /**
     * 根据进度来画一个进度条
     */
    private void drawLoading(Canvas canvas) {
        if (mProgress < 100) {
            int right = mProgress * getWidth() / 100;
            canvas.drawRect(0, getScrollY(), right, getScrollY() + mLoadingHeight, mLoadingPaint);
        }
    }

    /**
     * 需要主动调用
     * {@link android.webkit.WebChromeClient#onProgressChanged(WebView, int)}
     */
    public void updateLoading(int progress) {
        mProgress = progress;
        postInvalidate(0, getScrollY(), getWidth(), mLoadingHeight + getScrollY());
    }


    private int getDefaultLoadingColor() {
        return getContext().getResources().getColor(R.color.color_primary);
    }

    private int getDefaultLoadingHeight() {
        return getContext().getResources().getDimensionPixelOffset(R.dimen.dp_3);
    }
}
