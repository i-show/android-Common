package com.ishow.common.widget.label;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.DimenRes;
import androidx.annotation.IntDef;
import com.ishow.common.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class LabelViewHelper {
    private static final String TAG = "LabelViewHelper";

    private String mText;
    private int mDistance;
    private int mHeight;
    private int mBackgroundColor;
    private int mStrokeColor;
    private int mStrokeWidth;
    private int mTextSize;
    private int mTextStyle;
    private int mTextColor;
    private int mLabelGravity;
    private int mAlpha;
    private boolean isEnable;

    private Paint mRectPaint;
    private Paint mRectStrokePaint;
    private Paint mTextPaint;
    // simulator
    private Path mRectPath;
    private Path mTextPath;
    private Rect mTextBound;

    private Context mContext;
    private ILabelView mLabelView;

    public LabelViewHelper(ILabelView view, Context context, AttributeSet attrs, int defStyleAttr) {
        mLabelView = view;
        mContext = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LabelView, defStyleAttr, 0);
        mText = a.getString(R.styleable.LabelView_text);
        mTextSize = a.getDimensionPixelSize(R.styleable.LabelView_textSize, getDefaultTextSize());
        mTextColor = a.getColor(R.styleable.LabelView_textColor, Color.WHITE);
        mTextStyle = a.getInt(R.styleable.LabelView_textStyle, 0);

        mStrokeWidth = a.getDimensionPixelSize(R.styleable.LabelView_strokeWidth, 0);
        mStrokeColor = a.getColor(R.styleable.LabelView_strokeColor, Color.TRANSPARENT);

        mHeight = a.getDimensionPixelSize(R.styleable.LabelView_labelHeight, getDefaultHeight());
        mDistance = a.getDimensionPixelSize(R.styleable.LabelView_labelDistance, getDefaultDistance());
        mBackgroundColor = a.getColor(R.styleable.LabelView_backgroundColor, getDefaultBackgroundColor());
        isEnable = a.getBoolean(R.styleable.LabelView_labelEnable, true);
        mLabelGravity = a.getInteger(R.styleable.LabelView_labelGravity, Gravity.RIGHT_TOP);
        a.recycle();

        mRectPaint = new Paint();
        mRectPaint.setDither(true);
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStyle(Paint.Style.FILL);

        mRectStrokePaint = new Paint();
        mRectStrokePaint.setDither(true);
        mRectStrokePaint.setAntiAlias(true);
        mRectStrokePaint.setStyle(Paint.Style.STROKE);
        mRectStrokePaint.setColor(mStrokeColor);
        mRectStrokePaint.setStrokeWidth(mStrokeWidth);

        mRectPath = new Path();
        mRectPath.reset();

        mTextPath = new Path();
        mTextPath.reset();

        mTextBound = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setDither(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeJoin(Paint.Join.ROUND);
        mTextPaint.setStrokeCap(Paint.Cap.SQUARE);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTypeface(Typeface.defaultFromStyle(mTextStyle));
        if (!TextUtils.isEmpty(mText)) {
            mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
        }

        // 检测有效性
        checkVaild();
    }

    /**
     * 检测是否有效
     */
    private void checkVaild() {
        if (mLabelView == null || mLabelView.getView() == null) {
            throw new IllegalArgumentException("need a label view");
        }
    }

    public void onDraw(Canvas canvas) {
        if (!isEnable || mText == null) {
            return;
        }
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();

        float actualDistance = mDistance + mHeight / 2F;
        calculatePath(width, height);

        // 画背景
        mRectPaint.setColor(mBackgroundColor);
        if (mAlpha != 0) {
            mRectPaint.setAlpha(mAlpha);
        }
        canvas.drawPath(mRectPath, mRectPaint);

        // 画描边
        if (mStrokeWidth > 0) {
            canvas.drawPath(mRectPath, mRectStrokePaint);
        }

        // 画文本
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        float x = (float) (actualDistance / (Math.sin(Math.PI * 45 / 180)) / 2 - mTextBound.width() / 2);
        if (x < 0) x = 0;

        float y = (fontMetrics.top - fontMetrics.bottom) / 2F - fontMetrics.top;
        canvas.drawTextOnPath(mText, mTextPath, x, y, mTextPaint);
    }

    /**
     * 计算图形
     */
    private void calculatePath(int width, int height) {
        float startX = width - mDistance - mHeight;
        float startY = height - mDistance - mHeight;
        float middle = mHeight / 2F;

        switch (mLabelGravity) {
            case Gravity.LEFT_TOP:
                mRectPath.reset();
                mRectPath.moveTo(0, mDistance);
                mRectPath.lineTo(mDistance, 0);
                mRectPath.lineTo(mDistance + mHeight, 0);
                mRectPath.lineTo(0, mDistance + mHeight);
                mRectPath.close();

                mTextPath.reset();
                mTextPath.moveTo(0, mDistance + middle);
                mTextPath.lineTo(mDistance + middle, 0);
                mTextPath.close();
                break;
            case Gravity.RIGHT_TOP:
                mRectPath.reset();
                mRectPath.moveTo(startX, 0);
                mRectPath.lineTo(startX + mHeight, 0);
                mRectPath.lineTo((float) width, mDistance);
                mRectPath.lineTo((float) width, mDistance + mHeight);
                mRectPath.close();

                mTextPath.reset();
                mTextPath.moveTo(startX + middle, 0);
                mTextPath.lineTo((float) width, mDistance + middle);
                mTextPath.close();
                break;
            case Gravity.LEFT_BOTTOM:
                mRectPath.reset();
                mRectPath.moveTo(0, startY);
                mRectPath.lineTo(mDistance + mHeight, (float) height);
                mRectPath.lineTo(mDistance, (float) height);
                mRectPath.lineTo(0, startY + mHeight);
                mRectPath.close();

                mTextPath.reset();
                mTextPath.moveTo(0, startY + middle);
                mTextPath.lineTo(mDistance + middle, (float) height);
                mTextPath.close();

                break;
            case Gravity.RIGHT_BOTTOM:
                mRectPath.reset();
                mRectPath.moveTo(startX, (float) height);
                mRectPath.lineTo(width, startY);
                mRectPath.lineTo(width, startY + mHeight);
                mRectPath.lineTo(startX + mHeight, (float) height);
                mRectPath.close();

                mTextPath.reset();
                mTextPath.moveTo(startX + middle, (float) height);
                mTextPath.lineTo((float) width, startY + middle);
                mTextPath.close();

                break;
        }
    }

    public void setLabelHeight(@DimenRes int heightRes) {
        final int height = mContext.getResources().getDimensionPixelOffset(heightRes);
        if (mHeight != height) {
            mHeight = height;
            invalidate();
        }
    }

    public int getLabelHeight() {
        return mHeight;
    }

    public void setLabelDistance(@DimenRes int distanceRes) {
        final int distance = mContext.getResources().getDimensionPixelOffset(distanceRes);
        if (mDistance != distance) {
            mDistance = distance;
            invalidate();
        }
    }

    public int getLabelDistance() {
        return mDistance;
    }

    public boolean isLabelEnable() {
        return isEnable;
    }

    public void setLabelEnable(boolean visual) {
        if (isEnable != visual) {
            isEnable = visual;
            invalidate();
        }
    }

    public int getLabelGravity() {
        return mLabelGravity;
    }

    public void setLabelGravity(@Gravity int gravity) {
        if (mLabelGravity != gravity) {
            mLabelGravity = gravity;
            invalidate();
        }
    }

    public int getLabelTextColor() {
        return mTextColor;
    }

    public void setLabelTextColor(@ColorInt int textColor) {
        if (mTextColor != textColor) {
            mTextColor = textColor;
            invalidate();
        }
    }

    public int getLabelBackgroundColor() {
        return mBackgroundColor;
    }

    public void setLabelBackgroundColor(@ColorInt int backgroundColor) {
        if (mBackgroundColor != backgroundColor) {
            mBackgroundColor = backgroundColor;
            invalidate();
        }
    }


    @SuppressWarnings("unused")
    public void setLabelBackgroundAlpha(int alpha) {
        if (mAlpha != alpha) {
            mAlpha = alpha;
            invalidate();
        }
    }

    public String getLabelText() {
        return mText;
    }

    public void setLabelText(String text) {
        if (!TextUtils.equals(text, mText)) {
            mText = text;
            if (!TextUtils.isEmpty(mText)) {
                mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
            }
            invalidate();
        }
    }

    public int getLabelTextSize() {
        return mTextSize;
    }

    public void setLabelTextSize(@DimenRes int textSizeRes) {
        final int textSize = mContext.getResources().getDimensionPixelSize(textSizeRes);
        if (mTextSize != textSize) {
            mTextSize = textSize;
            invalidate();
        }
    }

    public void setLabelTextStyle(@TextStyle int textStyle) {
        if (mTextStyle == textStyle) {
            Log.i(TAG, "setLabelTextStyle: is same Style");
            return;
        }

        mTextStyle = textStyle;
        mTextPaint.setTypeface(Typeface.defaultFromStyle(mTextStyle));
        invalidate();
    }

    public void setLabelStrokeColor(@ColorInt int color) {
        if (mStrokeColor != color) {
            mStrokeColor = color;
            mRectStrokePaint.setColor(mStrokeColor);
            invalidate();
        }
    }

    public void setLabelStrokeWidth(@DimenRes int widthRes) {
        final int width = mContext.getResources().getDimensionPixelOffset(widthRes);
        if (mStrokeWidth != width) {
            mStrokeWidth = width;
            mRectStrokePaint.setStrokeWidth(mStrokeWidth);
            invalidate();
        }
    }

    private void invalidate() {
        mLabelView.getView().invalidate();
    }


    private int getMeasuredWidth() {
        return mLabelView.getView().getMeasuredWidth();
    }

    private int getMeasuredHeight() {
        return mLabelView.getView().getMeasuredHeight();
    }

    private int getDefaultTextSize() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.J_title);
    }

    private int getDefaultHeight() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.dp_30);
    }

    private int getDefaultDistance() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.dp_25);
    }

    private int getDefaultBackgroundColor() {
        return mContext.getResources().getColor(R.color.color_primary);
    }


    @IntDef({Gravity.LEFT_TOP, Gravity.RIGHT_TOP, Gravity.LEFT_BOTTOM, Gravity.RIGHT_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Gravity {
        int LEFT_TOP = 1;
        int RIGHT_TOP = 2;
        int LEFT_BOTTOM = 3;
        int RIGHT_BOTTOM = 4;
    }


    @IntDef({TextStyle.NORMAL, TextStyle.BOLD, TextStyle.ITALIC, TextStyle.BOLD_ITALIC})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextStyle {
        int NORMAL = 0;
        int BOLD = 1;
        int ITALIC = 2;
        int BOLD_ITALIC = 3;
    }


}