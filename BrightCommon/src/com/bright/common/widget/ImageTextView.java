/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
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

package com.bright.common.widget;

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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.StringRes;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.bright.common.R;
import com.bright.common.constant.DefaultColors;
import com.bright.common.constant.Position;
import com.bright.common.utils.image.ImageUtils;


public class ImageTextView extends View {
    private static final String TAG = "ImageTextView";

    /**
     * 默认 文本和图片之间的间距
     */
    private static final int DEFAULT_PADDING = 8;
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

        a.recycle();

        if (mTextStateColor == null) {
            mTextColor = DefaultColors.TEXT;
        } else {
            mTextColor = mTextStateColor.getDefaultColor();
        }

        if (mIconDrawable == null) {
            throw new IllegalStateException(" need a image !");
        }

        mIconDrawable.setCallback(this);
        mIconBitmap = ImageUtils.drawableToBitmap(mIconDrawable);
        init();
    }

    private void init() {
        initPaint();
        computeDesireWidth();
    }

    private void initPaint() {
        mTextPaint = new TextPaint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextDesireWidth = (int) Layout.getDesiredWidth(mText, mTextPaint);
    }

    /**
     * 更新画笔
     */
    private void updateTextPaint() {
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextDesireWidth = (int) Layout.getDesiredWidth(mText, mTextPaint);
    }

    /**
     * 点击状态有变化就会改变
     */
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Log.i(TAG, "drawableStateChanged: ");
        if (mIconDrawable instanceof StateListDrawable) {
            StateListDrawable drawable = (StateListDrawable) mIconDrawable;
            if (mIconDrawable.isStateful()) {
                int[] states = getDrawableState();
                drawable.setState(states);
                mIconBitmap = ImageUtils.drawableToBitmap(drawable);
                if (mTextStateColor != null) {
                    mTextColor = mTextStateColor.getColorForState(states, mTextColor);
                }
                invalidate();
            }

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

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
                if (mDesireWidth <= maxSize) {
                    mLayout = new StaticLayout(mText, mTextPaint, mTextDesireWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
                } else {
                    mLayout = new StaticLayout(mText, mTextPaint, maxSize, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
                }
                break;

            case Position.LEFT:
            case Position.RIGHT:
                maxSize = size - mIconBitmap.getWidth() - 2 * DEFAULT_PADDING - mPadding;
                if (mDesireWidth <= maxSize) {
                    mLayout = new StaticLayout(mText, mTextPaint, mTextDesireWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
                } else {
                    mLayout = new StaticLayout(mText, mTextPaint, maxSize, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
                }
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
            Log.i(TAG, "to do nothing  size is 0");
            return;
        }
        // Clear canvas
        canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
        // draw icon
        drawIcon(width, height);
        canvas.drawBitmap(mShowBitmap, 0, 0, null);
        // draw text
        drawText(canvas);
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

    private void computeDesireWidth() {

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
        String _text = text.toString();
        if (TextUtils.equals(_text, mText)) {
            return;
        }

        mText = _text;
        updateTextPaint();
        requestLayout();
    }

    public CharSequence getText() {
        return mText;
    }


    public float getTextSize() {
        return mTextSize;
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
        mTextSize = size;
        postInvalidate();
    }

    /**
     * Located in a position to set the picture text
     *
     * @param position can be set TOP BOTTOM LEFT RIGHT
     */
    public void setPosition(int position) {
        mPosition = position;
        requestLayout();
        postInvalidate();
    }

    /**
     * Sets the text color for all the states (normal, selected,
     * focused) to be this color.
     *
     * @attr ref com.haiyang.R.styleable#ImageTextButton_textColor
     * @see #setTextColor(ColorStateList)
     * @see #getTextColors()
     */
    public void setTextColor(ColorStateList color) {
        mTextStateColor = color;
        postInvalidate();
    }

    /**
     * Sets the text color.
     *
     * @see #setTextColor(int)
     * @see #getTextColors()
     */
    public void setTextColor(int color) {
        mTextStateColor = ColorStateList.valueOf(color);
        postInvalidate();
    }

    /**
     * Gets the text colors for the different states (normal, selected, focused) of the ImageTextButton.
     * <p/>
     * if is null , just it default
     *
     * @see #setTextColor(ColorStateList)
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
     * {@link #setIcon(Bitmap)} and
     * {@link BitmapFactory} instead.</p>
     *
     * @param resId the resource identifier of the drawable
     */
    public void setIcon(int resId) {
        mIconBitmap = BitmapFactory.decodeResource(getContext().getResources(), resId);
        postInvalidate();
    }

    /**
     * Sets a drawable as the icon of this ImageTextButton.
     *
     * @param drawable The drawable to set
     */
    public void setIcon(Drawable drawable) {
        mIconBitmap = ((BitmapDrawable) drawable).getBitmap();
        postInvalidate();
    }

    /**
     * Return the view's drawable, or null if no drawable has been assigned.
     */
    public Bitmap getIcon() {
        return mIconBitmap;
    }

    /**
     * Sets a Bitmap as the icon of this ImageTextButton.
     *
     * @param bitmap The bitmap to set
     */
    public void setIcon(Bitmap bitmap) {
        mIconBitmap = bitmap;
        postInvalidate();
    }


    private int getDefaultTextSize() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.H_title);
    }
}
