package com.bright.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.bright.common.R;
import com.bright.common.model.IPosition;

/**
 * 角标提示的TextView
 */
public class PromptTextView extends AppCompatTextView {
    private static final String TAG = "PromptTextView";
    /**
     * 位置比例
     */
    private static final float POSITION_SCALE = 0.06f;
    /**
     * 是文字模式
     */
    public static final int MODE_TEXT = 0;

    /**
     * 是小圈圈模式
     */
    public static final int MODE_GRAPH = 1;

    private int mMode;

    private String mTextString;
    private int mTextColor;
    private int mTextSize;
    private int mPadding;
    private int mPromptBackgroundColor;
    private int mPosition;
    private int mRadius;

    private Paint mBackGroundPaint;
    private Paint mTextPaint;

    private RectF mFirstRectF;
    private RectF mUsedRectF;

    public PromptTextView(Context context) {
        this(context, null);
    }

    public PromptTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PromptTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PromptTextView);
        mMode = a.getInt(R.styleable.PromptTextView_promptMode, MODE_GRAPH);
        mTextString = a.getString(R.styleable.PromptTextView_text);
        mTextColor = a.getColor(R.styleable.PromptTextView_textColor, Color.WHITE);
        mTextSize = a.getDimensionPixelSize(R.styleable.PromptTextView_textSize, getDefaultTextSize(context));
        mPadding = a.getDimensionPixelSize(R.styleable.PromptTextView_padding, getDefaultPadding(context));
        mRadius = a.getDimensionPixelSize(R.styleable.PromptTextView_radius, getDefaultRadius(context));
        mPosition = a.getInt(R.styleable.PromptTextView_position, IPosition.LEFT);
        mPromptBackgroundColor = a.getColor(R.styleable.PromptTextView_promptBackground, Color.RED);
        a.recycle();

        init();
    }

    private void init() {

        initPaint();
        Rect textRect = new Rect();
        mFirstRectF = new RectF();
        mUsedRectF = new RectF();

        switch (mMode) {
            case MODE_TEXT:
                if (TextUtils.isEmpty(mTextString)) {
                    textRect.set(0, 0, mRadius, mRadius);
                    mFirstRectF.set(textRect);
                } else {
                    mTextPaint.getTextBounds(mTextString, 0, mTextString.length(), textRect);
                    mFirstRectF.set(textRect);
                    mFirstRectF.set(mFirstRectF.left - mPadding, mFirstRectF.top - mPadding, mFirstRectF.right + mPadding, mFirstRectF.bottom + mPadding);
                    mFirstRectF.offset(mPadding, textRect.height() + mPadding);
                }
                break;
            case MODE_GRAPH:
                mFirstRectF.set(0, 0, mRadius, mRadius);
                break;
        }

    }

    private void initPaint() {
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);


        mBackGroundPaint = new Paint();
        mBackGroundPaint.setAntiAlias(true);
        mBackGroundPaint.setDither(true);
        mBackGroundPaint.setColor(mPromptBackgroundColor);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        switch (mPosition) {
            case IPosition.LEFT:
                mUsedRectF.set(mFirstRectF);
                mUsedRectF.offset(width * POSITION_SCALE, height * POSITION_SCALE);
                break;
            case IPosition.RIGHT:
                mUsedRectF.set(mFirstRectF);
                mUsedRectF.offset(width * (1 - POSITION_SCALE) - mFirstRectF.width(), height * POSITION_SCALE);
                break;

        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(mUsedRectF, 999, 999, mBackGroundPaint);
        if (mMode == MODE_TEXT && !TextUtils.isEmpty(mTextString)) {
            canvas.drawText(mTextString, mUsedRectF.left + mPadding, mUsedRectF.bottom - mPadding, mTextPaint);
        }
    }

    /**
     * 获取默认标题字体大小
     */
    public int getDefaultTextSize(Context context) {
        return context.getResources().getDimensionPixelOffset(R.dimen.K_title);
    }

    /**
     * 获取默认标题字体大小
     */
    public int getDefaultPadding(Context context) {
        return context.getResources().getDimensionPixelOffset(R.dimen.dp_5);
    }

    /**
     * 获取默认标题字体大小
     */
    public int getDefaultRadius(Context context) {
        return context.getResources().getDimensionPixelOffset(R.dimen.dp_4);
    }
}
