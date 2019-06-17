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

package com.ishow.common.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import androidx.annotation.*;
import androidx.core.graphics.drawable.DrawableCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.ishow.common.R;
import com.ishow.common.widget.prompt.IPrompt;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class ImageTextView extends View implements IPrompt {
    private static final String TAG = "ImageTextView";

    /**
     * 默认 文本和图片之间的间距
     */
    private static final int DEFAULT_PADDING = 3;
    /**
     * 图片位置
     */
    private int mImageOrientation;
    /**
     * 文本和图片之间的间距
     */
    private int mPadding;

    /**
     * 覆盖的颜色
     */
    private ColorStateList mTintColor;

    /**
     * 字体颜色
     */
    private int mTextColor;
    /**
     * 字体大小
     */
    private float mTextSize;

    private int mDesireWidth;
    private int mTextDesireWidth;

    // 画字体的时候需要的平移的距离
    private int mTranslationX;
    private int mTranslationY;
    /**
     * 字体颜色
     */
    private ColorStateList mTextStateColor;

    /**
     * 图标的Drawable
     */
    private Drawable mImageDrawable;
    private int mImageWidth;
    private int mImageHeight;

    /**
     * 画图标的区域
     */
    private Rect mIconRect = new Rect();

    /**
     * 文本内容
     */
    private String mText;

    /**
     * 文本的画笔
     */
    private TextPaint mTextPaint;

    private Layout mLayout;


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

    // 定义Ann
    @IntDef({Orientation.LEFT, Orientation.TOP, Orientation.RIGHT, Orientation.BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
        /**
         * Pictures in the text to the left
         */
        int LEFT = 0;
        /**
         * Pictures in the text to the right
         */
        int RIGHT = 4;
        /**
         * Pictures in the text to the top
         */
        int TOP = 8;
        /**
         * Pictures in the text to the bottom
         */
        int BOTTOM = 16;
    }

    /**
     * 关于Prompt 要用commit 来生效
     */
    public ImageTextView(Context context) {
        super(context);
        mTextColor = context.getResources().getColor(R.color.text_grey_normal);
        init();
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageTextView);
        mImageDrawable = a.getDrawable(R.styleable.ImageTextView_image);
        mImageWidth = a.getDimensionPixelSize(R.styleable.ImageTextView_imageWidth, 0);
        mImageHeight = a.getDimensionPixelSize(R.styleable.ImageTextView_imageHeight, 0);

        mText = a.getString(R.styleable.ImageTextView_text);
        mTextStateColor = a.getColorStateList(R.styleable.ImageTextView_textColor);
        mTextSize = a.getDimensionPixelSize(R.styleable.ImageTextView_textSize, getDefaultTextSize());

        mTintColor = a.getColorStateList(R.styleable.ImageTextView_tint);

        mImageOrientation = a.getInt(R.styleable.ImageTextView_position, Orientation.TOP);
        mPadding = a.getDimensionPixelSize(R.styleable.ImageTextView_padding, DEFAULT_PADDING);

        mMode = a.getInt(R.styleable.ImageTextView_promptMode, PromptMode.NONE);
        mPromptTextString = a.getString(R.styleable.ImageTextView_promptText);
        mPromptTextColor = a.getColor(R.styleable.ImageTextView_promptTextColor, Color.WHITE);
        mPromptTextSize = a.getDimensionPixelSize(R.styleable.ImageTextView_promptTextSize, getDefaultPromptTextSize(context));
        mPromptPadding = a.getDimensionPixelSize(R.styleable.ImageTextView_promptPadding, getDefaultPromptPadding(context));
        mPromptRadius = a.getDimensionPixelSize(R.styleable.ImageTextView_promptRadius, getDefaultPromptRadius(context));
        mPromptPosition = a.getInt(R.styleable.ImageTextView_promptPosition, PromptPosition.LEFT);
        mPromptBackgroundColor = a.getColor(R.styleable.ImageTextView_promptBackground, Color.RED);
        mWidthPaddingScale = a.getFloat(R.styleable.ImageTextView_widthPaddingScale, DEFAULT_PADDING_SCALE);
        mHeightPaddingScale = a.getFloat(R.styleable.ImageTextView_heightPaddingScale, DEFAULT_PADDING_SCALE);

        a.recycle();

        if (mTextStateColor == null) {
            mTextColor = context.getResources().getColor(R.color.text_grey_normal);
        } else {
            mTextColor = mTextStateColor.getDefaultColor();
        }

        if (mImageDrawable == null) {
            throw new IllegalStateException(" need a image !");
        }

        init();
    }

    private void init() {
        if (mTintColor != null) {
            mImageDrawable = DrawableCompat.wrap(mImageDrawable);
            DrawableCompat.setTintList(mImageDrawable, mTintColor);
        }

        // 取值范围为0 -1
        mWidthPaddingScale = Math.min(1.0f, Math.max(0, mWidthPaddingScale));
        mHeightPaddingScale = Math.min(1.0f, Math.max(0, mHeightPaddingScale));

        initPaint();
        mPromptTextRect = new Rect();
        mPromptRecordRectF = new RectF();
        mPromptUsedRectF = new RectF();
        commit();

        computeDesireWidth();
    }

    private void initPaint() {
        mTextPaint = new TextPaint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);

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

    /**
     * 点击状态有变化就会改变
     */
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        boolean needInvalidate = false;
        if (mImageDrawable instanceof StateListDrawable) {
            StateListDrawable drawable = (StateListDrawable) mImageDrawable;
            if (mImageDrawable.isStateful()) {
                int[] states = getDrawableState();
                drawable.setState(states);
                needInvalidate = true;
            }
        }

        if (mTextStateColor != null && mTextStateColor.isStateful()) {
            mTextColor = mTextStateColor.getColorForState(getDrawableState(), mTextColor);
            mTextPaint.setColor(mTextColor);
            needInvalidate = true;
        }

        if (needInvalidate) invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        measurePrompt(width, height);
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int widthMeasureSpec) {
        final int mode = MeasureSpec.getMode(widthMeasureSpec);
        final int size = MeasureSpec.getSize(widthMeasureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                return measureWidthUnspecified();
            case MeasureSpec.EXACTLY:
                return measureWidthByExactly(size);
            case MeasureSpec.AT_MOST:
                return measureWidthByAtmost(size);
        }

        return size;
    }

    private int measureWidthUnspecified() {
        int width = 0;
        mLayout = new StaticLayout(mText, mTextPaint, mTextDesireWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
        switch (mImageOrientation) {
            case Orientation.TOP:
            case Orientation.BOTTOM:
                width = DEFAULT_PADDING + Math.max(mLayout.getWidth(), getImageWidth()) + DEFAULT_PADDING;
                break;

            case Orientation.LEFT:
            case Orientation.RIGHT:
                width = DEFAULT_PADDING + mLayout.getWidth() + mPadding + getImageWidth() + DEFAULT_PADDING;
                break;
        }
        return width;
    }


    private int measureWidthByExactly(int size) {
        int maxSize;
        switch (mImageOrientation) {
            case Orientation.TOP:
            case Orientation.BOTTOM:
                maxSize = size - 2 * DEFAULT_PADDING;
                mLayout = new StaticLayout(mText, mTextPaint, maxSize, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
                break;

            case Orientation.LEFT:
            case Orientation.RIGHT:
                mLayout = new StaticLayout(mText, mTextPaint, mTextDesireWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
                break;
        }
        return size;
    }

    private int measureWidthByAtmost(int size) {
        int width = size;
        int maxSize;
        switch (mImageOrientation) {
            case Orientation.TOP:
            case Orientation.BOTTOM:
                if (mDesireWidth < size) {
                    mLayout = new StaticLayout(mText, mTextPaint, mTextDesireWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
                    width = DEFAULT_PADDING + Math.max(mLayout.getWidth(), getImageWidth()) + DEFAULT_PADDING;
                } else {
                    maxSize = size - 2 * DEFAULT_PADDING;
                    mLayout = new StaticLayout(mText, mTextPaint, maxSize, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
                    width = size;
                }
                break;

            case Orientation.LEFT:
            case Orientation.RIGHT:
                if (mDesireWidth < size) {
                    mLayout = new StaticLayout(mText, mTextPaint, mTextDesireWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
                    width = DEFAULT_PADDING + mLayout.getWidth() + mPadding + getImageWidth() + DEFAULT_PADDING;
                } else {
                    maxSize = size - getImageWidth() - 2 * DEFAULT_PADDING - mPadding;
                    mLayout = new StaticLayout(mText, mTextPaint, maxSize, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
                    width = size;
                }
                break;
        }
        return width;
    }


    private int measureHeight(int heightMeasureSpec) {
        final int mode = MeasureSpec.getMode(heightMeasureSpec);
        final int size = MeasureSpec.getSize(heightMeasureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                return measureHeightUnspecified();
            case MeasureSpec.EXACTLY:
                return size;
            case MeasureSpec.AT_MOST:
                return measureHeightByAtmost(size);
        }

        return size;
    }

    private int measureHeightUnspecified() {
        int height = 0;
        switch (mImageOrientation) {
            case Orientation.TOP:
            case Orientation.BOTTOM:
                height = DEFAULT_PADDING + mLayout.getHeight() + mPadding + getImageHeight() + DEFAULT_PADDING;
                break;

            case Orientation.LEFT:
            case Orientation.RIGHT:
                height = DEFAULT_PADDING + Math.max(mLayout.getHeight(), getImageHeight()) + DEFAULT_PADDING;
                break;
        }
        return height;
    }

    private int measureHeightByAtmost(int size) {
        int desireHeight = size;
        switch (mImageOrientation) {
            case Orientation.TOP:
            case Orientation.BOTTOM:
                desireHeight = DEFAULT_PADDING + mLayout.getHeight() + mPadding + getImageHeight() + DEFAULT_PADDING;
                break;

            case Orientation.LEFT:
            case Orientation.RIGHT:
                desireHeight = DEFAULT_PADDING + Math.max(mLayout.getHeight(), getImageHeight()) + DEFAULT_PADDING;
                break;
        }
        return Math.min(size, desireHeight);
    }


    private void measurePrompt(int width, int height) {
        if (mMode == PromptMode.NONE) {
            return;
        }

        if (mMode == PromptMode.TEXT && TextUtils.isEmpty(mPromptTextString)) {
            return;
        }


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
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();

        final int iconWidth = getImageWidth();
        final int iconHeight = getImageHeight();

        final int textWidth = mLayout.getWidth();
        final int textHeight = mLayout.getHeight();

        switch (mImageOrientation) {
            case Orientation.TOP:
                // Calculate icon Rect
                mIconRect.left = (width - iconWidth) / 2;
                mIconRect.right = mIconRect.left + iconWidth;
                mIconRect.top = (height - iconHeight - textHeight - mPadding) / 2;
                mIconRect.bottom = mIconRect.top + iconHeight;

                // Calculate text Rect
                mTranslationX = (width - textWidth) / 2;
                mTranslationY = mIconRect.bottom + mPadding;
                break;
            case Orientation.BOTTOM:
                // Calculate text Rect
                mTranslationX = (width - textWidth) / 2;
                mTranslationY = (height - iconHeight - textHeight - mPadding) / 2;

                // Calculate icon Rect
                mIconRect.left = (width - iconWidth) / 2;
                mIconRect.right = mIconRect.left + iconWidth;
                mIconRect.top = mTranslationY + textHeight + mPadding;
                mIconRect.bottom = mIconRect.top + iconHeight;
                break;
            case Orientation.LEFT:
                // Calculate icon Rect
                mIconRect.left = (width - iconWidth - textWidth - mPadding) / 2;
                mIconRect.right = mIconRect.left + iconWidth;
                mIconRect.top = (height - iconHeight) / 2;
                mIconRect.bottom = mIconRect.top + iconHeight;

                // Calculate text Rect
                mTranslationX = mIconRect.right + mPadding;
                mTranslationY = (height - textHeight) / 2;
                break;
            case Orientation.RIGHT:
                // Calculate text Rect
                mTranslationX = (width - iconWidth - textWidth - mPadding) / 2;
                mTranslationY = (height - textHeight) / 2;

                // Calculate icon Rect
                mIconRect.left = mTranslationX + textWidth + mPadding;
                mIconRect.right = mIconRect.left + iconWidth;
                mIconRect.top = (height - iconHeight) / 2;
                mIconRect.bottom = mIconRect.top + iconHeight;
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (width <= 0 || height <= 0) {
            return;
        }

        // draw text
        drawIcon(canvas);
        // draw text
        drawText(canvas);
        // drawPrompt
        drawPrompt(canvas);
    }


    private void drawIcon(Canvas canvas) {
        Drawable drawable = mImageDrawable;
        drawable.setBounds(mIconRect);
        drawable.draw(canvas);
    }

    private void drawText(Canvas canvas) {
        if (TextUtils.isEmpty(mText)) {
            Log.i(TAG, "drawText: mText is empty");
            return;
        }
        canvas.save();
        canvas.translate(mTranslationX, mTranslationY);
        mLayout.draw(canvas);
        canvas.restore();
    }

    private void drawPrompt(Canvas canvas) {
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

    private void computeDesireWidth() {
        mTextDesireWidth = (int) Layout.getDesiredWidth(mText, mTextPaint);

        final int iconWidth = getImageWidth();

        switch (mImageOrientation) {
            case Orientation.LEFT:
            case Orientation.RIGHT:
                mDesireWidth = mTextDesireWidth + iconWidth + mPadding + DEFAULT_PADDING * 2;
            case Orientation.TOP:
            case Orientation.BOTTOM:
                mDesireWidth = Math.max(mTextDesireWidth, iconWidth) + DEFAULT_PADDING * 2;
                break;
        }


        int minWidth = getMinimumWidth();
        setMinimumWidth(Math.max(minWidth, mDesireWidth));

    }

    /**
     * Sets the ImageTextButton to display the Text show .
     */
    @SuppressWarnings("unused")
    public void setTint(@ColorInt int color) {
        mTintColor = ColorStateList.valueOf(color);
        postInvalidate();
    }

    /**
     * Sets the ImageTextButton to display the Text show .
     */
    @SuppressWarnings("unused")
    public void setTint(@NonNull ColorStateList color) {
        mTintColor = color;
        postInvalidate();
    }


    /**
     * Sets the ImageTextButton to display the Text show .
     */
    public void setText(@StringRes int resid) {
        String text = getContext().getString(resid);
        setText(text);
    }

    /**
     * Sets the ImageTextButton to display the Text show .
     */
    public void setText(CharSequence text) {
        if (TextUtils.equals(text, mText)) {
            return;
        }
        mText = text.toString();
        computeDesireWidth();
        requestLayout();
        postInvalidate();
    }

    public CharSequence getText() {
        return mText;
    }

    /**
     * Set the default text size to a given unit and value.  See {@link
     * TypedValue} for the possible dimension units.
     *
     * @param size The desired size in the given units.
     */
    public void setTextSize(float size) {
        if (size < 0) {
            throw new IllegalStateException("textSize need larger than 0");
        }
        mTextPaint.setTextSize(mTextSize);
        computeDesireWidth();
        postInvalidate();
    }

    public float getTextSize() {
        return mTextSize;
    }

    /**
     * Located in a imageOrientation to set the picture text
     *
     * @param imageOrientation can be set TOP BOTTOM LEFT RIGHT
     */
    @SuppressWarnings("unused")
    public void setImageOrientation(@Orientation int imageOrientation) {
        mImageOrientation = imageOrientation;
        computeDesireWidth();
        requestLayout();
        postInvalidate();
    }


    /**
     * Sets the text color.
     *
     * @see #setTextColor(int)
     * @see #getTextColors()
     */
    public void setTextColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTextStateColor = getContext().getResources().getColorStateList(color, getContext().getTheme());
        } else {
            mTextStateColor = getContext().getResources().getColorStateList(color);
        }

        mTextColor = mTextStateColor.getDefaultColor();
        mTextPaint.setColor(mTextColor);
        postInvalidate();
    }

    /**
     * Gets the text colors for the different states (setNormalStatus, selected, focused) of the ImageTextButton.
     * <p/>
     * if is null , just it default
     *
     * @see #setTextColor(int)
     */
    @SuppressWarnings("unused")
    public ColorStateList getTextColors() {
        return mTextStateColor;
    }

    /**
     * Sets a drawable as the icon of this ImageTextButton.
     * <p/>
     * <p class="note">This does Bitmap reading and decoding on the UI
     * thread, which can cause a latency hiccup.  If that's a concern,
     * consider using {@link (Drawable)} or
     * {@link BitmapFactory} instead.</p>
     *
     * @param resId the resource identifier of the drawable
     */
    public void setIcon(@DrawableRes int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mImageDrawable = getContext().getResources().getDrawable(resId, getContext().getTheme());
        } else {
            mImageDrawable = getContext().getResources().getDrawable(resId);
        }
        postInvalidate();
    }

    /**
     * 设置当前模式
     */
    @Override
    public ImageTextView setPromptMode(@PromptMode int mode) {
        mMode = mode;
        return this;
    }

    @Override
    public ImageTextView setPromptText(String text) {
        try {
            int number = Integer.valueOf(text);
            setPromptText(number);
        } catch (NumberFormatException e) {
            mPromptTextString = text;
        }
        return this;
    }

    @Override
    public ImageTextView setPromptText(int text) {
        if (text > 99) {
            mPromptTextString = "99+";
        } else {
            mPromptTextString = String.valueOf(text);
        }
        return this;
    }

    @Override
    public ImageTextView setPromptTextColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPromptTextColor = getResources().getColor(color, getContext().getTheme());
        } else {
            mPromptTextColor = getResources().getColor(color);
        }
        mPromptTextPaint.setColor(mPromptTextColor);
        return this;
    }

    @Override
    public ImageTextView setPromptTextSize(@DimenRes int size) {
        mPromptTextSize = getResources().getDimensionPixelSize(size);
        mPromptTextPaint.setTextSize(mPromptTextSize);
        return this;
    }

    @Override
    public ImageTextView setPromptBackgroundColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPromptBackgroundColor = getResources().getColor(color, getContext().getTheme());
        } else {
            mPromptBackgroundColor = getResources().getColor(color);
        }
        mPromptBackgroundPaint.setColor(mPromptBackgroundColor);
        return this;
    }

    @Override
    public ImageTextView setPromptRadius(@DimenRes int radius) {
        mPromptRadius = getResources().getDimensionPixelSize(radius);
        return this;
    }

    @Override
    public ImageTextView setPromptPadding(@DimenRes int padding) {
        mPromptPadding = getResources().getDimensionPixelSize(padding);
        return this;
    }

    @Override
    public ImageTextView setPromptPosition(@PromptMode int position) {
        mPromptPosition = position;
        return this;
    }

    @Override
    public ImageTextView setPromptWidthPaddingScale(@FloatRange(from = 0.0f, to = 1.0f) float scale) {
        mWidthPaddingScale = scale;
        return this;
    }

    @Override
    public ImageTextView setPromptHeightPaddingScale(@FloatRange(from = 0.0f, to = 1.0f) float scale) {
        mHeightPaddingScale = scale;
        return this;
    }

    /**
     * 更新信息
     */
    @Override
    public ImageTextView commit() {
        return commit(false);
    }

    /**
     * 使生效
     *
     * @param init 是否是初始化
     */
    protected ImageTextView commit(boolean init) {
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
            postInvalidate();
        }
        return this;
    }


    private int getDefaultTextSize() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.H_title);
    }

    /**
     * 获取默认标题字体大小
     */
    protected int getDefaultPromptTextSize(Context context) {
        return context.getResources().getDimensionPixelOffset(R.dimen.K_title);
    }

    /**
     * 获取默认标题字体大小
     */
    protected int getDefaultPromptPadding(Context context) {
        return context.getResources().getDimensionPixelOffset(R.dimen.dp_5);
    }

    /**
     * 获取默认标题字体大小
     */
    protected int getDefaultPromptRadius(Context context) {
        return context.getResources().getDimensionPixelOffset(R.dimen.dp_7);
    }


    private int getImageWidth() {
        if (mImageWidth > 0) {
            return mImageWidth;
        }

        return mImageDrawable == null ? 0 : mImageDrawable.getIntrinsicWidth();
    }

    private int getImageHeight(){
        if(mImageHeight >0 ){
            return mImageHeight;
        }

        return mImageDrawable == null ?0 :mImageDrawable.getIntrinsicHeight();
    }
}
