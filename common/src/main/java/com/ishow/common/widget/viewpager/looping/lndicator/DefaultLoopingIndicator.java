package com.ishow.common.widget.viewpager.looping.lndicator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ishow.common.utils.UnitUtils;
import com.ishow.common.widget.viewpager.looping.ILoopingIndicator;

/**
 * Created by yuhaiyang on 2018/9/13.
 * 默认的
 */
public class DefaultLoopingIndicator implements ILoopingIndicator {
    private Paint mIndicatorPaint;
    private Paint mIndicatorBorderPaint;
    private int mIndicatorRadius;
    private int mIndicatorItemWidth;
    private int mIndicatorWidth;
    private int mIndicatorHeight;

    private int mWidth;
    private int mHeight;

    public DefaultLoopingIndicator() {
        mIndicatorRadius = UnitUtils.dip2px(3);
        mIndicatorItemWidth = 5 * mIndicatorRadius;
        mIndicatorHeight = 6 * mIndicatorRadius;

        mIndicatorPaint = new Paint();
        mIndicatorPaint.setAntiAlias(true);
        mIndicatorPaint.setDither(true);
        mIndicatorPaint.setColor(Color.WHITE);

        mIndicatorBorderPaint = new Paint();
        mIndicatorBorderPaint.setAntiAlias(true);
        mIndicatorBorderPaint.setDither(true);
        mIndicatorBorderPaint.setColor(Color.GRAY);
        mIndicatorBorderPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void onViewSizeChanged(int w, int h) {
        mWidth = w;
        mHeight = h;
    }

    @Override
    public void onDataSizeChanged(int count) {
        mIndicatorWidth = mIndicatorItemWidth * count;
    }

    @Override
    public void onDraw(Canvas canvas, final int scrollX, final int count, final int currentPosition, final float positionOffset) {
        final int nextPosition = currentPosition == count - 1 ? 0 : currentPosition + 1;
        final int startX = mWidth / 2 - mIndicatorWidth / 2 + scrollX;
        final int y = mHeight - mIndicatorHeight;
        int currentIndicatorRadius = (int) (mIndicatorRadius + mIndicatorRadius * (1 - positionOffset) * 0.38F);
        int nextIndicatorRadius = (int) (mIndicatorRadius + mIndicatorRadius * positionOffset * 0.38F);
        for (int i = 0; i < count; i++) {
            int x = startX + i * mIndicatorItemWidth + (mIndicatorItemWidth - mIndicatorRadius) / 2;
            if (i == currentPosition) {
                canvas.drawCircle(x, y, currentIndicatorRadius, mIndicatorPaint);
                canvas.drawCircle(x, y, currentIndicatorRadius, mIndicatorBorderPaint);
            } else if (i == nextPosition) {
                canvas.drawCircle(x, y, nextIndicatorRadius, mIndicatorPaint);
                canvas.drawCircle(x, y, nextIndicatorRadius, mIndicatorBorderPaint);
            } else {
                canvas.drawCircle(x, y, mIndicatorRadius, mIndicatorPaint);
                canvas.drawCircle(x, y, mIndicatorRadius, mIndicatorBorderPaint);
            }
        }
    }
}
