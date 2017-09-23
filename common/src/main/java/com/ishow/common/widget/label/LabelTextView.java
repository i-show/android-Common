package com.ishow.common.widget.label;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

import com.ishow.common.R;

public class LabelTextView extends AppCompatTextView implements ILabelView {

    LabelViewHelper mLabelViewHelper;

    public LabelTextView(Context context) {
        this(context, null);
    }

    public LabelTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLabelViewHelper = new LabelViewHelper(this, context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mLabelViewHelper.onDraw(canvas);
    }


    /**
     * 设置Label的高度
     *
     * @param height Label高度
     */
    @Override
    public void setLabelHeight(@DimenRes int height) {
        mLabelViewHelper.setLabelHeight(height);
    }

    /**
     * 获取Label的高度
     *
     * @return Label的高度
     */
    @Override
    public int getLabelHeight() {
        return mLabelViewHelper.getLabelHeight();
    }

    /**
     * 设置Label距离边的距离
     *
     * @param distance 距离边的距离
     */
    @Override
    public void setLabelDistance(@DimenRes int distance) {
        mLabelViewHelper.setLabelDistance(distance);
    }

    /**
     * 获取Label距离边的距离
     */
    @Override
    public int getLabelDistance() {
        return mLabelViewHelper.getLabelDistance();
    }

    /**
     * Label是否显示
     *
     * @return 是否显示
     */
    @Override
    public boolean isLabelEnable() {
        return mLabelViewHelper.isLabelEnable();
    }

    /**
     * 设置Label是否显示
     *
     * @param enable 是否显示
     */
    @Override
    public void setLabelEnable(boolean enable) {
        mLabelViewHelper.setLabelEnable(enable);
    }

    /**
     * 获取Label的位置
     */
    @Override
    public int getLabelGravity() {
        return mLabelViewHelper.getLabelGravity();
    }

    /**
     * 设置Label的位置
     *
     * @param gravity Label的位置
     */
    @Override
    public void setLabelGravity(@LabelViewHelper.Gravity int gravity) {
        mLabelViewHelper.setLabelGravity(gravity);
    }

    /**
     * 获取Label的字体颜色
     *
     * @return @ColorInt 返回字体颜色
     */
    @Override
    public int getLabelTextColor() {
        return mLabelViewHelper.getLabelTextColor();
    }

    /**
     * 设置Label的字体颜色
     *
     * @param textColor 字体颜色
     */
    @Override
    public void setLabelTextColor(@ColorInt int textColor) {
        mLabelViewHelper.setLabelTextColor(textColor);
    }

    /**
     * 设置背景颜色
     *
     * @return 返回背景颜色
     * @see #setLabelBackgroundColor(int)
     */
    @Override
    public int getLabelBackgroundColor() {
        return mLabelViewHelper.getLabelBackgroundColor();
    }

    /**
     * 获取背景颜色
     *
     * @param backgroundColor 背景颜色
     * @see #getLabelBackgroundColor()
     */
    @Override
    public void setLabelBackgroundColor(@ColorInt int backgroundColor) {
        mLabelViewHelper.setLabelBackgroundColor(backgroundColor);
    }

    /**
     * 获取显示的文本
     *
     * @return Label Text
     * @see #setLabelText(String)
     */
    @Override
    public String getLabelText() {
        return mLabelViewHelper.getLabelText();
    }

    /**
     * 设置显示的文本
     *
     * @param text 文本内容
     * @see #getLabelText()
     */
    @Override
    public void setLabelText(String text) {
        mLabelViewHelper.setLabelText(text);
    }

    /**
     * 获取当前字体的大小
     *
     * @return 返回Label的字体大小
     * @see #setLabelTextSize
     */
    @Override
    public int getLabelTextSize() {
        return mLabelViewHelper.getLabelTextSize();
    }

    /**
     * 设置当前字体的大小
     *
     * @param textSize 字体大小
     */
    @Override
    public void setLabelTextSize(@DimenRes int textSize) {
        mLabelViewHelper.setLabelTextSize(textSize);
    }

    /**
     * 设置字体样式
     *
     * @param textStyle 字体样式
     */
    @Override
    public void setLabelTextStyle(@LabelViewHelper.TextStyle int textStyle) {
        mLabelViewHelper.setLabelTextStyle(textStyle);
    }

    /**
     * 设置边框的颜色
     *
     * @param color 边框颜色
     */
    @Override
    public void setLabelStrokeColor(@ColorInt int color) {
        mLabelViewHelper.setLabelStrokeColor(color);
    }

    /**
     * 设置边框的宽度
     *
     * @param widthRes 边框的宽度
     */
    @Override
    public void setLabelStrokeWidth(@DimenRes int widthRes) {
        mLabelViewHelper.setLabelStrokeWidth(widthRes);
    }


    @Override
    public View getView() {
        return this;
    }
}


