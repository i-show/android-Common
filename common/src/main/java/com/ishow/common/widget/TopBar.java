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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import com.ishow.common.R;
import com.ishow.common.widget.imageview.PromptImageView;
import com.ishow.common.widget.prompt.IPrompt;
import com.ishow.common.widget.textview.MarqueeTextView;
import com.ishow.common.widget.textview.PromptTextView;

@SuppressWarnings("unused")
public class TopBar extends ViewGroup implements OnClickListener {

    private static final String TAG = "TopBar";
    /**
     * 单位宽度和高度的比率
     */
    private static final float UNIT_WIDTH_RADIO = 0.88f;
    /**
     * 多次点击事件的消息
     */
    private static final int HANDLER_BATTER = 0x001;
    /**
     * 提示信息
     */
    private static final float DEFAULT_IMAGE_WIDTH_PROMPT_SCALE = 0.17f;
    private static final float DEFAULT_IMAGE_HEIGHT_PROMPT_SCALE = 0.19f;
    private static final float DEFAULT_TEXT_WIDTH_PROMPT_SCALE = 0.14f;
    private static final float DEFAULT_TEXT_HEIGHT_PROMPT_SCALE = 0.25f;

    /**
     * 左侧第一个图片
     */
    private PromptImageView mLeftImageView;
    private int mLeftImageWidth;
    private int mLeftImageMinWidth;
    private int mLeftImageResId;
    private int mLeftImageVisibility;
    private int mLeftImageWidthSpec;
    /**
     * 左侧第二个图片
     */
    private PromptImageView mLeftImageView2;
    private int mLeftImage2Width;
    private int mLeftImage2ResId;
    private int mLeftImage2Visibility;
    /**
     * 左侧文本
     */
    private PromptTextView mLeftTextView;
    private Drawable mLeftTextDrawable;
    private Drawable mLeftTextBackground;
    private ColorStateList mLeftTextColor;
    private String mLeftStr;
    private int mLeftTextSize;
    private int mLeftTextViewWidth;
    private int mLeftTextMinWidth;
    private int mLeftTextVisibility;
    private int mLeftTextDrawablePadding;
    /**
     * 主标题
     */
    private MarqueeTextView mTitleView;
    private ColorStateList mTitleColor;
    private String mTitleStr;
    private int mTitleSize;
    private int mTitleDesireWidth;
    private int mTextStyle;
    private int mTitleVisibility;
    /**
     * 副标题
     */
    private MarqueeTextView mSubTitleView;
    private ColorStateList mSubTitleColor;
    private String mSubTitleStr;
    private int mSubTitleDesireWidth;
    private int mSubTitleSize;
    private int mSubTitleVisibility;
    /**
     * 右侧第一个图片
     */
    private PromptImageView mRightImageView;
    private int mRightImageWidth;
    private int mRightImageMinWidth;
    private int mRightImageResId;
    private int mRightImageVisibility;
    private int mRightImageWidthSpec;
    /**
     * 右侧第二个图片
     */
    private PromptImageView mRightImageView2;
    private int mRightImage2Width;
    private int mRightImage2ResId;
    private int mRightImage2Visibility;
    /**
     * 右侧文本
     */
    private PromptTextView mRightTextView;
    private ColorStateList mRightTextColor;
    private Drawable mRightTextBackground;
    private Drawable mRightTextDrawable;
    private String mRightStr;
    private int mRightTextWidth;
    private int mRightTextMinWidth;
    private int mRightTextSize;
    private int mRightTextDrawablePadding;
    private int mRightTextVisibility;

    private int mBackGround;
    private int mLeftBackground;
    private int mRightBackground;
    private int mItemBackground;

    /**
     * TopBar的高度
     */
    private int mTopBarHeight;
    /**
     *  图片或者文字的最小宽度
     */
    private int mUnitWidth;
    /**
     * Title 点击的次数
     */
    private int mClickCount;
    /**
     * Title 第一次点击的时间， 多次点击的标记位
     */
    private long mFirstTime;
    private long mLastLeftClickTime;
    private long mLastRightClickTime;
    /**
     * Title 是否可以点击
     */
    private boolean isClickable;
    /**
     * 是否启用密码格式 连续点击进入
     */
    private boolean isSecretCode;
    /**
     * 相关监听（左右点击事件）
     */
    private OnTopBarListener mTopBarListener;
    /**
     * 多次点击的监听
     */
    private OnSecretListener mSecretListener;

    /**
     * 高度的Spec
     */
    private int mExactlyWidthSpec;
    private int mExactlyHeightSpec;
    private int mAtMostHeightSpec;

    private int mGapSize;
    private int mSmallGapSize;
    private Handler mHandler;

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TopBar, R.attr.topBarStyle, 0);

        mLeftStr = a.getString(R.styleable.TopBar_leftText);
        mLeftTextSize = a.getDimensionPixelSize(R.styleable.TopBar_leftTextSize, 0);
        mLeftTextColor = a.getColorStateList(R.styleable.TopBar_leftTextColor);
        mLeftTextBackground = a.getDrawable(R.styleable.TopBar_leftTextBackground);
        mLeftTextDrawable = a.getDrawable(R.styleable.TopBar_leftTextDrawable);
        mLeftTextDrawablePadding = a.getDimensionPixelSize(R.styleable.TopBar_leftTextDrawablePadding, 0);
        mLeftTextMinWidth = a.getDimensionPixelSize(R.styleable.TopBar_leftTextMinWidth, 0);

        mLeftBackground = a.getResourceId(R.styleable.TopBar_leftImageBackground, 0);

        mLeftImageResId = a.getResourceId(R.styleable.TopBar_leftImage, 0);
        mLeftImageMinWidth = a.getDimensionPixelSize(R.styleable.TopBar_leftImageMinWidth, 0);
        mLeftImageVisibility = a.getInt(R.styleable.TopBar_leftImageVisibility, View.VISIBLE);

        mLeftImage2ResId = a.getResourceId(R.styleable.TopBar_leftImage2, 0);
        mLeftImage2Visibility = a.getInt(R.styleable.TopBar_leftImage2Visibility, View.VISIBLE);

        mRightStr = a.getString(R.styleable.TopBar_rightText);
        mRightTextSize = a.getDimensionPixelSize(R.styleable.TopBar_rightTextSize, 0);
        mRightTextColor = a.getColorStateList(R.styleable.TopBar_rightTextColor);
        mRightTextBackground = a.getDrawable(R.styleable.TopBar_rightTextBackground);
        mRightTextDrawable = a.getDrawable(R.styleable.TopBar_rightTextDrawable);
        mRightTextDrawablePadding = a.getDimensionPixelSize(R.styleable.TopBar_rightTextDrawablePadding, 0);
        mRightTextMinWidth = a.getDimensionPixelSize(R.styleable.TopBar_rightTextMinWidth, 0);

        mRightBackground = a.getResourceId(R.styleable.TopBar_rightImageBackground, 0);

        mRightImageResId = a.getResourceId(R.styleable.TopBar_rightImage, 0);
        mRightImageVisibility = a.getInt(R.styleable.TopBar_rightImageVisibility, View.VISIBLE);
        mRightImageMinWidth = a.getDimensionPixelSize(R.styleable.TopBar_rightImageMinWidth, 0);
        mRightImage2ResId = a.getResourceId(R.styleable.TopBar_rightImage2, 0);
        mRightImage2Visibility = a.getInt(R.styleable.TopBar_rightImage2Visibility, View.VISIBLE);

        mTitleStr = a.getString(R.styleable.TopBar_text);
        mTitleSize = a.getDimensionPixelSize(R.styleable.TopBar_textSize, getDefaultTextSize(context));
        mTitleColor = a.getColorStateList(R.styleable.TopBar_textColor);
        mTextStyle = a.getInt(R.styleable.TopBar_textStyle, 0);

        mSubTitleStr = a.getString(R.styleable.TopBar_subText);
        mSubTitleSize = a.getDimensionPixelSize(R.styleable.TopBar_subTextSize, getDefaultSubTitleSize(context));
        mSubTitleColor = a.getColorStateList(R.styleable.TopBar_subTextColor);

        mTopBarHeight = a.getDimensionPixelOffset(R.styleable.TopBar_android_actionBarSize, getDefaultHeight());
        mBackGround = a.getResourceId(R.styleable.TopBar_android_background, android.R.color.transparent);
        mItemBackground = a.getResourceId(R.styleable.TopBar_android_selectableItemBackground, android.R.color.transparent);

        isClickable = a.getBoolean(R.styleable.TopBar_clickable, false);
        isSecretCode = a.getBoolean(R.styleable.TopBar_secret, false);

        if (isSecretCode) {
            isClickable = true;
        }

        a.recycle();

        initNecessaryParams();
        initView();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, mExactlyHeightSpec);
        int width = getMeasuredWidth();

        if (mLeftImageVisibility != GONE && mLeftImageView != null) {
            mLeftImageView.measure(mLeftImageWidthSpec, mExactlyHeightSpec);
            mLeftImageWidth = mLeftImageView.getMeasuredWidth();
        }

        if (mLeftImage2Visibility != GONE && mLeftImageView2 != null) {
            mLeftImageView2.measure(mExactlyWidthSpec, mExactlyHeightSpec);
            mLeftImage2Width = mLeftImageView2.getMeasuredWidth();
        }

        if (mLeftTextVisibility != GONE && mLeftTextView != null) {
            if (mLeftTextBackground != null) {
                mLeftTextView.measure(MeasureSpec.makeMeasureSpec(width / 3, MeasureSpec.AT_MOST), mAtMostHeightSpec);
            } else {
                mLeftTextView.measure(MeasureSpec.makeMeasureSpec(width / 3, MeasureSpec.AT_MOST), mExactlyHeightSpec);
            }
            mLeftTextViewWidth = mLeftTextView.getMeasuredWidth();
        }

        if (mRightImageVisibility != GONE && mRightImageView != null) {
            mRightImageView.measure(mRightImageWidthSpec, mExactlyHeightSpec);
            mRightImageWidth = mRightImageView.getMeasuredWidth();
        }

        if (mRightImage2Visibility != GONE && mRightImageView2 != null) {
            mRightImageView2.measure(mExactlyWidthSpec, mExactlyHeightSpec);
            mRightImage2Width = mRightImageView2.getMeasuredWidth();
        }

        if (mRightTextVisibility != GONE && mRightTextView != null) {
            if (mRightTextBackground != null) {
                mRightTextView.measure(MeasureSpec.makeMeasureSpec(width / 3, MeasureSpec.AT_MOST), mAtMostHeightSpec);
            } else {
                mRightTextView.measure(MeasureSpec.makeMeasureSpec(width / 3, MeasureSpec.AT_MOST), mExactlyHeightSpec);
            }
            mRightTextWidth = mRightTextView.getMeasuredWidth();
        }


        if (mTitleVisibility != GONE && mTitleView != null) {
            mTitleView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), mAtMostHeightSpec);
        }

        if (mSubTitleVisibility != GONE && mSubTitleView != null) {
            mSubTitleView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), mAtMostHeightSpec);
        }

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = right - left;
        if (mLeftImageVisibility != GONE && mLeftImageView != null) {
            mLeftImageView.layout(left, 0, left + mLeftImageWidth, mTopBarHeight);
            left += mLeftImageWidth;
        }

        if (mLeftImage2Visibility != GONE && mLeftImageView2 != null) {
            mLeftImageView2.layout(left, 0, left + mLeftImage2Width, mTopBarHeight);
            left += mLeftImage2Width;
        }

        if (mLeftTextVisibility != GONE && mLeftTextView != null) {
            if (mLeftTextBackground == null) {
                mLeftTextView.layout(left, 0, left + mLeftTextViewWidth, mTopBarHeight);
            } else {
                int _height = mLeftTextView.getMeasuredHeight();
                int _top = (mTopBarHeight - _height) / 2;
                if (left == 0) {
                    left = mGapSize;
                }
                mLeftTextView.layout(left, _top, left + mLeftTextViewWidth, _top + _height);
            }
        }

        if (mRightTextVisibility != GONE && mRightTextView != null) {
            if (mRightTextBackground == null) {
                mRightTextView.layout(right - mRightTextWidth, 0, right, mTopBarHeight);
            } else {
                int _height = mRightTextView.getMeasuredHeight();
                int _top = (mTopBarHeight - _height) / 2;
                if (right == width) {
                    right -= mGapSize;
                }
                mRightTextView.layout(right - mRightTextWidth, _top, right, _top + _height);
            }
            right -= mRightTextWidth;
        }
        if (mRightImageVisibility != GONE && mRightImageView != null) {
            mRightImageView.layout(right - mRightImageWidth, 0, right, mTopBarHeight);
            right -= mRightImageWidth;
        }

        if (mRightImage2Visibility != GONE && mRightImageView2 != null) {
            mRightImageView2.layout(right - mRightImage2Width, 0, right, mTopBarHeight);
        }

        final int titleHeight = mTitleVisibility == GONE ? 0 : mTitleView.getMeasuredHeight();
        final int subTitleHeight = mSubTitleVisibility == GONE ? 0 : mSubTitleView.getMeasuredHeight();
        final int rightTotal = mRightImageWidth + mRightImage2Width + mRightTextWidth;
        final int leftTotal = mLeftImageWidth + mLeftImage2Width + mLeftTextViewWidth;

        top = (mTopBarHeight - titleHeight - subTitleHeight) / 2;
        if (mTitleVisibility != GONE && mTitleView != null) {
            mTitleView.layout(0, top, width, top + titleHeight);
            top += titleHeight;
            resetTitlePadding(width, leftTotal, rightTotal);
        }

        if (mSubTitleVisibility != GONE && mSubTitleView != null) {
            mSubTitleView.layout(0, top, width, top + subTitleHeight);
            resetSubTitlePadding(width, leftTotal, rightTotal);
        }
    }


    @Override
    public void onClick(View v) {
        // id是非静态的，所以不能使用Switch Case
        final int id = v.getId();
        if (id == R.id.leftText || id == R.id.leftImage || id == R.id.leftImage2) {
            performLeftClick(v);
        } else if (id == R.id.rightText || id == R.id.rightImage || id == R.id.rightImage2) {
            performRightClick(v);
        } else if (id == R.id.title || id == R.id.subTitle) {
            performCenterClick(v);
        }

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mHandler = new RecycleHandler();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }

    /**
     * 初始化一些必要的变量
     */
    private void initNecessaryParams() {
        mUnitWidth = (int) (mTopBarHeight * UNIT_WIDTH_RADIO);
        mExactlyHeightSpec = MeasureSpec.makeMeasureSpec(mTopBarHeight, MeasureSpec.EXACTLY);
        mExactlyWidthSpec = MeasureSpec.makeMeasureSpec(mUnitWidth, MeasureSpec.EXACTLY);
        mLeftImageWidthSpec = MeasureSpec.makeMeasureSpec(Math.max(mLeftImageMinWidth, mUnitWidth), MeasureSpec.EXACTLY);
        mRightImageWidthSpec = MeasureSpec.makeMeasureSpec(Math.max(mRightImageMinWidth, mUnitWidth), MeasureSpec.EXACTLY);
        mAtMostHeightSpec = MeasureSpec.makeMeasureSpec(mTopBarHeight, MeasureSpec.AT_MOST);
        mGapSize = getResources().getDimensionPixelSize(R.dimen.gap_grade_1);
        mSmallGapSize = getResources().getDimensionPixelSize(R.dimen.gap_grade_0);
    }

    private void initView() {
        // 在加载View之前先确认参数是否正确，或更换为默认值
        checkEffectiveParams();

        getTitle();
        getSubTitle();

        getLeftImageView();
        getLeftImageView2();
        getLeftTextView();

        getRightImageView();
        getRightImageView2();
        getRightTextView();

        setBackgroundResource(mBackGround);
    }


    /**
     * 进行检测参数的有效性
     */
    private void checkEffectiveParams() {
        // just set left and right button with = height
        if (mTitleColor == null) {
            mTitleColor = ColorStateList.valueOf(Color.WHITE);
        }

        if (mSubTitleColor == null) {
            mSubTitleColor = ColorStateList.valueOf(Color.WHITE);
        }

        if (mLeftTextSize == 0) {
            mLeftTextSize = getSuggestLeftOrRightTextSize();
        }

        if (mLeftTextColor == null) {
            mLeftTextColor = mTitleColor;
        }

        if (mLeftBackground == 0) {
            mLeftBackground = mItemBackground;
        }


        if (mRightTextSize == 0) {
            mRightTextSize = getSuggestLeftOrRightTextSize();
        }

        if (mRightTextColor == null) {
            mRightTextColor = mTitleColor;
        }

        if (mRightBackground == 0) {
            mRightBackground = mItemBackground;
        }


        if (mLeftImageResId == 0) {
            mLeftImageVisibility = GONE;
        }
        if (mLeftImage2ResId == 0) {
            mLeftImage2Visibility = GONE;
        }
        if (TextUtils.isEmpty(mLeftStr)) {
            mLeftTextVisibility = GONE;
        }


        if (mRightImageResId == 0) {
            mRightImageVisibility = GONE;
        }
        if (mRightImage2ResId == 0) {
            mRightImage2Visibility = GONE;
        }
        if (TextUtils.isEmpty(mRightStr)) {
            mRightTextVisibility = GONE;
        }

        if (TextUtils.isEmpty(mTitleStr)) {
            mTitleVisibility = GONE;
        }

        if (TextUtils.isEmpty(mSubTitleStr)) {
            mSubTitleVisibility = GONE;
        }

    }


    public TextView getTitle() {

        if (mTitleVisibility == GONE) {
            return null;
        }

        if (mTitleView == null) {
            mTitleView = new MarqueeTextView(getContext());
            mTitleView.setId(R.id.title);
            mTitleView.setText(mTitleStr);
            mTitleView.setPadding(mGapSize, mSmallGapSize, mGapSize, mSmallGapSize);
            mTitleView.setTextColor(mTitleColor);
            mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);
            mTitleView.setIncludeFontPadding(false);
            mTitleView.setOnClickListener(this);
            mTitleView.setGravity(Gravity.CENTER);
            mTitleView.setClickable(isClickable);
            mTitleView.setTypeface(Typeface.defaultFromStyle(mTextStyle));
            addView(mTitleView, 0);

            computeTitleDesireWidth();
        }
        return mTitleView;
    }

    public TextView getSubTitle() {

        if (mSubTitleVisibility == GONE) {
            return null;
        }

        if (mSubTitleView == null) {
            mSubTitleView = new MarqueeTextView(getContext());
            mSubTitleView.setId(R.id.subTitle);
            mSubTitleView.setText(mSubTitleStr);
            mSubTitleView.setPadding(mGapSize, 0, mGapSize, 0);
            mSubTitleView.setOnClickListener(this);
            mSubTitleView.setTextColor(mSubTitleColor);
            mSubTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSubTitleSize);
            mSubTitleView.setIncludeFontPadding(false);
            mSubTitleView.setGravity(Gravity.CENTER);
            mSubTitleView.setClickable(isClickable);
            addView(mSubTitleView, 0);

            computeSubTitleDesireWidth();
        }
        return mSubTitleView;
    }


    @SuppressWarnings("UnusedReturnValue")
    public PromptImageView getLeftImageView() {

        if (mLeftImageVisibility == View.GONE) {
            return null;
        }

        if (mLeftImageView == null) {
            mLeftImageView = new PromptImageView(getContext());
            mLeftImageView.setId(R.id.leftImage);
            mLeftImageView.setVisibility(mLeftImageVisibility);
            mLeftImageView.setImageResource(mLeftImageResId);
            mLeftImageView.setBackgroundResource(mLeftBackground);
            mLeftImageView.setScaleType(ImageView.ScaleType.CENTER);
            mLeftImageView.setOnClickListener(this);
            mLeftImageView.setMinimumWidth(mUnitWidth);
            mLeftImageView.setMinimumHeight(mTopBarHeight);
            setDefaultPromptState(mLeftImageView);
            addView(mLeftImageView);
        }
        return mLeftImageView;
    }

    @SuppressWarnings("UnusedReturnValue")
    public PromptImageView getLeftImageView2() {
        if (mLeftImage2Visibility == View.GONE) {
            return null;
        }

        if (mLeftImageView2 == null) {
            mLeftImageView2 = new PromptImageView(getContext());
            mLeftImageView2.setId(R.id.leftImage2);
            mLeftImageView2.setVisibility(mLeftImage2Visibility);
            mLeftImageView2.setImageResource(mLeftImage2ResId);
            mLeftImageView2.setBackgroundResource(mLeftBackground);
            mLeftImageView2.setScaleType(ImageView.ScaleType.CENTER);
            mLeftImageView2.setMinimumWidth(mUnitWidth);
            mLeftImageView2.setMinimumHeight(mTopBarHeight);
            mLeftImageView2.setOnClickListener(this);
            setDefaultPromptState(mLeftImageView2);
            addView(mLeftImageView2);
        }
        return mLeftImageView;
    }

    @SuppressWarnings("UnusedReturnValue")
    public PromptTextView getLeftTextView() {
        if (mLeftTextVisibility == View.GONE) {
            return null;
        }

        if (mLeftTextView == null) {
            mLeftTextView = new PromptTextView(getContext());
            mLeftTextView.setId(R.id.leftText);
            mLeftTextView.setText(mLeftStr);
            mLeftTextView.setPadding(mGapSize, mGapSize, mGapSize, mGapSize);
            mLeftTextView.setGravity(Gravity.CENTER);
            mLeftTextView.setTextColor(mLeftTextColor);
            mLeftTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftTextSize);
            mLeftTextView.setOnClickListener(this);
            mLeftTextView.setLines(1);
            // 至少要这么宽 位了美观
            mLeftTextView.setMinWidth(Math.max(mLeftTextMinWidth, mUnitWidth));
            mLeftTextView.setEllipsize(TextUtils.TruncateAt.END);
            if (mLeftTextBackground != null) {
                mLeftTextView.setBackground(mLeftTextBackground);
            } else {
                mLeftTextView.setBackgroundResource(mLeftBackground);
            }

            if (mLeftTextDrawable != null) {
                mLeftTextView.setCompoundDrawablesWithIntrinsicBounds(mLeftTextDrawable, null, null, null);
                mLeftTextView.setCompoundDrawablePadding(mLeftTextDrawablePadding);
            }

            setDefaultPromptState(mLeftTextView);
            addView(mLeftTextView);
        }
        return mLeftTextView;
    }

    @SuppressWarnings("UnusedReturnValue")
    public PromptImageView getRightImageView() {

        if (mRightImageVisibility == View.GONE) {
            return null;
        }

        if (mRightImageView == null) {
            mRightImageView = new PromptImageView(getContext());
            mRightImageView.setId(R.id.rightImage);
            mRightImageView.setVisibility(mRightImageVisibility);
            mRightImageView.setImageResource(mRightImageResId);
            mRightImageView.setBackgroundResource(mRightBackground);
            mRightImageView.setScaleType(ImageView.ScaleType.CENTER);
            mRightImageView.setOnClickListener(this);
            setDefaultPromptState(mRightImageView);
            addView(mRightImageView);
        }
        return mRightImageView;
    }

    @SuppressWarnings("UnusedReturnValue")
    public PromptImageView getRightImageView2() {
        if (mRightImage2Visibility == View.GONE) {
            return null;
        }

        if (mRightImageView2 == null) {
            mRightImageView2 = new PromptImageView(getContext());
            mRightImageView2.setId(R.id.rightImage2);
            mRightImageView2.setVisibility(mRightImage2Visibility);
            mRightImageView2.setImageResource(mRightImage2ResId);
            mRightImageView2.setBackgroundResource(mRightBackground);
            mRightImageView2.setScaleType(ImageView.ScaleType.CENTER);
            mRightImageView2.setOnClickListener(this);
            setDefaultPromptState(mRightImageView2);
            addView(mRightImageView2);
        }
        return mRightImageView2;
    }

    public PromptTextView getRightTextView() {
        if (mRightTextVisibility == View.GONE) {
            return null;
        }

        if (mRightTextView == null) {
            mRightTextView = new PromptTextView(getContext());
            mRightTextView.setId(R.id.rightText);
            mRightTextView.setText(mRightStr);
            mRightTextView.setPadding(mGapSize, mGapSize, mGapSize, mGapSize);
            mRightTextView.setGravity(Gravity.CENTER);
            mRightTextView.setTextColor(mRightTextColor);
            mRightTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize);
            mRightTextView.setLines(1);
            mRightTextView.setOnClickListener(this);
            // 至少要这么宽 位了美观
            mRightTextView.setMinWidth(Math.max(mUnitWidth, mRightTextMinWidth));
            mRightTextView.setEllipsize(TextUtils.TruncateAt.END);
            if (mRightTextBackground != null) {
                mRightTextView.setBackground(mRightTextBackground);
            } else {
                mRightTextView.setBackgroundResource(mRightBackground);
            }

            if (mRightTextDrawable != null) {
                mRightTextView.setCompoundDrawablesWithIntrinsicBounds(mRightTextDrawable, null, null, null);
                mRightTextView.setCompoundDrawablePadding(mRightTextDrawablePadding);
            }

            setDefaultPromptState(mRightTextView);
            addView(mRightTextView);
        }
        return mRightTextView;
    }

    private void resetTitlePadding(int width, int leftTotal, int rightTotal) {
        int bestPadding = Math.max(leftTotal, rightTotal);
        int bestWidth = width - bestPadding * 2 - mGapSize * 2;
        int maxWidth = width - rightTotal - leftTotal - mGapSize * 2;
        // 差值
        int difference = maxWidth - mTitleDesireWidth;

        if (mTitleDesireWidth <= bestWidth) {
            mTitleView.setPadding(bestPadding, mSmallGapSize, bestPadding, mSmallGapSize);
        } else if (mTitleDesireWidth >= maxWidth) {
            mTitleView.setPadding(leftTotal + mGapSize, mSmallGapSize, rightTotal + mGapSize, mSmallGapSize);
        } else if (leftTotal > rightTotal) {
            if (leftTotal == mLeftTextViewWidth && mLeftTextBackground != null) {
                leftTotal += mGapSize;
            }

            if (rightTotal == mRightTextWidth && mRightTextBackground != null) {
                rightTotal += mGapSize;
            }
            mTitleView.setPadding(leftTotal + mGapSize, mSmallGapSize, rightTotal + difference + mGapSize, mSmallGapSize);
        } else {
            if (leftTotal == mLeftTextViewWidth && mLeftTextBackground != null) {
                leftTotal += mGapSize;
            }

            if (rightTotal == mRightTextWidth && mRightTextBackground != null) {
                rightTotal += mGapSize;
            }
            mTitleView.setPadding(leftTotal + mGapSize + difference, mSmallGapSize, rightTotal + mGapSize, mSmallGapSize);
        }
    }

    private void resetSubTitlePadding(int width, int leftTotal, int rightTotal) {
        int bestPadding = Math.max(leftTotal, rightTotal);
        int bestWidth = width - bestPadding * 2 - mGapSize * 2;
        int maxWidth = width - rightTotal - leftTotal - mGapSize * 2;
        // 差值
        int difference = maxWidth - mSubTitleDesireWidth;

        if (mSubTitleDesireWidth < bestWidth) {
            mSubTitleView.setPadding(bestPadding, 0, bestPadding, 0);
        } else if (mSubTitleDesireWidth > maxWidth) {
            mSubTitleView.setPadding(leftTotal + mGapSize, 0, rightTotal + mGapSize, 0);
        } else if (leftTotal > rightTotal) {
            mSubTitleView.setPadding(leftTotal + mGapSize, 0, rightTotal + mGapSize + difference, 0);
        } else {
            mSubTitleView.setPadding(leftTotal + mGapSize + difference, 0, rightTotal + mGapSize, 0);
        }
    }

    /**
     * 左后文字的字体大小应该是Title字体的0。8
     */
    private int getSuggestLeftOrRightTextSize() {
        return (int) (mTitleSize * 0.8f);
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

    private void performCenterClick(View v) {
        if (isSecretCode) {
            long second = System.currentTimeMillis();
            if (second - mFirstTime < 400) {
                ++mClickCount;
            } else {
                mClickCount = 1;
            }

            if (mClickCount > 1 && mSecretListener != null) {
                mSecretListener.onSecretClick(v, mClickCount);
            } else if (mHandler != null) {
                mHandler.removeMessages(HANDLER_BATTER);
                Message message = new Message();
                message.what = HANDLER_BATTER;
                message.obj = v;
                mHandler.sendMessageDelayed(message, 400);
            }
            mFirstTime = second;
        } else {
            if (mTopBarListener != null) {
                mTopBarListener.onTitleClick(v);
            }
        }
    }

    /**
     * 设置Title
     */
    public void setText(@StringRes int resId) {
        String title = getContext().getString(resId);
        setText(title);
    }

    /**
     * 设置Title
     */
    public void setText(String title) {
        mTitleVisibility = VISIBLE;
        if (mTitleView == null) {
            getTitle();
        }
        mTitleView.setText(title);
        computeTitleDesireWidth();
        requestLayout();
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

        mSubTitleVisibility = VISIBLE;
        if (mSubTitleView == null) {
            getSubTitle();
        }
        mSubTitleView.setText(title);
        computeSubTitleDesireWidth();
        requestLayout();
    }

    /**
     * 设置左边字体显示
     */
    public void setLeftText(@StringRes int resId) {
        String text = getContext().getString(resId);
        setLeftText(text);
    }

    /**
     * 设置左边字体显示
     */
    public void setLeftText(String text) {
        mLeftTextVisibility = VISIBLE;
        if (mLeftTextView == null) {
            getLeftTextView();
        }
        mLeftTextView.setText(text);
        requestLayout();
    }

    /**
     * 右边文本的左侧小图
     */
    public void setLeftTextLeftDrawable(@DrawableRes int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        setLeftTextLeftDrawable(drawable);
    }

    /**
     * 右边文本的左侧小图
     */
    public void setLeftTextLeftDrawable(Drawable drawable) {
        if (drawable == null) {
            Log.i(TAG, "setRightTextLeftDrawable: drawable is null");
            return;
        }
        mLeftTextVisibility = VISIBLE;
        if (mLeftTextView == null) {
            getLeftTextView();
        }
        mLeftTextView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        mLeftTextView.setCompoundDrawablePadding(mLeftTextDrawablePadding);
    }

    /**
     * 右边文本的左侧小图
     */
    public void setLeftTextRightDrawable(@DrawableRes int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        setLeftTextRightDrawable(drawable);
    }

    /**
     * 右边文本的左侧小图
     */
    public void setLeftTextRightDrawable(Drawable drawable) {
        if (drawable == null) {
            Log.i(TAG, "setRightTextLeftDrawable: drawable is null");
            return;
        }
        mLeftTextVisibility = VISIBLE;
        if (mLeftTextView == null) {
            getLeftTextView();
        }
        mLeftTextView.setCompoundDrawablesWithIntrinsicBounds(mLeftTextDrawable, null, drawable, null);
        mLeftTextView.setCompoundDrawablePadding(mLeftTextDrawablePadding);
    }

    /**
     * 左侧边文本和图片的padding
     */
    public void setLeftTextLeftDrawablePadding(int padding) {
        mLeftTextVisibility = VISIBLE;
        if (mLeftTextView == null) {
            getLeftTextView();
        }
        mLeftTextDrawablePadding = padding;
        mLeftTextView.setCompoundDrawablePadding(padding);
    }


    public void setLeftImageResource(@DrawableRes int resId) {
        mLeftImageVisibility = VISIBLE;
        if (mLeftImageView == null) {
            getLeftImageView();
        }
        mLeftImageView.setImageResource(resId);
        requestLayout();
    }

    public void setLeftImageDrawable(@NonNull Drawable drawable) {
        mLeftImageVisibility = VISIBLE;
        if (mLeftImageView == null) {
            getLeftImageView();
        }
        mLeftImageView.setImageDrawable(drawable);
        requestLayout();
    }


    public void setLeft2ImageResource(@DrawableRes int resId) {
        mLeftImage2Visibility = VISIBLE;
        if (mLeftImageView2 == null) {
            getLeftImageView2();
        }
        mLeftImageView2.setImageResource(resId);
        requestLayout();
    }

    /**
     * 设置右边字体显示
     */
    public void setRightText(@StringRes int resId) {
        String text = getContext().getString(resId);
        setRightText(text);
    }

    /**
     * 设置右边字体显示
     */
    public void setRightText(String text) {
        mRightTextVisibility = VISIBLE;
        if (mRightTextView == null) {
            getRightTextView();
        }
        mRightTextView.setText(text);
        requestLayout();
    }

    /**
     * 右边文本的左侧小图
     */
    public void setRightTextLeftDrawable(@DrawableRes int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        setRightTextLeftDrawable(drawable);
    }

    /**
     * 右边文本的左侧小图
     */
    public void setRightTextLeftDrawable(Drawable drawable) {
        if (drawable == null) {
            Log.i(TAG, "setRightTextLeftDrawable: drawable is null");
            return;
        }
        mRightTextVisibility = VISIBLE;
        if (mRightTextView == null) {
            getRightTextView();
        }
        mRightTextView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        mRightTextView.setCompoundDrawablePadding(mRightTextDrawablePadding);
    }


    /**
     * 右边文本的左侧小图
     */
    public void setRightTextLeftDrawablePadding(int padding) {
        mRightTextVisibility = VISIBLE;
        if (mRightTextView == null) {
            getRightTextView();
        }
        mRightTextView.setCompoundDrawablePadding(padding);
        mRightTextDrawablePadding = padding;
    }

    /**
     * 右侧第一个图
     */
    public void setRightImageResource(@DrawableRes int resId) {
        mRightImageVisibility = VISIBLE;
        if (mRightImageView == null) {
            getRightImageView();
        }
        mRightImageView.setImageResource(resId);
        requestLayout();
    }

    public void setRightImageMinWidth(int minWidth) {
        mRightImageMinWidth = minWidth;
        mRightImageWidthSpec = MeasureSpec.makeMeasureSpec(Math.max(mRightImageMinWidth, mUnitWidth), MeasureSpec.EXACTLY);
        requestLayout();
    }

    /**
     * 右侧第二个图
     */
    public void setRight2ImageResource(@DrawableRes int resId) {
        mRightImage2Visibility = VISIBLE;
        if (mRightImageView2 == null) {
            getRightImageView2();
        }
        mRightImageView2.setImageResource(resId);
        requestLayout();
    }


    /**
     * 设置左边是否可见
     */
    public void setLeftVisibility(int visibility) {
        setLeftTextVisibility(visibility);
        setLeftImageVisibility(visibility);
        setLeftImage2Visibility(visibility);
    }

    public void setLeftTextVisibility(int visibility) {
        if (mLeftTextView != null) {
            mLeftTextView.setVisibility(visibility);
        }
        mLeftTextVisibility = visibility;
    }

    public void setLeftImageVisibility(int visibility) {
        if (mLeftImageView != null) {
            mLeftImageView.setVisibility(visibility);
        }
        mLeftImageVisibility = visibility;
    }

    public void setLeftImage2Visibility(int visibility) {
        if (mLeftImageView2 != null) {
            mLeftImageView2.setVisibility(visibility);
        }
        mLeftImage2Visibility = visibility;
    }

    /**
     * 设置右边是否可见
     */
    public void setRightVisibility(int visibility) {
        setRightTextVisibility(visibility);
        setRightImageVisibility(visibility);
        setRightImage2Visibility(visibility);
    }

    public void setRightTextVisibility(int visibility) {
        if (mRightTextView != null) {
            mRightTextView.setVisibility(visibility);
        }
        mRightTextVisibility = visibility;
    }

    public void setRightImageVisibility(int visibility) {
        if (mRightImageView != null) {
            mRightImageView.setVisibility(visibility);
        }
        mRightImageVisibility = visibility;
    }

    public void setRightImage2Visibility(int visibility) {
        if (mRightImageView2 != null) {
            mRightImageView2.setVisibility(visibility);
        }
        mRightImage2Visibility = visibility;
    }

    /**
     * 计算Title 想要的宽度
     */
    private void computeTitleDesireWidth() {
        if (mTitleView == null) {
            mTitleDesireWidth = 0;
            return;
        }

        mTitleDesireWidth = (int) Layout.getDesiredWidth(mTitleView.getText(), mTitleView.getPaint());
    }


    /**
     * 计算SubTitle 想要的宽度
     */
    private void computeSubTitleDesireWidth() {
        if (mSubTitleView == null) {
            mSubTitleDesireWidth = 0;
            return;
        }

        mSubTitleDesireWidth = (int) Layout.getDesiredWidth(mSubTitleView.getText(), mSubTitleView.getPaint());
    }

    private void setDefaultPromptState(IPrompt prompt) {
        if (prompt == null) {
            Log.i(TAG, "setDefaultPromptState: ");
            return;
        }
        if (prompt instanceof PromptTextView) {
            prompt.setPromptWidthPaddingScale(DEFAULT_TEXT_WIDTH_PROMPT_SCALE);
            prompt.setPromptHeightPaddingScale(DEFAULT_TEXT_HEIGHT_PROMPT_SCALE);
        } else {
            prompt.setPromptWidthPaddingScale(DEFAULT_IMAGE_WIDTH_PROMPT_SCALE);
            prompt.setPromptHeightPaddingScale(DEFAULT_IMAGE_HEIGHT_PROMPT_SCALE);
        }
        prompt.setPromptMode(IPrompt.PromptMode.NONE);
        prompt.commit();
    }

    /**
     * 获取默认TopBar的高度
     */
    private int getDefaultHeight() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.top_bar_height);
    }

    /**
     * 获取默认标题字体大小
     */
    public int getDefaultTextSize(Context context) {
        return context.getResources().getDimensionPixelOffset(R.dimen.top_bar_title_text_size);
    }

    /**
     * 获取默认副标题标题字体大小
     */
    public int getDefaultSubTitleSize(Context context) {
        return context.getResources().getDimensionPixelOffset(R.dimen.top_bar_sub_title_text_size);
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

    /**
     * 用来添加和回收的Handler
     */
    @SuppressLint("HandlerLeak")
    private class RecycleHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_BATTER:
                    if (mClickCount == 1) {
                        Log.i(TAG, "sec click");
                        if (mTopBarListener != null) {
                            mTopBarListener.onTitleClick((View) msg.obj);
                        }
                    }
                    break;
            }
        }
    }
}