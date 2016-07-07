/**
 * Copyright (C) 2015  Haiyang Yu Android Source Project
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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.bright.common.R;
import com.bright.common.constant.DefaultColors;
import com.bright.common.utils.ImageUtils;
import com.bright.common.utils.UnitUtils;
import com.bright.common.utils.Utils;


public class ImageTextView extends View {
    private final String TAG = ImageTextView.class.getSimpleName();
    /**
     * Pictures in the text to the left
     */
    public static final int LEFT = 1;
    /**
     * Pictures in the text to the right
     */
    public static final int RIGHT = 2;
    /**
     * Pictures in the text to the top
     */
    public static final int TOP = 3;
    /**
     * Pictures in the text to the bottom
     */
    public static final int BOTTOM = 4;
    /**
     * 图片位置
     */
    private int mPosition;
    /**
     * 默认 文本和图片之间的间距
     */
    private static final int DEFAULT_PADDING = 10;
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
     * 最小高度
     */
    private int mMinHeight;
    /**
     * 字体颜色
     */
    private int mTextColor;
    /**
     * 字体大小
     */
    private float mTextSize;
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
     * 画字体的区域
     */
    private Rect mTextRect = new Rect();
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
     * 提示的数字
     */
    private String mPromtNumber;
    /**
     * 文本的画笔
     */
    private Paint mTextPaint;
    /**
     * 提示背景画笔
     */
    private Paint mNotiBgPaint;
    /**
     * 提示文本画笔
     */
    private Paint mNotiTextPaint;

    public ImageTextView(Context context) {
        super(context);
        mTextColor = DefaultColors.TEXT;
        init();
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageTextView);
        mIconDrawable = a.getDrawable(R.styleable.ImageTextView_image);

        mText = a.getString(R.styleable.ImageTextView_text);
        mTextStateColor = a.getColorStateList(R.styleable.ImageTextView_textColor);
        mTextSize = a.getDimensionPixelSize(R.styleable.ImageTextView_textSize, sp2px(13));

        mTintColor = a.getColor(R.styleable.ImageTextView_tint, Color.TRANSPARENT);
        mTintAlpha = a.getInt(R.styleable.ImageTextView_tintAlpha, 0);

        mPosition = a.getInt(R.styleable.ImageTextView_positon, TOP);
        mPadding = a.getDimensionPixelSize(R.styleable.ImageTextView_padding, DEFAULT_PADDING);

        mMinHeight = a.getDimensionPixelSize(R.styleable.ImageTextView_android_minHeight, 0);

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
        Log.i(TAG, "mText = " + mText);
        //初始化Text画笔
        if (! TextUtils.isEmpty(mText)) {
            mTextPaint = new Paint();
            mTextPaint.setColor(mTextColor);
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.setAntiAlias(true);
            mTextPaint.setDither(true);
            mTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
        }

        mNotiBgPaint = new Paint();
        mNotiBgPaint.setAntiAlias(true);
        mNotiBgPaint.setDither(true);
        mNotiBgPaint.setColor(DefaultColors.RED);

        mNotiTextPaint = new Paint();
        mNotiTextPaint.setAntiAlias(true);
        mNotiTextPaint.setDither(true);
        mNotiTextPaint.setColor(Color.WHITE);
        mNotiTextPaint.setTextSize(mTextSize * 0.8f); // 提示字体的大小为其他的0.8f
    }

    /**
     * 更新画笔
     */
    private void updateTextPaint() {
        if (mTextPaint == null) {
            mTextPaint = new Paint();
            mTextPaint.setAntiAlias(true);
            mTextPaint.setDither(true);
        }
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
    }

    /**
     * 点击状态有变化就会改变
     */
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Get view width and height!
        int width = widthMeasureSpec;
        int height = heightMeasureSpec;

        final int textWidth = mTextRect.width();
        final int textHeight = mTextRect.height();

        final int iconWidth = mIconBitmap.getWidth();
        final int iconHeight = mIconBitmap.getHeight();

        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        int modeW = MeasureSpec.getMode(widthMeasureSpec);

        switch (mPosition) {
            case LEFT:
            case RIGHT:
                if (modeW == MeasureSpec.AT_MOST || modeW == MeasureSpec.UNSPECIFIED) {
                    width = getPaddingLeft() + getPaddingRight() + textWidth + iconWidth + mPadding + DEFAULT_PADDING * 2;
                }
                if (modeH == MeasureSpec.AT_MOST || modeH == MeasureSpec.UNSPECIFIED) {
                    int H = Math.max(textHeight, iconHeight);
                    height = getPaddingTop() + getPaddingBottom() + H + DEFAULT_PADDING * 2;
                }
                break;
            case TOP:
            case BOTTOM:
                if (modeW == MeasureSpec.AT_MOST || modeW == MeasureSpec.UNSPECIFIED) {
                    int W = Math.max(textWidth, iconWidth);
                    width = getPaddingLeft() + getPaddingRight() + W + DEFAULT_PADDING * 2;
                }
                if (modeH == MeasureSpec.AT_MOST || modeH == MeasureSpec.UNSPECIFIED) {
                    height = getPaddingTop() + getPaddingBottom() + iconHeight + textHeight + mPadding + DEFAULT_PADDING * 2;
                }
                break;

        }
        height = Math.max(height, mMinHeight);
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();

        final int textWidth = mTextRect.width();
        final int textHeight = mTextRect.height();

        final int iconWidth = mIconBitmap.getWidth();
        final int iconHeight = mIconBitmap.getHeight();

        switch (mPosition) {
            case TOP:
                // Calculate icon Rect
                mIconRect.left = (width - iconWidth) / 2;
                mIconRect.right = mIconRect.left + iconWidth;
                mIconRect.top = (height - iconHeight - textHeight - mPadding) / 2;
                mIconRect.bottom = mIconRect.top + iconHeight;
                // Calculate text Rect
                mTextRect.left = (width - textWidth) / 2;
                mTextRect.right = mTextRect.left + textWidth;
                mTextRect.top = mIconRect.bottom + mPadding;
                mTextRect.bottom = mTextRect.top + textHeight;
                break;
            case BOTTOM:
                // Calculate text Rect
                mTextRect.left = (width - textWidth) / 2;
                mTextRect.right = mTextRect.left + textWidth;
                mTextRect.top = (height - iconHeight - textHeight - mPadding) / 2;
                mTextRect.bottom = mTextRect.top + textHeight;

                // Calculate icon Rect
                mIconRect.left = (width - iconWidth) / 2;
                mIconRect.right = mIconRect.left + iconWidth;
                mIconRect.top = mTextRect.bottom + mPadding;
                mIconRect.bottom = mIconRect.top + iconHeight;
                break;
            case LEFT:
                // Calculate icon Rect
                mIconRect.left = (width - iconWidth - textWidth - mPadding) / 2;
                mIconRect.right = mIconRect.left + iconWidth;
                mIconRect.top = (height - iconHeight) / 2;
                mIconRect.bottom = mIconRect.top + iconHeight;

                // Calculate text Rect
                mTextRect.left = mIconRect.right + mPadding;
                mTextRect.right = mTextRect.left + textWidth;
                mTextRect.top = (height - textHeight) / 2;
                mTextRect.bottom = mTextRect.top + textHeight;
                break;
            case RIGHT:
                // Calculate text Rect
                mTextRect.left = (width - iconWidth - textWidth - mPadding) / 2;
                mTextRect.right = mTextRect.left + textWidth;
                mTextRect.top = (height - textHeight) / 2;
                mTextRect.bottom = mTextRect.top + textHeight;

                // Calculate icon Rect
                mIconRect.left = mTextRect.right + mPadding;
                mIconRect.right = mIconRect.left + iconWidth;
                mIconRect.top = (height - iconHeight) / 2;
                mIconRect.bottom = mIconRect.top + iconHeight;
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Clear canvas
        canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
        // draw icon
        drawIcon();
        canvas.drawBitmap(mShowBitmap, 0, 0, null);

        // draw text
        if (! TextUtils.isEmpty(mText)) {
            Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
            int baseline = mTextRect.top + (mTextRect.bottom - mTextRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;

            mTextPaint.setColor(mTextColor);
            mTextPaint.setAlpha(255);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(mText, mTextRect.centerX(), baseline, mTextPaint);

            mTextPaint.setColor(mTintColor);
            mTextPaint.setAlpha(mTintAlpha * 2 / 3);
            canvas.drawText(mText, mTextRect.centerX(), baseline, mTextPaint);
        }
        // 画提示框
        drawPromt(canvas);
    }

    private void drawPromt(Canvas canvas) {
        if (isShowPromt) {
            final int padding = UnitUtils.dip2px(getContext(), 4);
            final int paddingLess = UnitUtils.dip2px(getContext(), 2);
            final int x = mIconRect.right + padding;
            final int y = mIconRect.top + padding;

            // 计算提示数字大小
            Rect rect = new Rect();
            mNotiTextPaint.getTextBounds(mPromtNumber, 0, mPromtNumber.length(), rect);
            int textWidth = rect.width();
            int textHeight = rect.height();

            // 通过计算的大小 然控制画圆的半径
            int radius;
            if (TextUtils.isEmpty(mPromtNumber)) {
                radius = UnitUtils.dip2px(getContext(), 3);
            } else if (mPromtNumber.length() >= 3) {
                radius = Math.max(textWidth, textHeight) / 2 + paddingLess;
            } else {
                radius = Math.max(textWidth, textHeight) / 2 + padding;
            }

            canvas.drawCircle(x, y, radius, mNotiBgPaint);

            rect.top = y - textHeight / 2 - 2;
            rect.bottom = y + textHeight / 2 - 2;
            rect.left = x - textWidth / 2;
            rect.right = x + textWidth / 2;

            Paint.FontMetricsInt fontMetrics = mNotiTextPaint.getFontMetricsInt();
            int baseline = rect.top + (rect.bottom - rect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            mNotiTextPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(mPromtNumber, rect.centerX(), baseline, mNotiTextPaint);
        }
    }

    private void drawIcon() {
        //二级缓存
        mShowBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
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

    /**
     * 显示提示
     */
    public void showPromt(boolean show) {
        showPromt(show, Utils.EMPTY);
    }

    /**
     * 显示提示
     *
     * @param show   是否显示
     * @param number 显示的数字
     */
    public void showPromt(boolean show, String number) {
        isShowPromt = show;
        mPromtNumber = number.trim();
        if (number.length() >= 3) {
            mNotiTextPaint.setTextSize((float) (mTextSize * 0.6));
        } else {
            mNotiTextPaint.setTextSize((float) (mTextSize * 0.8));
        }
        invalidate();
    }

    /**
     * 显示提示
     *
     * @param show   是否显示
     * @param number 显示的数字
     */
    public void showPromt(boolean show, int number) {
        if (number > 99) {
            showPromt(show, "99+");
        } else {
            showPromt(show, String.valueOf(number));
        }
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

    public CharSequence getText() {
        return mText;
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

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
