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

package com.brightyu.androidcommon.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class TestView extends View {

    private Resources mResources;
    private Paint mBitPaint;
    private Bitmap mBitmap;

    private int mTotalWidth, mTotalHeight;
    private int mBitWidth, mBitHeight;
    private Rect mSrcRect, mDestRect;
    private PorterDuffXfermode mXfermode;

    private Rect mDynamicRect;
    private int mCurrentTop;
    private int mStart, mEnd;
    private Bitmap srcBitmap, dstBitmap;

    public TestView(Context context) {
        super(context);
        mResources = getResources();
        initPaint();
        initXfermode();
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mResources = getResources();
        initPaint();
        initXfermode();
    }

    private void initXfermode() {
        // \u53e0\u52a0\u5904\u7ed8\u5236\u6e90\u56fe
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    }

    private void initPaint() {
        // \u521d\u59cb\u5316paint
        mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mBitPaint.setDither(true);
    }

    //\u521b\u5efa\u4e00\u4e2a\u5706\u5f62bitmap\uff0c\u4f5c\u4e3adst\u56fe
    private Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFFFFCC44);
        c.drawCircle(w / 3, h / 3, w / 3, p);
        return bm;
    }

    // \u521b\u5efa\u4e00\u4e2a\u77e9\u5f62bitmap\uff0c\u4f5c\u4e3asrc\u56fe
    private Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFF66AAFF);
        c.drawRect(w / 3, h / 3, w, h, p);
        return bm;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        srcBitmap = makeSrc(getWidth(), getHeight());
        dstBitmap = makeDst(getWidth(), getHeight());
        // \u5b58\u4e3a\u65b0\u56fe\u5c42
        int saveLayerCount = canvas.saveLayer(0, 0, width, height, mBitPaint, Canvas.ALL_SAVE_FLAG);
        //int saveLayerCount = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
//        mBitPaint.setColor(Color.GREEN);
//        canvas.drawCircle(width / 3, height / 3, width / 3, mBitPaint);
//
//        // \u8bbe\u7f6e\u6df7\u5408\u6a21\u5f0f
//        // \u7ed8\u5236\u6e90\u56fe\u5f62
//        mBitPaint.setColor(Color.BLUE);
//        canvas.drawRect(width / 3, height / 3, width, height, mBitPaint);
//        // \u6e05\u9664\u6df7\u5408\u6a21\u5f0f
//        mBitPaint.setXfermode(null);


        canvas.drawBitmap(dstBitmap, width / 3, height / 3, mBitPaint);
        //\u8bbe\u7f6ePaint\u7684Xfermode
        mBitPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        canvas.drawBitmap(srcBitmap, width / 3, height / 3, mBitPaint);
        mBitPaint.setXfermode(null);
        // \u8fd8\u539f\u753b\u5e03


        // \u6062\u590d\u4fdd\u5b58\u7684\u56fe\u5c42\uff1b
        canvas.restoreToCount(saveLayerCount);

    }
}
