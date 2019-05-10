package com.ishow.common.widget.watermark;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.ishow.common.R;

/**
 * Created by yuhaiyang on 2018/1/23.
 * 水印的参数
 */

public class WaterMarkHelp {
    /**
     * 释放一个全局控制的变量
     * 方便以后进行宏控制
     */
    private static boolean sShowWaterMark = true;
    private static String sDefaultText = "测试版本";
    private Paint mPaint;
    private Params mParams;
    private View mView;
    private int mTextWidth;
    private int mTextHeight;

    public void init(View view, @NonNull Params params) {
        mParams = params;
        mView = view;

        params.alpha = Math.min(1.0F, Math.max(0F, params.alpha));
        params.angle = Math.min(360, Math.max(0, params.angle));
        params.text = TextUtils.isEmpty(params.text) ? sDefaultText : params.text;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setTextSize(params.textSize);
        mPaint.setColor(params.textColor);
        mPaint.setAlpha((int) (params.alpha * 255));

        Rect rect = new Rect();
        mPaint.getTextBounds(params.text, 0, params.text.length(), rect);
        mTextWidth = rect.width() + params.startPadding + params.endPadding;
        mTextHeight = rect.height() + params.topPadding + params.bottomPadding;
    }

    public void draw(Canvas canvas, final int width, final int height) {
        if (!sShowWaterMark) {
            return;
        }

        if (mParams == null || mPaint == null || canvas == null) {
            return;
        }

        if (!mParams.enable) {
            return;
        }

        int count = canvas.saveLayer(0, 0, width, height, mPaint, Canvas.ALL_SAVE_FLAG);
        canvas.rotate(mParams.angle, width / 2, height / 2);

        final int startY = -height;
        final int endY = height + height;
        final int startX = -width;
        final int endX = width + width;

        for (int y = startY; y <= endY; y += mTextHeight) {
            for (int x = startX; x <= endX; x += mTextWidth) {
                canvas.drawText(mParams.text, x + mParams.startPadding, y + mParams.topPadding, mPaint);
            }
        }

        canvas.restoreToCount(count);
    }


    @SuppressWarnings("WeakerAccess")
    public void setWaterMarkEnable(boolean enable) {
        mParams.enable = enable;
        mView.postInvalidate();
    }

    @SuppressWarnings("WeakerAccess")
    public static int getDefaultTextSize(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.A_title);
    }

    @SuppressWarnings("WeakerAccess")
    public static int getDefaultTextColor(Context context) {
        return context.getResources().getColor(R.color.text_grey_light_normal);
    }

    @SuppressWarnings("WeakerAccess")
    public static int getDefaultPadding(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.dp_20);
    }

    @SuppressWarnings("WeakerAccess")
    public static float getDefaultAlpha() {
        return 0.2F;
    }

    public static int getDefaultAngle() {
        return 315;
    }

    public static void show(boolean show) {
        sShowWaterMark = show;
    }

    public static void defaultText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        sDefaultText = text;
    }

    public static class Params {
        public String text;
        public int textSize;
        public int textColor;
        public int topPadding;
        public int bottomPadding;
        public int startPadding;
        public int endPadding;
        public float alpha;
        public int angle;
        public boolean enable;
    }
}
