/**
 * Copyright (C) 2015  Haiyang Yu Android Source Project
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
package com.bright.common.utils.glide.transform;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class GlideRoundTransform extends BitmapTransformation {
    private float mRadius = 0f;
    private Paint mBorderPaint;
    private float mBorderWidth;

    public GlideRoundTransform(Context context) {
        this(context, 4);
    }

    public GlideRoundTransform(Context context, int radius) {
        this(context, radius, 0, 0);
    }

    public GlideRoundTransform(Context context, int radius, int borderWidth, int borderColor) {
        super(context);
        mRadius = radius;
        mBorderWidth = borderWidth;

        mBorderPaint = new Paint();
        mBorderPaint.setDither(true);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }


    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        // border 要居中即要压在图片上
        final int border = (int) (mBorderWidth / 2);
        final int width = (int) (source.getWidth() - mBorderWidth);
        final int height = (int) (source.getHeight() - mBorderWidth);

        Bitmap result = pool.get(width, height, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(border, border, width - border, height - border);
        canvas.drawRoundRect(rectF, mRadius, mRadius, paint);

        if (mBorderPaint != null) {
            canvas.drawRoundRect(rectF, mRadius, mRadius, mBorderPaint);
        }
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName() + Math.round(mRadius);
    }
}