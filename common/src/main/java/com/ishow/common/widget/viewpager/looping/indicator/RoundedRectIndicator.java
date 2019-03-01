package com.ishow.common.widget.viewpager.looping.indicator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.ishow.common.utils.UnitUtils;
import com.ishow.common.widget.viewpager.looping.ILoopingIndicator;

/**
 * Created by yuhaiyang on 2018/9/13.
 * 圆角矩形
 */
public class RoundedRectIndicator implements ILoopingIndicator {
    private static final String TAG = "RoundedRectIndicator";
    private Paint mIndicatorPaint;
    private Paint mIndicatorBorderPaint;

    private int mWidth;

    private int mIndicatorWidth;
    private int mIndicatorGapSize;
    private int mIndicatorItemSize;
    private int mIndicatorItemIncreaseSize;

    private RectF mCurrentRect;
    private RectF mNextRect;
    private RectF mNormalRect;

    public RoundedRectIndicator() {
        mIndicatorPaint = new Paint();
        mIndicatorPaint.setAntiAlias(true);
        mIndicatorPaint.setDither(true);
        mIndicatorPaint.setColor(Color.WHITE);

        mIndicatorBorderPaint = new Paint();
        mIndicatorBorderPaint.setAntiAlias(true);
        mIndicatorBorderPaint.setDither(true);
        mIndicatorBorderPaint.setColor(Color.GRAY);
        mIndicatorBorderPaint.setStyle(Paint.Style.STROKE);

        mCurrentRect = new RectF();
        mNextRect = new RectF();
        mNormalRect = new RectF();

        mIndicatorItemSize = UnitUtils.dip2px(6);
        mIndicatorGapSize = UnitUtils.dip2px(6);
        mIndicatorItemIncreaseSize = mIndicatorItemSize * 2;
    }

    @Override
    public void onViewSizeChanged(int w, int h) {
        mWidth = w;
        final int bottom = (int) (h * 0.9F);
        mCurrentRect.top = bottom - mIndicatorItemSize;
        mCurrentRect.bottom = bottom;

        mNextRect.top = bottom - mIndicatorItemSize;
        mNextRect.bottom = bottom;

        mNormalRect.top = bottom - mIndicatorItemSize;
        mNormalRect.bottom = bottom;
    }

    @Override
    public void onDataSizeChanged(int count) {
        mIndicatorWidth = mIndicatorItemSize * count + mIndicatorGapSize * (count - 1) + mIndicatorItemIncreaseSize;
    }

    @Override
    public void onDraw(Canvas canvas, int scrollX, int count, int currentPosition, float positionOffset) {
        final int nextPosition = currentPosition == count - 1 ? 0 : currentPosition + 1;
        int currentWidth = mIndicatorItemSize + (int) (mIndicatorItemIncreaseSize * (1 - positionOffset));
        int nextWidth = mIndicatorItemSize + (int) (mIndicatorItemIncreaseSize * positionOffset);
        float left = mWidth / 2 - mIndicatorWidth / 2 + scrollX;
        for (int i = 0; i < count; i++) {
            // 求圆圈的圆心坐标
            if (i == currentPosition) {
                mCurrentRect.left = left;
                mCurrentRect.right = mCurrentRect.left + currentWidth;
                canvas.drawRoundRect(mCurrentRect, 100, 100, mIndicatorPaint);
                canvas.drawRoundRect(mCurrentRect, 100, 100, mIndicatorBorderPaint);
                left = mCurrentRect.right + mIndicatorGapSize;
            } else if (i == nextPosition) {
                mNextRect.left = left;
                mNextRect.right = mNextRect.left + nextWidth;
                canvas.drawRoundRect(mNextRect, 100, 100, mIndicatorPaint);
                canvas.drawRoundRect(mNextRect, 100, 100, mIndicatorBorderPaint);
                left = mNextRect.right + mIndicatorGapSize;
            } else {
                mNormalRect.left = left;
                mNormalRect.right = mNormalRect.left + mIndicatorItemSize;
                canvas.drawRoundRect(mNormalRect, 100, 100, mIndicatorPaint);
                canvas.drawRoundRect(mNormalRect, 100, 100, mIndicatorBorderPaint);
                left = mNormalRect.right + mIndicatorGapSize;
            }
        }
    }
}
