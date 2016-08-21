package com.bright.common.widget.prompt;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.FloatRange;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.bright.common.R;
import com.bright.common.constant.Position;

/**
 * 角标提示的TextView
 */
public class PromptImageView extends AppCompatImageView implements IPrompt {
    private static final String TAG = "PromptImageView";
    private int mMode;

    private String mTextString;
    private int mTextColor;
    private int mTextSize;
    private int mPadding;
    private int mPromptBackgroundColor;
    private int mPosition;
    private int mRadius;

    private float mWidthPaddingScale;
    private float mHeightPaddingScale;

    private Paint mBackgroundPaint;
    private Paint mTextPaint;

    private Rect mTextRect;
    /**
     * 仅仅用来记录 不用来操作的
     */
    private RectF mRecordRectF;
    private RectF mUsedRectF;


    public PromptImageView(Context context) {
        this(context, null);
    }

    public PromptImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PromptImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PromptImageView);
        mMode = a.getInt(R.styleable.PromptImageView_promptMode, MODE_GRAPH);
        mTextString = a.getString(R.styleable.PromptImageView_text);
        mTextColor = a.getColor(R.styleable.PromptImageView_textColor, Color.WHITE);
        mTextSize = a.getDimensionPixelSize(R.styleable.PromptImageView_textSize, getDefaultTextSize(context));
        mPadding = a.getDimensionPixelSize(R.styleable.PromptImageView_padding, getDefaultPadding(context));
        mRadius = a.getDimensionPixelSize(R.styleable.PromptImageView_radius, getDefaultRadius(context));
        mPosition = a.getInt(R.styleable.PromptImageView_position, Position.LEFT);
        mPromptBackgroundColor = a.getColor(R.styleable.PromptImageView_promptBackground, Color.RED);
        mWidthPaddingScale = a.getFloat(R.styleable.PromptImageView_widthPaddingScale, DEFAULT_PADDING_SCALE);
        mHeightPaddingScale = a.getFloat(R.styleable.PromptImageView_heightPaddingScale, DEFAULT_PADDING_SCALE);
        a.recycle();

        init();
    }

    private void init() {
        // 取值范围为0 -1
        mWidthPaddingScale = Math.min(1.0f, Math.max(0, mWidthPaddingScale));
        mHeightPaddingScale = Math.min(1.0f, Math.max(0, mHeightPaddingScale));

        initPaint();
        mTextRect = new Rect();
        mRecordRectF = new RectF();
        mUsedRectF = new RectF();

        commit();
    }


    private void initPaint() {
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);


        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setDither(true);
        mBackgroundPaint.setColor(mPromptBackgroundColor);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "onMeasure: ");
        if (mMode == MODE_NONE) {
            return;
        }
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        switch (mPosition) {
            case Position.LEFT:
                mUsedRectF.set(mRecordRectF);
                mUsedRectF.offset(width * mWidthPaddingScale, height * mHeightPaddingScale);
                break;
            case Position.RIGHT:
                mUsedRectF.set(mRecordRectF);
                mUsedRectF.offset(width * (1 - mWidthPaddingScale) - mRecordRectF.width(), height * mHeightPaddingScale);
                break;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mMode == MODE_NONE) {
            return;
        }
        canvas.drawRoundRect(mUsedRectF, 999, 999, mBackgroundPaint);
        if (mMode == MODE_TEXT && !TextUtils.isEmpty(mTextString)) {
            Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
            float baseline = mUsedRectF.top + (mUsedRectF.bottom - mUsedRectF.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(mTextString, mUsedRectF.centerX(), baseline, mTextPaint);
        }
    }


    /**
     * 设置当前模式
     */
    @Override
    public PromptImageView setMode(@mode int mode) {
        mMode = mode;
        return this;
    }

    @Override
    public PromptImageView setPromptText(String text) {
        try {
            int number = Integer.valueOf(text);
            setPromptText(number);
        } catch (NumberFormatException e) {
            mTextString = text;
        }
        return this;
    }

    @Override
    public PromptImageView setPromptText(int text) {
        if (text > 99) {
            mTextString = "99+";
        } else {
            mTextString = String.valueOf(text);
        }
        return this;
    }

    @Override
    public PromptImageView setPromptTextColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTextColor = getResources().getColor(color, getContext().getTheme());
        } else {
            mTextColor = getResources().getColor(color);
        }
        mTextPaint.setColor(mTextColor);
        return this;
    }

    @Override
    public PromptImageView setPromptTextSize(@DimenRes int size) {
        mTextSize = getResources().getDimensionPixelSize(size);
        mTextPaint.setColor(mTextSize);
        return this;
    }

    @Override
    public PromptImageView setPromptBackgroundColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPromptBackgroundColor = getResources().getColor(color, getContext().getTheme());
        } else {
            mPromptBackgroundColor = getResources().getColor(color);
        }
        mBackgroundPaint.setColor(mPromptBackgroundColor);
        return this;
    }

    @Override
    public PromptImageView setPromptRadius(@DimenRes int radius) {
        mRadius = getResources().getDimensionPixelSize(radius);
        return this;
    }

    @Override
    public PromptImageView setPromptPadding(@DimenRes int padding) {
        mPadding = getResources().getDimensionPixelSize(padding);
        return this;
    }

    @Override
    public PromptImageView setPromptPosition(@position int position) {
        mPosition = position;
        return this;
    }

    @Override
    public PromptImageView setPromptWidthPaddingScale(@FloatRange(from = 0.0f, to = 1.0f) float scale) {
        mWidthPaddingScale = scale;
        return this;
    }

    @Override
    public PromptImageView setPromptHeightPaddingScale(@FloatRange(from = 0.0f, to = 1.0f) float scale) {
        mHeightPaddingScale = scale;
        return this;
    }

    /**
     * 更新信息
     */
    @Override
    public PromptImageView commit() {
        return commit(false);
    }

    /**
     * 使生效
     *
     * @param init 是否是初始化
     */
    protected PromptImageView commit(boolean init) {
        switch (mMode) {
            case MODE_TEXT:
                if (TextUtils.isEmpty(mTextString)) {
                    mTextRect.set(0, 0, mRadius, mRadius);
                    mRecordRectF.set(mTextRect);
                } else {
                    mTextPaint.getTextBounds(mTextString, 0, mTextString.length(), mTextRect);
                    // 保证至少是圆形
                    if (mTextRect.width() < mTextRect.height()) {
                        mTextRect.right = mTextRect.left + mTextRect.height() + 1;
                    }
                    mRecordRectF.set(mTextRect);
                    mRecordRectF.set(mRecordRectF.left - mPadding, mRecordRectF.top - mPadding, mRecordRectF.right + mPadding, mRecordRectF.bottom + mPadding);
                    mRecordRectF.offset(mPadding, mTextRect.height() + mPadding);
                }
                break;
            case MODE_GRAPH:
                mRecordRectF.set(0, 0, mRadius, mRadius);
                break;
        }

        if (!init) {
            postInvalidate();
        }
        return this;
    }

    /**
     * 获取默认标题字体大小
     */
    protected int getDefaultTextSize(Context context) {
        return context.getResources().getDimensionPixelOffset(R.dimen.K_title);
    }

    /**
     * 获取默认标题字体大小
     */
    protected int getDefaultPadding(Context context) {
        return context.getResources().getDimensionPixelOffset(R.dimen.dp_5);
    }

    /**
     * 获取默认标题字体大小
     */
    protected int getDefaultRadius(Context context) {
        return context.getResources().getDimensionPixelOffset(R.dimen.dp_5);
    }
}
