/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
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

package com.ishow.common.widget.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.FloatRange;
import androidx.appcompat.widget.AppCompatTextView;
import com.ishow.common.R;
import com.ishow.common.widget.prompt.IPrompt;

/**
 * 角标提示的TextView
 */
public class PromptTextView extends AppCompatTextView implements IPrompt {
    private int mMode;

    private String mPromptTextString;
    private int mPromptTextColor;
    private int mPromptTextSize;
    private int mPromptPadding;
    private int mPromptBackgroundColor;
    private int mPromptPosition;
    private int mPromptRadius;

    private float mWidthPaddingScale;
    private float mHeightPaddingScale;

    private Paint mPromptTextPaint;
    private Paint mPromptBackgroundPaint;

    private Rect mPromptTextRect;
    /**
     * 仅仅用来记录 不用来操作的
     */
    private RectF mPromptRecordRectF;
    private RectF mPromptUsedRectF;

    public PromptTextView(Context context) {
        this(context, null);
    }

    public PromptTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PromptTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PromptTextView);
        mMode = a.getInt(R.styleable.PromptTextView_promptMode, PromptMode.GRAPH);
        mPromptTextString = a.getString(R.styleable.PromptTextView_promptText);
        mPromptTextColor = a.getColor(R.styleable.PromptTextView_promptTextColor, Color.WHITE);
        mPromptTextSize = a.getDimensionPixelSize(R.styleable.PromptTextView_promptTextSize, getDefaultTextSize(context));
        mPromptPadding = a.getDimensionPixelSize(R.styleable.PromptTextView_promptPadding, getDefaultPadding(context));
        mPromptRadius = a.getDimensionPixelSize(R.styleable.PromptTextView_promptRadius, getDefaultRadius(context));
        mPromptPosition = a.getInt(R.styleable.PromptTextView_promptPosition, PromptPosition.LEFT);
        mPromptBackgroundColor = a.getColor(R.styleable.PromptTextView_promptBackground, Color.RED);
        mWidthPaddingScale = a.getFloat(R.styleable.PromptTextView_widthPaddingScale, DEFAULT_PADDING_SCALE);
        mHeightPaddingScale = a.getFloat(R.styleable.PromptTextView_heightPaddingScale, DEFAULT_PADDING_SCALE);
        a.recycle();

        init();
    }

    private void init() {
        // 取值范围为0 -1
        mWidthPaddingScale = Math.min(1.0f, Math.max(0, mWidthPaddingScale));
        mHeightPaddingScale = Math.min(1.0f, Math.max(0, mHeightPaddingScale));

        initPaint();
        mPromptTextRect = new Rect();
        mPromptRecordRectF = new RectF();
        mPromptUsedRectF = new RectF();

        commit();
    }


    private void initPaint() {
        mPromptTextPaint = new TextPaint();
        mPromptTextPaint.setAntiAlias(true);
        mPromptTextPaint.setDither(true);
        mPromptTextPaint.setTextSize(mPromptTextSize);
        mPromptTextPaint.setColor(mPromptTextColor);


        mPromptBackgroundPaint = new Paint();
        mPromptBackgroundPaint.setAntiAlias(true);
        mPromptBackgroundPaint.setDither(true);
        mPromptBackgroundPaint.setColor(mPromptBackgroundColor);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mMode == PromptMode.NONE) {
            return;
        }

        if (mMode == PromptMode.TEXT && TextUtils.isEmpty(mPromptTextString)) {
            return;
        }

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        switch (mPromptPosition) {
            case PromptPosition.LEFT:
                mPromptUsedRectF.set(mPromptRecordRectF);
                mPromptUsedRectF.offset(width * mWidthPaddingScale, height * mHeightPaddingScale);
                break;
            case PromptPosition.RIGHT:
                mPromptUsedRectF.set(mPromptRecordRectF);
                mPromptUsedRectF.offset(width * (1 - mWidthPaddingScale) - mPromptRecordRectF.width(), height * mHeightPaddingScale);
                break;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mMode == PromptMode.NONE) {
            return;
        }

        if (mMode == PromptMode.TEXT && TextUtils.isEmpty(mPromptTextString)) {
            return;
        }
        canvas.drawRoundRect(mPromptUsedRectF, 999, 999, mPromptBackgroundPaint);
        if (mMode == PromptMode.TEXT && !TextUtils.isEmpty(mPromptTextString)) {
            Paint.FontMetricsInt fontMetrics = mPromptTextPaint.getFontMetricsInt();
            float baseline = mPromptUsedRectF.top + (mPromptUsedRectF.bottom - mPromptUsedRectF.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            mPromptTextPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(mPromptTextString, mPromptUsedRectF.centerX(), baseline, mPromptTextPaint);
        }
    }


    /**
     * 设置当前模式
     */
    @Override
    public PromptTextView setPromptMode(@PromptMode int mode) {
        mMode = mode;
        return this;
    }

    @Override
    public PromptTextView setPromptText(String text) {
        try {
            int number = Integer.valueOf(text);
            setPromptText(number);
        } catch (NumberFormatException e) {
            mPromptTextString = text;
        }
        return this;
    }

    @Override
    public PromptTextView setPromptText(int text) {
        if (text > 99) {
            mPromptTextString = "99+";
        } else {
            mPromptTextString = String.valueOf(text);
        }
        return this;
    }

    @Override
    public PromptTextView setPromptTextColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPromptTextColor = getResources().getColor(color, getContext().getTheme());
        } else {
            mPromptTextColor = getResources().getColor(color);
        }
        mPromptTextPaint.setColor(mPromptTextColor);
        return this;
    }

    @Override
    public PromptTextView setPromptTextSize(@DimenRes int size) {
        mPromptTextSize = getResources().getDimensionPixelSize(size);
        mPromptTextPaint.setTextSize(mPromptTextSize);
        return this;
    }

    @Override
    public PromptTextView setPromptBackgroundColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPromptBackgroundColor = getResources().getColor(color, getContext().getTheme());
        } else {
            mPromptBackgroundColor = getResources().getColor(color);
        }
        mPromptBackgroundPaint.setColor(mPromptBackgroundColor);
        return this;
    }

    @Override
    public PromptTextView setPromptRadius(@DimenRes int radius) {
        mPromptRadius = getResources().getDimensionPixelSize(radius);
        return this;
    }

    @Override
    public PromptTextView setPromptPadding(@DimenRes int padding) {
        mPromptPadding = getResources().getDimensionPixelSize(padding);
        return this;
    }

    @Override
    public PromptTextView setPromptPosition(@PromptPosition int position) {
        mPromptPosition = position;
        return this;
    }

    @Override
    public PromptTextView setPromptWidthPaddingScale(@FloatRange(from = 0.0f, to = 1.0f) float scale) {
        mWidthPaddingScale = scale;
        return this;
    }

    @Override
    public PromptTextView setPromptHeightPaddingScale(@FloatRange(from = 0.0f, to = 1.0f) float scale) {
        mHeightPaddingScale = scale;
        return this;
    }

    /**
     * 更新信息
     */
    @Override
    public PromptTextView commit() {
        return commit(false);
    }

    /**
     * 使生效
     *
     * @param init 是否是初始化
     */
    protected PromptTextView commit(boolean init) {
        switch (mMode) {
            case PromptMode.TEXT:
                if (TextUtils.isEmpty(mPromptTextString)) {
                    mPromptTextRect.set(0, 0, mPromptRadius, mPromptRadius);
                    mPromptRecordRectF.set(mPromptTextRect);
                } else {
                    mPromptTextPaint.getTextBounds(mPromptTextString, 0, mPromptTextString.length(), mPromptTextRect);
                    // 保证至少是圆形 视觉上感觉不太圆 所以+
                    if (mPromptTextRect.width() < mPromptTextRect.height()) {
                        mPromptTextRect.right = mPromptTextRect.left + mPromptTextRect.height() + 1;
                    }
                    mPromptRecordRectF.set(mPromptTextRect);
                    mPromptRecordRectF.set(mPromptRecordRectF.left - mPromptPadding, mPromptRecordRectF.top - mPromptPadding, mPromptRecordRectF.right + mPromptPadding, mPromptRecordRectF.bottom + mPromptPadding);
                    mPromptRecordRectF.offset(mPromptPadding, mPromptTextRect.height() + mPromptPadding);
                }
                break;
            case PromptMode.GRAPH:
                mPromptRecordRectF.set(0, 0, mPromptRadius, mPromptRadius);
                break;
        }

        if (!init) {
            requestLayout();
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
