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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.ishow.common.R;
import com.ishow.common.constant.Position;
import com.ishow.common.utils.image.ImageUtils;
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
    private int mPosition;
    /**
     * 文本和图片之间的间距
     */
    private int mPadding;

    /**
     * 覆盖的颜色
     */
    private int mTintColor;
    /**
     * 覆盖颜色的透明度
     */
    private int mTintAlpha;

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
     * 原始图标的Bitmap
     */
    private Bitmap mIconBitmap;
    /**
     * 要显示原始图标的Bitmap
     */
    private Bitmap mShowBitmap;
    /**
     * 图标的Drawable
     */
    private Drawable mIconDrawable;

    /**
     * 画图标的区域
     */
    private Rect mIconRect = new Rect();
    /**
     * 是否显示提示
     */
    private boolean isShowPromt;
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
    @IntDef({Position.LEFT, Position.TOP, Position.RIGHT, Position.BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface position {
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
        mIconDrawable = a.getDrawable(R.styleable.ImageTextView_image);

        mText = a.getString(R.styleable.ImageTextView_text);
        mTextStateColor = a.getColorStateList(R.styleable.ImageTextView_textColor);
        mTextSize = a.getDimensionPixelSize(R.styleable.ImageTextView_textSize, getDefaultTextSize());

        mTintColor = a.getColor(R.styleable.ImageTextView_tint, Color.TRANSPARENT);
        mTintAlpha = a.getInt(R.styleable.ImageTextView_tintAlpha, 0);

        mPosition = a.getInt(R.styleable.ImageTextView_position, Position.TOP);
        mPadding = a.getDimensionPixelSize(R.styleable.ImageTextView_padding, DEFAULT_PADDING);

        mMode = a.getInt(R.styleable.ImageTextView_promptMode, MODE_NONE);
        mPromptTextString = a.getString(R.styleable.ImageTextView_promptText);
        mPromptTextColor = a.getColor(R.styleable.ImageTextView_promptTextColor, Color.WHITE);
        mPromptTextSize = a.getDimensionPixelSize(R.styleable.ImageTextView_promptTextSize, getDefaultPromptTextSize(context));
        mPromptPadding = a.getDimensionPixelSize(R.styleable.ImageTextView_promptPadding, getDefaultPromptPadding(context));
        mPromptRadius = a.getDimensionPixelSize(R.styleable.ImageTextView_promptRadius, getDefaultPromptRadius(context));
        mPromptPosition = a.getInt(R.styleable.ImageTextView_promptPosition, Position.LEFT);
        mPromptBackgroundColor = a.getColor(R.styleable.ImageTextView_promptBackground, Color.RED);
        mWidthPaddingScale = a.getFloat(R.styleable.ImageTextView_widthPaddingScale, DEFAULT_PADDING_SCALE);
        mHeightPaddingScale = a.getFloat(R.styleable.ImageTextView_heightPaddingScale, DEFAULT_PADDING_SCALE);

        a.recycle();

        if (mTextStateColor == null) {
            mTextColor = context.getResources().getColor(R.color.text_grey_normal);
        } else {
            mTextColor = mTextStateColor.getDefaultColor();
        }

        if (mIconDrawable == null) {
            throw new IllegalStateException(" need a image !");
        }

        mIconBitmap = ImageUtils.drawableToBitmap(mIconDrawable);
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

        if (mIconDrawable instanceof StateListDrawable) {
            StateListDrawable drawable = (StateListDrawable) mIconDrawable;
            if (mIconDrawable.isStateful()) {
                int[] states = getDrawableState();
                drawable.setState(states);
                mIconBitmap = ImageUtils.drawableToBitmap(drawable);
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
        switch (mPosition) {
            case Position.TOP:
            case Position.BOTTOM:
                width = DEFAULT_PADDING + Math.max(mLayout.getWidth(), mIconBitmap.getWidth()) + DEFAULT_PADDING;
                break;

            case Position.LEFT:
            case Position.RIGHT:
                width = DEFAULT_PADDING + mLayout.getWidth() + mPadding + mIconBitmap.getWidth() + DEFAULT_PADDING;
                break;
        }
        return width;
    }


    private int measureWidthByExactly(int size) {
        int maxSize;
        switch (mPosition) {
            case Position.TOP:
            case Position.BOTTOM:
                maxSize = size - 2 * DEFAULT_PADDING;
                mLayout = new StaticLayout(mText, mTextPaint, maxSize, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
                break;

            case Position.LEFT:
            case Position.RIGHT:
                maxSize = size - mIconBitmap.getWidth() - 2 * DEFAULT_PADDING - mPadding;
                mLayout = new StaticLayout(mText, mTextPaint, maxSize, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
                break;
        }
        return size;
    }

    private int measureWidthByAtmost(int size) {
        int width = size;
        int maxSize;
        switch (mPosition) {
            case Position.TOP:
            case Position.BOTTOM:
                if (mDesireWidth < size) {
                    mLayout = new StaticLayout(mText, mTextPaint, mTextDesireWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
                    width = DEFAULT_PADDING + Math.max(mLayout.getWidth(), mIconBitmap.getWidth()) + DEFAULT_PADDING;
                } else {
                    maxSize = size - 2 * DEFAULT_PADDING;
                    mLayout = new StaticLayout(mText, mTextPaint, maxSize, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
                    width = size;
                }
                break;

            case Position.LEFT:
            case Position.RIGHT:
                if (mDesireWidth < size) {
                    mLayout = new StaticLayout(mText, mTextPaint, mTextDesireWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
                    width = DEFAULT_PADDING + mLayout.getWidth() + mPadding + mIconBitmap.getWidth() + DEFAULT_PADDING;
                } else {
                    maxSize = size - mIconBitmap.getWidth() - 2 * DEFAULT_PADDING - mPadding;
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
        switch (mPosition) {
            case Position.TOP:
            case Position.BOTTOM:
                height = DEFAULT_PADDING + mLayout.getHeight() + mPadding + mIconBitmap.getHeight() + DEFAULT_PADDING;
                break;

            case Position.LEFT:
            case Position.RIGHT:
                height = DEFAULT_PADDING + Math.max(mLayout.getHeight(), mIconBitmap.getHeight()) + DEFAULT_PADDING;
                break;
        }
        return height;
    }

    private int measureHeightByAtmost(int size) {
        int desireHeight = size;
        switch (mPosition) {
            case Position.TOP:
            case Position.BOTTOM:
                desireHeight = DEFAULT_PADDING + mLayout.getHeight() + mPadding + mIconBitmap.getHeight() + DEFAULT_PADDING;
                break;

            case Position.LEFT:
            case Position.RIGHT:
                desireHeight = DEFAULT_PADDING + Math.max(mLayout.getHeight(), mIconBitmap.getHeight()) + DEFAULT_PADDING;
                break;
        }
        return Math.min(size, desireHeight);
    }


    private void measurePrompt(int width, int height) {
        if (mMode == MODE_NONE) {
            Log.i(TAG, "onMeasure: mode = NONE");
            return;
        }

        if (mMode == MODE_TEXT && TextUtils.isEmpty(mPromptTextString)) {
            Log.i(TAG, "onMeasure: mPromptTextString is empty");
            return;
        }


        switch (mPromptPosition) {
            case Position.LEFT:
                mPromptUsedRectF.set(mPromptRecordRectF);
                mPromptUsedRectF.offset(width * mWidthPaddingScale, height * mHeightPaddingScale);
                break;
            case Position.RIGHT:
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

        final int iconWidth = mIconBitmap.getWidth();
        final int iconHeight = mIconBitmap.getHeight();

        final int textWidth = mLayout.getWidth();
        final int textHeight = mLayout.getHeight();

        switch (mPosition) {
            case Position.TOP:
                // Calculate icon Rect
                mIconRect.left = (width - iconWidth) / 2;
                mIconRect.right = mIconRect.left + iconWidth;
                mIconRect.top = (height - iconHeight - textHeight - mPadding) / 2;
                mIconRect.bottom = mIconRect.top + iconHeight;

                // Calculate text Rect
                mTranslationX = (width - textWidth) / 2;
                mTranslationY = mIconRect.bottom + mPadding;
                break;
            case Position.BOTTOM:
                // Calculate text Rect
                mTranslationX = (width - textWidth) / 2;
                mTranslationY = (height - iconHeight - textHeight - mPadding) / 2;

                // Calculate icon Rect
                mIconRect.left = (width - iconWidth) / 2;
                mIconRect.right = mIconRect.left + iconWidth;
                mIconRect.top = mTranslationY + textHeight + mPadding;
                mIconRect.bottom = mIconRect.top + iconHeight;
                break;
            case Position.LEFT:
                // Calculate icon Rect
                mIconRect.left = (width - iconWidth - textWidth - mPadding) / 2;
                mIconRect.right = mIconRect.left + iconWidth;
                mIconRect.top = (height - iconHeight) / 2;
                mIconRect.bottom = mIconRect.top + iconHeight;

                // Calculate text Rect
                mTranslationX = mIconRect.right + mPadding;
                mTranslationY = (height - textHeight) / 2;
                break;
            case Position.RIGHT:
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
        // Clear canvas
        canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
        // draw icon
        drawIcon(width, height);
        canvas.drawBitmap(mShowBitmap, 0, 0, null);
        // draw text
        drawText(canvas);
        // drawPrompt
        drawPrompt(canvas);
    }


    private void drawIcon(int width, int height) {
        //二级缓存
        mShowBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mShowBitmap);
        Paint paint = new Paint();

        paint.setColor(mTintColor);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setAlpha(mTintAlpha);
        canvas.drawRect(mIconRect, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paint.setAlpha(255);
        canvas.drawBitmap(mIconBitmap, null, mIconRect, paint);
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
        if (mMode == MODE_NONE) {
            return;
        }

        if (mMode == MODE_TEXT && TextUtils.isEmpty(mPromptTextString)) {
            return;
        }
        canvas.drawRoundRect(mPromptUsedRectF, 999, 999, mPromptBackgroundPaint);
        if (mMode == MODE_TEXT && !TextUtils.isEmpty(mPromptTextString)) {
            Paint.FontMetricsInt fontMetrics = mPromptTextPaint.getFontMetricsInt();
            float baseline = mPromptUsedRectF.top + (mPromptUsedRectF.bottom - mPromptUsedRectF.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            mPromptTextPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(mPromptTextString, mPromptUsedRectF.centerX(), baseline, mPromptTextPaint);
        }
    }

    private void computeDesireWidth() {
        mTextDesireWidth = (int) Layout.getDesiredWidth(mText, mTextPaint);

        final int iconWidth = mIconBitmap.getWidth();

        switch (mPosition) {
            case Position.LEFT:
            case Position.RIGHT:
                mDesireWidth = mTextDesireWidth + iconWidth + mPadding + DEFAULT_PADDING * 2;
            case Position.TOP:
            case Position.BOTTOM:
                mDesireWidth = Math.max(mTextDesireWidth, iconWidth) + DEFAULT_PADDING * 2;
                break;
        }


        int minWidth = getMinimumWidth();
        setMinimumWidth(Math.max(minWidth, mDesireWidth));

    }

    /**
     * Sets the ImageTextButton to display the Text show .
     */

    public void setTint(int color) {
        mTintColor = color;
        postInvalidate();
    }

    /**
     * Sets the ImageTextButton to display the Text show .
     */
    public void setTintAlpha(int alpha) {
        mTintAlpha = alpha;
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
     * Located in a position to set the picture text
     *
     * @param position can be set TOP BOTTOM LEFT RIGHT
     */
    public void setPosition(@position int position) {
        mPosition = position;
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
     * Gets the text colors for the different states (normal, selected, focused) of the ImageTextButton.
     * <p/>
     * if is null , just it default
     *
     * @see #setTextColor(int)
     */
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
            mIconDrawable = getContext().getResources().getDrawable(resId, getContext().getTheme());
        } else {
            mIconDrawable = getContext().getResources().getDrawable(resId);
        }
        mIconBitmap = ImageUtils.drawableToBitmap(mIconDrawable);
        postInvalidate();
    }


    /**
     * Return the view's drawable, or null if no drawable has been assigned.
     */
    public Bitmap getIcon() {
        return mIconBitmap;
    }

    /**
     * 设置当前模式
     */
    @Override
    public ImageTextView setPromptMode(@mode int mode) {
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
    public ImageTextView setPromptPosition(@position int position) {
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
            case MODE_TEXT:
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
            case MODE_GRAPH:
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
}
