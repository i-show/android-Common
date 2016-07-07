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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bright.common.R;


public class TopBar extends ViewGroup implements OnClickListener {

    private static final String TAG = "TopBar";
    /**
     * 默认的点击图片
     */
    private static final int DEFAUTL_ACTION_IMAGE = android.R.color.transparent;

    /**
     * 左边按钮和右边按钮 的padding
     */
    private static final int DEFAULT_PADDING = 5;  // This is 5dp
    /**
     * 默认字体的颜色
     */
    private static final int DEFAULT_TEXT_COLOR = android.R.color.white;
    /**
     * 多次点击事件的消息
     */
    private static final int HANDLER_BATTER = 0x001;
    /**
     * 小红点位置的百分比
     */
    private static final float PROMAT_LOCATION = 0.25f;

    /**
     * title的字体颜色
     */
    private ColorStateList mTitleColor;
    /**
     * sub title 的字体颜色
     */
    private ColorStateList mSubTitleColor;

    /**
     * mBackGround 背景 的resid ， 整个TopBar的背景
     */
    private int mBackGround;
    /**
     * 左边和右边的 图片id
     */
    private int mLeftImageResId, mRightImageResId;
    /**
     * 左边和右边的 背景 id
     */
    private int mLeftBackground, mRightBackground;
    /**
     * 左边和右边是否可见
     */
    private int mLeftVisibility, mRightVisibility;
    /**
     * 左边和右边字体大小
     */
    private int mLeftTextSize, mRightTextSize;
    /**
     * 左边和右边字体颜色
     */
    private ColorStateList mLeftTextColor, mRightTextColor;

    // 默认的背景
    private int mItemBackgound;
    // Title字体大小
    private int mTitleSize;
    // SubTitle字体大小
    private int mSubTitleSize;
    // 左右按钮的最小宽度
    private int mActionMinWidth;

    // TopBar的高度
    private int mTopBarHeight;

    /**
     * Title 点击的次数
     */
    private int mClickCount;
    /**
     * Title 第一次点击的时间， 多次点击的标记位
     */
    private long mFirstTime;
    /**
     * 上次左边点击时间
     */
    private long mLastLeftClickTime;
    /**
     * 上次右边点击时间
     */
    private long mLastRightClickTime;

    private String mTitleStr;
    private String mSubTitleStr;

    private String mLeftStr;
    private String mRightStr;

    // Title 是否可以点击
    private boolean isClickable;
    // 是否启用密码格式 连续点击进入
    private boolean isSecretCode;
    // 是否显示Promt
    private boolean isShowLeftPromt;


    private MarqueeTextView mTitle;
    private MarqueeTextView mSubTitle;

    private ImageView mLeftImage;
    private ImageView mRightImage;

    private TextView mLeftText;
    private TextView mRightText;

    /**
     * 三大包容控件
     */
    private LinearLayout mTitleContent;
    private LinearLayout mLeftContent;
    private LinearLayout mRightContent;
    /**
     * 相关监听（左右点击事件）
     */
    private OnTopBarListener mTopBarListener;
    /**
     * 多次点击的监听
     */
    private OnSecretListener mSecretListener;
    /**
     * 提示小红点的画笔
     */
    private Paint mNotiPaint;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_BATTER:
                    if (mClickCount == 1) {
                        Log.i(TAG, "sec click");
                        if (mTopBarListener != null) {
                            mTopBarListener.onTitleClick(mTitleContent);
                        }
                    }
                    break;
            }
        }

    };


    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TopBar, R.attr.topbarStyle, 0);

        mLeftStr = a.getString(R.styleable.TopBar_leftText);
        mLeftTextSize = a.getInt(R.styleable.TopBar_leftTextSize, 0);
        mLeftTextColor = a.getColorStateList(R.styleable.TopBar_leftTextColor);
        mLeftImageResId = a.getResourceId(R.styleable.TopBar_leftImage, 0);
        mLeftVisibility = a.getInt(R.styleable.TopBar_leftVisibility, View.VISIBLE);
        mLeftBackground = a.getResourceId(R.styleable.TopBar_leftBackground, 0);

        mRightStr = a.getString(R.styleable.TopBar_rightText);
        mRightTextSize = a.getInt(R.styleable.TopBar_rightTextSize, 0);
        mRightTextColor = a.getColorStateList(R.styleable.TopBar_rightTextColor);
        mRightImageResId = a.getResourceId(R.styleable.TopBar_rightImage, 0);
        mRightVisibility = a.getInt(R.styleable.TopBar_rightVisibility, View.VISIBLE);
        mRightBackground = a.getResourceId(R.styleable.TopBar_rightBackground, 0);

        mTitleStr = a.getString(R.styleable.TopBar_text);
        mTitleSize = a.getDimensionPixelSize(R.styleable.TopBar_textSize, getDefaultTextSize(context));
        mTitleColor = a.getColorStateList(R.styleable.TopBar_textColor);

        mSubTitleStr = a.getString(R.styleable.TopBar_subText);
        mSubTitleSize = a.getDimensionPixelSize(R.styleable.TopBar_subTextSize, getDefaultSubTitleSize(context));
        mSubTitleColor = a.getColorStateList(R.styleable.TopBar_subTextColor);

        mTopBarHeight = a.getDimensionPixelOffset(R.styleable.TopBar_android_actionBarSize, getDefaultHeight());
        mBackGround = a.getResourceId(R.styleable.TopBar_android_background, android.R.color.transparent);
        mItemBackgound = a.getResourceId(R.styleable.TopBar_android_selectableItemBackground, android.R.color.transparent);

        isClickable = a.getBoolean(R.styleable.TopBar_clickable, false);
        isSecretCode = a.getBoolean(R.styleable.TopBar_secretable, false);


        mActionMinWidth = a.getDimensionPixelSize(R.styleable.TopBar_actionMinWidth, 0);

        if (isSecretCode) {
            isClickable = true;
        }

        a.recycle();
        checkEffectiveParams();

        // 顶部提示的画笔颜色
        mNotiPaint = new Paint();
        mNotiPaint.setAntiAlias(true);
        mNotiPaint.setDither(true);
        mNotiPaint.setColor(Color.RED);
    }


    /**
     * 进行检测参数的有效性
     */
    private void checkEffectiveParams() {
        // just set left and right button width = height
        if (mTitleColor == null) {
            mTitleColor = getResources().getColorStateList(DEFAULT_TEXT_COLOR);
        }

        if (mSubTitleColor == null) {
            mSubTitleColor = getResources().getColorStateList(DEFAULT_TEXT_COLOR);
        }

        if (mLeftTextSize == 0) {
            mLeftTextSize = getSuggestLeftOrRightTextSize();
        }

        if (mLeftTextColor == null) {
            mLeftTextColor = mTitleColor;
        }

        if (mLeftBackground == 0) {
            mLeftBackground = mItemBackgound;
        }


        if (mRightTextSize == 0) {
            mRightTextSize = getSuggestLeftOrRightTextSize();
        }

        if (mRightTextColor == null) {
            mRightTextColor = mTitleColor;
        }

        if (mRightBackground == 0) {
            mRightBackground = mItemBackgound;
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getTitle();
        getSubTitle();
        getLeftButton();
        getRightButton();
        setBackgroundResource(mBackGround);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        final int heightSpec = MeasureSpec.makeMeasureSpec(mTopBarHeight, MeasureSpec.EXACTLY);
        final int width = getMeasuredWidth();
        // 左右点击事件的最大宽度为 1/3的父类
        final int maxWidth = width / 3;
        // 宽度取值范围 小于 最大宽度  大于 Topbar的高度
        int minWidth = Math.max(Math.min(maxWidth, mActionMinWidth), mTopBarHeight);

        // 左右点击事件的最大宽度为 1/3的父类 就重新计算
        mLeftContent.measure(wrapSpec, heightSpec);
        final int leftWidth = Math.max(minWidth, Math.min(mLeftContent.getMeasuredWidth(), maxWidth));
        mLeftContent.measure(
                MeasureSpec.makeMeasureSpec(leftWidth, MeasureSpec.EXACTLY),
                heightSpec);

        // 左右点击事件的最大宽度为 1/3的父类 就重新计算
        mRightContent.measure(wrapSpec, heightSpec);
        final int rightWidth = Math.max(minWidth, Math.min(mRightContent.getMeasuredWidth(), maxWidth));
        mRightContent.measure(
                MeasureSpec.makeMeasureSpec(rightWidth, MeasureSpec.EXACTLY),
                heightSpec);

        // 这个地方的宽度有点问题
        mTitleContent.measure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mTopBarHeight, MeasureSpec.EXACTLY));

        setMeasuredDimension(widthMeasureSpec, mTopBarHeight);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int width = r - l;
        final int leftWidth = mLeftContent.getMeasuredWidth();
        final int rightWidth = mRightContent.getMeasuredWidth();
        // 左右按钮的宽度
        final int featureWidth = Math.max(leftWidth, rightWidth);

        if (mLeftVisibility == View.VISIBLE) {
            mLeftContent.layout(0, 0, featureWidth, mTopBarHeight);
        }

        if (mRightVisibility == View.VISIBLE) {
            mRightContent.layout(width - featureWidth, 0, width, mTopBarHeight);
        }

        // Title 设置padding 预防遮挡
        mTitleContent.setPadding(featureWidth, 0, featureWidth, 0);
        mTitleContent.layout(0, 0, width, mTopBarHeight);

        // 最后检测是否可以点击
        checkLeftEnable();
        checkRightEnable();
    }

    private void checkLeftEnable() {
        if (mLeftImage != null && mLeftImage.getDrawable() != null) {
            mLeftContent.setEnabled(true);
        } else if (mLeftText != null && !TextUtils.isEmpty(mLeftText.getText())) {
            mLeftContent.setEnabled(true);
        } else {
            mLeftContent.setEnabled(false);
        }
    }

    private void checkRightEnable() {
        if (mRightImage != null && mRightImage.getDrawable() != null) {
            mRightContent.setEnabled(true);
        } else if (mRightText != null && !TextUtils.isEmpty(mRightText.getText())) {
            mRightContent.setEnabled(true);
        } else {
            mRightContent.setEnabled(false);
        }
    }


    @Override
    public void onClick(View v) {
        // id是非静态的，所以不能使用Switch Case
        final int id = v.getId();
        if (id == R.id.left) {
            performLeftClick(v);
        } else if (id == R.id.right) {
            performRightClick(v);
        } else if (id == R.id.center) {
            if (isSecretCode) {
                long second = System.currentTimeMillis();
                if (second - mFirstTime < 400) {
                    ++mClickCount;
                } else {
                    mClickCount = 1;
                }

                if (mClickCount > 1 && mSecretListener != null) {
                    mSecretListener.onSecretClick(v, mClickCount);
                } else {
                    mHandler.removeMessages(HANDLER_BATTER);
                    mHandler.sendEmptyMessageDelayed(HANDLER_BATTER, 400);
                }
                mFirstTime = second;
            } else {
                if (mTopBarListener != null) {
                    mTopBarListener.onTitleClick(v);
                }
            }
        }

    }

    public LinearLayout getTitleContent() {
        if (mTitleContent == null) {
            mTitleContent = new LinearLayout(getContext());
            mTitleContent.setId(R.id.center);
            mTitleContent.setGravity(Gravity.CENTER);
            mTitleContent.setOrientation(LinearLayout.VERTICAL);
            mTitleContent.setEnabled(isClickable);
            mTitleContent.setClickable(isClickable);
            mTitleContent.setOnClickListener(this);
            addView(mTitleContent);
        }
        return mTitleContent;
    }

    public TextView getTitle() {
        // Must be have parent
        getTitleContent();

        if (mTitle == null) {
            mTitle = new MarqueeTextView(getContext());
            mTitle.setText(mTitleStr);
            mTitle.setTextColor(mTitleColor);
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);
            mTitle.setIncludeFontPadding(false);
            mTitle.setGravity(Gravity.CENTER);
            mTitleContent.addView(mTitle);
        }
        return mTitle;
    }

    public TextView getSubTitle() {
        // Must be have parent
        getTitleContent();

        if (TextUtils.isEmpty(mSubTitleStr)) {
            return null;
        }

        if (mSubTitle == null) {
            mSubTitle = new MarqueeTextView(getContext());
            mSubTitle.setText(mSubTitleStr);
            mSubTitle.setTextColor(mSubTitleColor);
            mSubTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSubTitleSize);
            mSubTitle.setIncludeFontPadding(false);
            mSubTitle.setGravity(Gravity.CENTER);
            mTitleContent.addView(mSubTitle);
        }
        return mSubTitle;
    }

    public View getLeftButton() {

        final int padding = dip2px(DEFAULT_PADDING);
        mLeftContent = new LinearLayout(getContext());
        mLeftContent.setId(R.id.left);
        mLeftContent.setGravity(Gravity.CENTER);
        mLeftContent.setBackgroundResource(mLeftBackground);
        mLeftContent.setOrientation(LinearLayout.HORIZONTAL);
        mLeftContent.setClickable(true);
        mLeftContent.setOnClickListener(this);
        mLeftContent.setPadding(padding, 0, padding, 0);

        if (mLeftImage == null && mLeftVisibility != View.GONE) {
            mLeftImage = new ImageView(getContext());
            if (mLeftImageResId != 0) {
                mLeftImage.setImageResource(mLeftImageResId);
            }
            mLeftContent.addView(mLeftImage);
        }

        if (!TextUtils.isEmpty(mLeftStr)) {
            mLeftText = new TextView(getContext());
            mLeftText.setText(mLeftStr);
            mLeftText.setGravity(Gravity.CENTER);
            mLeftText.setTextColor(mLeftTextColor);
            mLeftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftTextSize);
            mLeftText.setSingleLine();
            mLeftText.setEllipsize(TextUtils.TruncateAt.END);
            mLeftContent.addView(mLeftText);
        }

        addView(mLeftContent);
        return mLeftContent;
    }

    public View getRightButton() {
        // 先进行移除
        if (mRightContent != null) {
            mRightContent.removeAllViews();
            mRightText = null;
            mRightImage = null;
            removeView(mRightContent);
        }

        final int padding = dip2px(DEFAULT_PADDING);
        mRightContent = new LinearLayout(getContext());
        mRightContent.setId(R.id.right);
        mRightContent.setGravity(Gravity.CENTER);
        mRightContent.setBackgroundResource(mRightBackground);
        mRightContent.setOrientation(LinearLayout.HORIZONTAL);
        mRightContent.setClickable(true);
        mRightContent.setOnClickListener(this);
        mRightContent.setPadding(padding, 0, padding, 0);

        if (!TextUtils.isEmpty(mRightStr)) {
            mRightText = new TextView(getContext());
            mRightText.setText(mRightStr);
            mRightText.setGravity(Gravity.CENTER);
            mRightText.setTextColor(mRightTextColor);
            mRightText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize);
            mRightText.setSingleLine();
            mRightText.setEllipsize(TextUtils.TruncateAt.END);
            mRightContent.addView(mRightText);
        }

        if (mRightImage == null && mRightVisibility != View.GONE) {
            mRightImage = new ImageView(getContext());
            if (mRightImageResId != 0) {
                mRightImage.setImageResource(mRightImageResId);
            }
            mRightContent.addView(mRightImage);
        }

        addView(mRightContent);
        return mRightContent;
    }

    private int getSuggestLeftOrRightTextSize() {
        return (int) (mTitleSize * 0.8);
    }

    /**
     * 左侧点击
     */
    private void performLeftClick(View v) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastLeftClickTime < 500) {
            Log.i(TAG, "performLeftClick: Too fast");
            return;
        }
        mLastLeftClickTime = nowTime;

        if (mTopBarListener != null) {
            mTopBarListener.onLeftClick(v);
        }
    }

    /**
     * 右侧点击
     */
    private void performRightClick(View v) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastRightClickTime < 500) {
            Log.i(TAG, "performRightClick: Too fast");
            return;
        }
        mLastRightClickTime = nowTime;

        if (mTopBarListener != null) {
            mTopBarListener.onRightClick(v);
        }
    }

    /**
     * 设置Title
     */
    public void setText(@StringRes int resId) {
        if (resId == 0) {
            return;
        }
        String title = getContext().getString(resId);
        setText(title);
    }

    /**
     * 设置Title
     */
    public void setText(String title) {
        if (TextUtils.isEmpty(title)) {
            return;
        }
        mTitle.setText(title);
    }

    /**
     * 设置标题颜色
     */
    public void setTextColor(@ColorRes int color) {
        mTitleColor = getResources().getColorStateList(color);
        mTitle.setTextColor(mTitleColor);
    }

    /**
     * 设置副标题
     */
    public void setSubText(@StringRes int resId) {
        if (resId == 0) {
            Log.i(TAG, "setSubText: resid is error");
            return;
        }
        String text = getContext().getString(resId);
        setSubText(text);
    }

    /**
     * 设置subTitle
     */
    public void setSubText(String title) {
        // if title is null just return
        if (TextUtils.isEmpty(title)) {
            return;
        }
        // 不能删除，防止问题：开始没有mSubTitle之后add进去
        mSubTitleStr = title;
        getSubTitle();
        mSubTitle.setText(title);
    }

    /**
     * 设置左边字体显示
     */
    public void setLeftText(@StringRes int resId) {
        if (resId == 0) {
            Log.i(TAG, "setLeftText: resid is error");
            return;
        }
        String text = getContext().getString(resId);
        setLeftText(text);
    }

    /**
     * 设置左边字体显示
     */
    public void setLeftText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        // 不能删除，防止问题：预防之后add进去
        mLeftStr = text;
        getLeftButton();
        mLeftText.setText(text);
    }

    /**
     * 设置右边字体显示
     */
    public void setRightText(@StringRes int resId) {
        if (resId == 0) {
            Log.i(TAG, "setRightText: resid is error");
            return;
        }
        String text = getContext().getString(resId);
        setRightText(text);
    }

    /**
     * 设置右边字体显示
     */
    public void setRightText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        mRightStr = text;
        if (mRightText == null) {
            getRightButton();
        }
        mRightText.setText(text);
    }

    /**
     * 获取显示的View；
     */
    public TextView getRightTextView() {
        return mRightText;
    }

    public void setLeftImageDrawable(Drawable d) {
        if (mLeftImage != null) {
            mLeftImage.setImageDrawable(d);
        }
    }

    public void setLeftImageResource(int resId) {
        if (mLeftImage != null) {
            mLeftImage.setImageResource(resId);
        }
    }

    public void setRightImageDrawable(Drawable d) {
        if (mRightImage != null) {
            mRightImage.setImageDrawable(d);
        }
    }

    public void setRightImageResource(int resId) {
        if (mRightImage != null && resId != 0) {
            mRightImage.setImageResource(resId);
        }
    }


    public ImageView getRightImageView() {
        return mRightImage;
    }


    /**
     * 设置左边是否可见
     */
    public void setLeftVisibility(int visibility) {
        if (mLeftContent != null) {
            mLeftContent.setVisibility(visibility);
        }
    }

    /**
     * 设置右边是否可见
     */
    public void setRightVisibility(int visibility) {
        if (mRightContent != null) {
            mRightContent.setVisibility(visibility);
        }
    }

    public void setRightImageVisibility(int visibility) {
        if (mRightImage != null) {
            mRightImage.setVisibility(visibility);
        }
    }

    public void setRightTextVisibility(int visibility) {
        if (mRightText != null) {
            mRightText.setVisibility(visibility);
        }
    }


    /**
     * @param show true: 显示左上方的小红点
     *             false: 不显示小红点
     */
    public void showLeftPrompt(boolean show) {
        isShowLeftPromt = show;
        invalidate();
    }

    /**
     * 获取默认标题字体大小
     */
    public int getDefaultTextSize(Context context) {
        return context.getResources().getDimensionPixelOffset(R.dimen.topbar_title);
    }

    /**
     * 获取默认副标题标题字体大小
     */
    public int getDefaultSubTitleSize(Context context) {
        return context.getResources().getDimensionPixelOffset(R.dimen.topbar_sub_title);
    }

    /**
     * 添加监听
     */
    public void setOnTopBarListener(OnTopBarListener listener) {
        mTopBarListener = listener;
    }

    /**
     * 添加彩蛋监听
     */
    public void setOnSecretListener(OnSecretListener listener) {
        mSecretListener = listener;
    }

    /**
     * 获取默认TopBar的高度
     */
    private int getDefaultHeight() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.dp_48);
    }

    public int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isShowLeftPromt) {
            int radius = dip2px(3);
            int x = Math.max(mLeftContent.getMeasuredWidth(), mRightContent.getMeasuredWidth());
            x = (int) (x * (1 - PROMAT_LOCATION));
            int y = (int) (getHeight() * PROMAT_LOCATION);
            canvas.drawCircle(x, y, radius, mNotiPaint);
        }

    }


    /**
     * 点击监听
     */
    public interface OnTopBarListener {
        /**
         * 点击了左边
         */
        void onLeftClick(View v);

        /**
         * 点击了右边
         */
        void onRightClick(View v);

        /**
         * 点击了标题
         */
        void onTitleClick(View v);
    }

    /**
     * 彩蛋监听
     */
    public interface OnSecretListener {
        /**
         * @param v     点击的View
         * @param count 点击的次数
         */
        void onSecretClick(View v, int count);
    }
}