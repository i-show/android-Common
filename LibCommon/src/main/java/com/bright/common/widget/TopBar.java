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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
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

import com.bright.common.R;
import com.bright.common.widget.prompt.IPrompt;
import com.bright.common.widget.prompt.PromptImageView;
import com.bright.common.widget.prompt.PromptTextView;


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
     * 中间的view
     */
    private MarqueeTextView mTitleView;
    private MarqueeTextView mSubTitleView;
    /**
     * 左边的View
     */
    private PromptImageView mLeftImageView;
    private PromptImageView mLeftImageView2;
    private PromptTextView mLeftTextView;
    /**
     * 右边的View
     */
    private PromptImageView mRightImageView;
    private PromptImageView mRightImageView2;
    private PromptTextView mRightTextView;


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
     * 左边图片信息
     */
    private int mLeftImageResId, mLeftImage2ResId;

    /**
     * 右边边图片信息
     */
    private int mRightImageResId, mRightImage2ResId;
    /**
     * 左边和右边的 背景 id
     */
    private int mLeftBackground, mRightBackground;
    private Drawable mLeftTextBackgound, mRightTextBackground;
    private Drawable mLeftTextDrawable;
    /**
     * 左边是否可见
     */
    private int mLeftImageVisibility, mLeftImage2Visibility, mLeftTextVisibility;
    /**
     * 右边是否可见
     */
    private int mRightImageVisibility, mRightImage2Visibility, mRightTextVisibility;

    private int mTitleVisibility, mSubTitleVisibility;

    /**
     * 左边和右边字体大小
     */
    private int mLeftTextSize, mRightTextSize;
    private int mLeftTextDrawablePadding;
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

    // TopBar的高度
    private int mTopBarHeight;
    // 图片或者文字的最小宽度
    private int mUnitWidth;

    private int mLeftImageWidth;
    private int mLeftImage2Width;
    private int mLeftTextViewWidth;

    private int mRightImageWidth;
    private int mRightImage2Width;
    private int mRightTextViewWidth;

    private int mTitleDesireWidth;
    private int mSubTitleDesireWidth;
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
    private int mExactlyHeightSpec;
    private int mExactlyWidthSpec;
    private int mAtmostHeightSpec;
    private int mGapSize;
    private int mSmallGapSize;

    private Handler mHandler;

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TopBar, R.attr.topbarStyle, 0);

        mLeftStr = a.getString(R.styleable.TopBar_leftText);
        mLeftTextSize = a.getDimensionPixelSize(R.styleable.TopBar_leftTextSize, 0);
        mLeftTextColor = a.getColorStateList(R.styleable.TopBar_leftTextColor);
        mLeftTextBackgound = a.getDrawable(R.styleable.TopBar_leftTextBackground);
        mLeftTextDrawable = a.getDrawable(R.styleable.TopBar_leftTextDrawable);
        mLeftTextDrawablePadding = a.getDimensionPixelSize(R.styleable.TopBar_leftTextDrawablePadding, 0);

        mLeftBackground = a.getResourceId(R.styleable.TopBar_leftBackground, 0);

        mLeftImageResId = a.getResourceId(R.styleable.TopBar_leftImage, 0);
        mLeftImageVisibility = a.getInt(R.styleable.TopBar_leftVisibility, View.VISIBLE);
        mLeftImage2ResId = a.getResourceId(R.styleable.TopBar_leftImage2, 0);
        mLeftImage2Visibility = a.getInt(R.styleable.TopBar_leftVisibility2, View.VISIBLE);

        mRightStr = a.getString(R.styleable.TopBar_rightText);
        mRightTextSize = a.getDimensionPixelSize(R.styleable.TopBar_rightTextSize, 0);
        mRightTextColor = a.getColorStateList(R.styleable.TopBar_rightTextColor);
        mRightTextBackground = a.getDrawable(R.styleable.TopBar_rightTextBackground);

        mRightBackground = a.getResourceId(R.styleable.TopBar_rightBackground, 0);

        mRightImageResId = a.getResourceId(R.styleable.TopBar_rightImage, 0);
        mRightImageVisibility = a.getInt(R.styleable.TopBar_rightVisibility, View.VISIBLE);
        mRightImage2ResId = a.getResourceId(R.styleable.TopBar_rightImage2, 0);
        mRightImage2Visibility = a.getInt(R.styleable.TopBar_rightVisibility2, View.VISIBLE);

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

        if (mLeftImageVisibility != GONE) {
            mLeftImageView.measure(mExactlyWidthSpec, mExactlyHeightSpec);
            mLeftImageWidth = mLeftImageView.getMeasuredWidth();
        }

        if (mLeftImage2Visibility != GONE) {
            mLeftImageView2.measure(mExactlyWidthSpec, mExactlyHeightSpec);
            mLeftImage2Width = mLeftImageView2.getMeasuredWidth();
        }

        if (mLeftTextVisibility != GONE) {
            if (mLeftTextBackgound != null) {
                mLeftTextView.measure(MeasureSpec.makeMeasureSpec(width / 3, MeasureSpec.AT_MOST), mAtmostHeightSpec);
            } else {
                mLeftTextView.measure(MeasureSpec.makeMeasureSpec(width / 3, MeasureSpec.AT_MOST), mExactlyHeightSpec);
            }
            mLeftTextViewWidth = mLeftTextView.getMeasuredWidth();
        }

        if (mRightImageVisibility != GONE) {
            mRightImageView.measure(mExactlyWidthSpec, mExactlyHeightSpec);
            mRightImageWidth = mRightImageView.getMeasuredWidth();
        }

        if (mRightImage2Visibility != GONE) {
            mRightImageView2.measure(mExactlyWidthSpec, mExactlyHeightSpec);
            mRightImage2Width = mRightImageView2.getMeasuredWidth();
        }

        if (mRightTextVisibility != GONE) {
            if (mRightTextBackground != null) {
                mRightTextView.measure(MeasureSpec.makeMeasureSpec(width / 3, MeasureSpec.AT_MOST), mAtmostHeightSpec);
            } else {
                mRightTextView.measure(MeasureSpec.makeMeasureSpec(width / 3, MeasureSpec.AT_MOST), mExactlyHeightSpec);
            }
            mRightTextViewWidth = mRightTextView.getMeasuredWidth();
        }


        if (mTitleVisibility != GONE) {
            mTitleView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), mAtmostHeightSpec);
        }

        if (mSubTitleVisibility != GONE) {
            mSubTitleView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), mAtmostHeightSpec);
        }

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = right - left;

        if (mLeftImageVisibility != GONE) {
            mLeftImageView.layout(left, top, left + mLeftImageWidth, bottom);
            left += mLeftImageWidth;
        }

        if (mLeftImage2Visibility != GONE) {
            mLeftImageView2.layout(left, top, left + mLeftImage2Width, bottom);
            left += mLeftImage2Width;
        }

        if (mLeftTextVisibility != GONE) {
            if (mLeftTextBackgound == null) {
                mLeftTextView.layout(left, top, left + mLeftTextViewWidth, bottom);
            } else {
                int _height = mLeftTextView.getMeasuredHeight();
                int _top = (mTopBarHeight - _height) / 2;
                if (left == 0) {
                    left = mGapSize;
                }
                mLeftTextView.layout(left, _top, left + mLeftTextViewWidth, _top + _height);
            }
        }

        if (mRightTextVisibility != GONE) {
            if (mRightTextBackground == null) {
                mRightTextView.layout(right - mRightTextViewWidth, top, right, bottom);
            } else {
                int _height = mRightTextView.getMeasuredHeight();
                int _top = (mTopBarHeight - _height) / 2;
                if (right == width) {
                    right -= mGapSize;
                }
                mRightTextView.layout(right - mRightTextViewWidth, _top, right, _top + _height);
            }
            right -= mRightTextViewWidth;
        }
        if (mRightImageVisibility != GONE) {
            mRightImageView.layout(right - mRightImageWidth, top, right, bottom);
            right -= mRightImageWidth;
        }

        if (mRightImage2Visibility != GONE) {
            mRightImageView2.layout(right - mRightImage2Width, top, right, bottom);
        }

        final int titleHeight = mTitleVisibility == GONE ? 0 : mTitleView.getMeasuredHeight();
        final int subTitleHeight = mSubTitleVisibility == GONE ? 0 : mSubTitleView.getMeasuredHeight();
        final int rightTotal = mRightImageWidth + mRightImage2Width + mRightTextViewWidth;
        final int leftTotal = mLeftImageWidth + mLeftImage2Width + mLeftTextViewWidth;

        top = (mTopBarHeight - titleHeight - subTitleHeight) / 2;
        if (mTitleVisibility != GONE) {
            mTitleView.layout(0, top, width, top + titleHeight);
            top += titleHeight;
            resetTitlePadding(width, leftTotal, rightTotal);
        }

        if (mSubTitleVisibility != GONE) {
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
        mAtmostHeightSpec = MeasureSpec.makeMeasureSpec(mTopBarHeight, MeasureSpec.AT_MOST);
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
        // just set left and right button width = height
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
            Log.i(TAG, "getTitle: return null mTitleVisibility is gone");
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
            addView(mTitleView, 0);

            computeTitleDesireWidth();
        }
        return mTitleView;
    }

    public TextView getSubTitle() {

        if (mSubTitleVisibility == GONE) {
            Log.i(TAG, "getTitle: return null mSubTitleVisibility is gone");
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


    public PromptImageView getLeftImageView() {

        if (mLeftImageVisibility == View.GONE) {
            Log.i(TAG, "getLeftImageView: is visiable gone just not add");
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

    public PromptImageView getLeftImageView2() {
        if (mLeftImage2Visibility == View.GONE) {
            Log.i(TAG, "getLeftImageView2: is visiable gone just not add");
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

    public PromptTextView getLeftTextView() {
        if (mLeftTextVisibility == View.GONE) {
            Log.i(TAG, "getLeftTextView: is visiable gone just not add");
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
            mLeftTextView.setMinWidth(mUnitWidth);
            mLeftTextView.setEllipsize(TextUtils.TruncateAt.END);
            if (mLeftTextBackgound != null) {
                mLeftTextView.setBackground(mLeftTextBackgound);
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

    public PromptImageView getRightImageView() {

        if (mRightImageVisibility == View.GONE) {
            Log.i(TAG, "getRightImageView: is visiable gone just not add");
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

    public PromptImageView getRightImageView2() {
        if (mRightImage2Visibility == View.GONE) {
            Log.i(TAG, "getRightImageView2: is visiable gone just not add");
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
            Log.i(TAG, "getRightTextView: is visiable gone just not add");
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
            mRightTextView.setMinWidth(mTopBarHeight);
            mRightTextView.setEllipsize(TextUtils.TruncateAt.END);
            if (mRightTextBackground != null) {
                mRightTextView.setBackground(mRightTextBackground);
            } else {
                mRightTextView.setBackgroundResource(mRightBackground);
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
            if (leftTotal == mLeftTextViewWidth && mLeftTextBackgound != null) {
                leftTotal += mGapSize;
            }

            if (rightTotal == mRightTextViewWidth && mRightTextBackground != null) {
                rightTotal += mGapSize;
            }
            mTitleView.setPadding(leftTotal + mGapSize, mSmallGapSize, rightTotal + difference + mGapSize, mSmallGapSize);
        } else {
            if (leftTotal == mLeftTextViewWidth && mLeftTextBackgound != null) {
                leftTotal += mGapSize;
            }

            if (rightTotal == mRightTextViewWidth && mRightTextBackground != null) {
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
        if (TextUtils.isEmpty(title)) {
            Log.i(TAG, "setText: title is empty");
            return;
        }
        if (mTitleView == null) {
            mTitleVisibility = VISIBLE;
            getTitle();
        }
        mTitleView.setText(title);
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
        if (TextUtils.isEmpty(title)) {
            return;
        }
        if (mSubTitleView == null) {
            mSubTitleVisibility = VISIBLE;
            getSubTitle();
        }
        mSubTitleView.setText(title);
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
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (mLeftTextView == null) {
            mLeftTextVisibility = VISIBLE;
            getLeftTextView();
        }
        mLeftTextView.setText(text);
    }

    public void setLeftImageResource(@DrawableRes int resId) {
        if (mLeftImageView == null) {
            mLeftImageVisibility = VISIBLE;
            getLeftImageView();
        }
        mLeftImageView.setImageResource(resId);
    }

    public void setLeft2ImageResource(@DrawableRes int resId) {
        if (mLeftImageView2 == null) {
            mLeftImage2Visibility = VISIBLE;
            getLeftImageView2();
        }
        mLeftImageView2.setImageResource(resId);
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
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (mRightTextView == null) {
            mRightTextVisibility = VISIBLE;
            getRightTextView();
        }
        mRightTextView.setText(text);
    }

    public void setRightImageResource(@DrawableRes int resId) {
        if (mRightImageView == null) {
            mRightImageVisibility = VISIBLE;
            getRightImageView();
        }
        mRightImageView.setImageResource(resId);
    }

    public void setRight2ImageResource(@DrawableRes int resId) {
        if (mRightImageView2 == null) {
            mRightImage2Visibility = VISIBLE;
            getRightImageView2();
        }
        mRightImageView2.setImageResource(resId);
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
    }

    public void setLeftImageVisibility(int visibility) {
        if (mLeftImageView != null) {
            mLeftImageView.setVisibility(visibility);
        }
    }

    public void setLeftImage2Visibility(int visibility) {
        if (mLeftImageView2 != null) {
            mLeftImageView2.setVisibility(visibility);
        }
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
    }

    public void setRightImageVisibility(int visibility) {
        if (mRightImageView != null) {
            mRightImageView.setVisibility(visibility);
        }
    }

    public void setRightImage2Visibility(int visibility) {
        if (mRightImageView2 != null) {
            mRightImageView2.setVisibility(visibility);
        }
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
        prompt.setPromptMode(IPrompt.MODE_NONE);
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