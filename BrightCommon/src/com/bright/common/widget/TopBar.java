/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
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
     * 单位宽度和高度的比率
     */
    private static final float UNIT_WIDTH_RADIO = 0.88f;
    /**
     * 默认字体的颜色
     */
    private static final int DEFAULT_TEXT_COLOR = android.R.color.white;
    /**
     * 多次点击事件的消息
     */
    private static final int HANDLER_BATTER = 0x001;

    /**
     * 中间的view
     */
    private MarqueeTextView mTitleView;
    private MarqueeTextView mSubTitleView;
    /**
     * 左边的View
     */
    private ImageView mLeftImageView;
    private ImageView mLeftImageView2;
    private TextView mLeftTextView;
    /**
     * 右边的View
     */
    private ImageView mRightImageView;
    private ImageView mRightImageView2;
    private TextView mRightTextView;


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

    // 左边是否是一个整体点击事件
    private boolean isLeftWhole;


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

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TopBar, R.attr.topbarStyle, 0);

        isLeftWhole = a.getBoolean(R.styleable.TopBar_isLeftWhole, false);

        mLeftStr = a.getString(R.styleable.TopBar_leftText);
        mLeftTextSize = a.getInt(R.styleable.TopBar_leftTextSize, 0);
        mLeftTextColor = a.getColorStateList(R.styleable.TopBar_leftTextColor);
        mLeftTextBackgound = a.getDrawable(R.styleable.TopBar_leftTextBackground);

        mLeftBackground = a.getResourceId(R.styleable.TopBar_leftBackground, 0);

        mLeftImageResId = a.getResourceId(R.styleable.TopBar_leftImage, 0);
        mLeftImageVisibility = a.getInt(R.styleable.TopBar_leftVisibility, View.VISIBLE);
        mLeftImage2ResId = a.getResourceId(R.styleable.TopBar_leftImage2, 0);
        mLeftImage2Visibility = a.getInt(R.styleable.TopBar_leftVisibility2, View.VISIBLE);

        mRightStr = a.getString(R.styleable.TopBar_rightText);
        mRightTextSize = a.getInt(R.styleable.TopBar_rightTextSize, 0);
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
                mLeftTextView.layout(left, _top, left + mLeftTextViewWidth, _top + _height);
            }
        }

        if (mRightTextVisibility != GONE) {
            if (mRightTextBackground == null) {
                mRightTextView.layout(right - mRightTextViewWidth, top, right, bottom);
            } else {
                int _height = mRightTextView.getMeasuredHeight();
                int _top = (mTopBarHeight - _height) / 2;
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
        if (id == R.id.left) {
            performLeftClick(v);
        } else if (id == R.id.right) {
            performRightClick(v);
        } else if (id == R.id.center) {
            performCenterClick(v);
        }

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
            mTitleView.setText(mTitleStr);
            mTitleView.setPadding(mGapSize, mSmallGapSize, mGapSize, mSmallGapSize);
            mTitleView.setTextColor(mTitleColor);
            mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);
            mTitleView.setIncludeFontPadding(false);
            mTitleView.setGravity(Gravity.CENTER);
            addView(mTitleView);

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
            mSubTitleView.setText(mSubTitleStr);
            mSubTitleView.setPadding(mGapSize, 0, mGapSize, 0);
            mSubTitleView.setTextColor(mSubTitleColor);
            mSubTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSubTitleSize);
            mSubTitleView.setIncludeFontPadding(false);
            mSubTitleView.setGravity(Gravity.CENTER);
            addView(mSubTitleView);

            computeSubTitleDesireWidth();
        }
        return mSubTitleView;
    }


    private ImageView getLeftImageView() {

        if (mLeftImageVisibility == View.GONE) {
            Log.i(TAG, "getLeftImageView: is visiable gone just not add");
            return null;
        }

        if (mLeftImageView == null) {
            mLeftImageView = new ImageView(getContext());
            mLeftImageView.setId(R.id.leftImage);
            mLeftImageView.setVisibility(mLeftImageVisibility);
            mLeftImageView.setImageResource(mLeftImageResId);
            mLeftImageView.setBackgroundResource(mLeftBackground);
            mLeftImageView.setScaleType(ImageView.ScaleType.CENTER);
            mLeftImageView.setOnClickListener(this);
            mLeftImageView.setMinimumWidth(mUnitWidth);
            mLeftImageView.setMinimumHeight(mTopBarHeight);
            addView(mLeftImageView);
        }
        return mLeftImageView;
    }

    private ImageView getLeftImageView2() {
        if (mLeftImage2Visibility == View.GONE) {
            Log.i(TAG, "getLeftImageView2: is visiable gone just not add");
            return null;
        }

        if (mLeftImageView2 == null) {
            mLeftImageView2 = new ImageView(getContext());
            mLeftImageView2.setId(R.id.leftImage2);
            mLeftImageView2.setVisibility(mLeftImage2Visibility);
            mLeftImageView2.setImageResource(mLeftImage2ResId);
            mLeftImageView2.setBackgroundResource(mLeftBackground);
            mLeftImageView2.setScaleType(ImageView.ScaleType.CENTER);
            mLeftImageView2.setMinimumWidth(mUnitWidth);
            mLeftImageView2.setMinimumHeight(mTopBarHeight);
            mLeftImageView2.setOnClickListener(this);
            addView(mLeftImageView2);
        }
        return mLeftImageView;
    }

    private TextView getLeftTextView() {
        if (mLeftTextVisibility == View.GONE) {
            Log.i(TAG, "getLeftTextView: is visiable gone just not add");
            return null;
        }

        if (mLeftTextView == null) {
            mLeftTextView = new TextView(getContext());
            mLeftTextView.setId(R.id.leftText);
            mLeftTextView.setText(mLeftStr);
            mLeftTextView.setPadding(mGapSize, mGapSize, mGapSize, mGapSize);
            mLeftTextView.setGravity(Gravity.CENTER);
            mLeftTextView.setTextColor(mLeftTextColor);
            mLeftTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftTextSize);
            mLeftTextView.setOnClickListener(this);
            mLeftTextView.setSingleLine();
            // 至少要这么宽 位了美观
            mLeftTextView.setMinWidth(mUnitWidth);
            mLeftTextView.setEllipsize(TextUtils.TruncateAt.END);
            if (mLeftTextBackgound != null) {
                mLeftTextView.setBackground(mLeftTextBackgound);
            } else {
                mLeftTextView.setBackgroundResource(mLeftBackground);
            }
            addView(mLeftTextView);
        }
        return mLeftTextView;
    }

    private ImageView getRightImageView() {

        if (mRightImageVisibility == View.GONE) {
            Log.i(TAG, "getRightImageView: is visiable gone just not add");
            return null;
        }

        if (mRightImageView == null) {
            mRightImageView = new ImageView(getContext());
            mRightImageView.setId(R.id.rightImage);
            mRightImageView.setVisibility(mRightImageVisibility);
            mRightImageView.setImageResource(mRightImageResId);
            mRightImageView.setBackgroundResource(mRightBackground);
            mRightImageView.setScaleType(ImageView.ScaleType.CENTER);
            mRightImageView.setOnClickListener(this);
            addView(mRightImageView);
        }
        return mRightImageView;
    }

    private ImageView getRightImageView2() {
        if (mRightImage2Visibility == View.GONE) {
            Log.i(TAG, "getRightImageView2: is visiable gone just not add");
            return null;
        }

        if (mRightImageView2 == null) {
            mRightImageView2 = new ImageView(getContext());
            mRightImageView2.setId(R.id.rightImage2);
            mRightImageView2.setVisibility(mRightImage2Visibility);
            mRightImageView2.setImageResource(mRightImage2ResId);
            mRightImageView2.setBackgroundResource(mRightBackground);
            mRightImageView2.setScaleType(ImageView.ScaleType.CENTER);
            mRightImageView2.setOnClickListener(this);
            addView(mRightImageView2);
        }
        return mRightImageView2;
    }

    private TextView getRightTextView() {
        if (mRightTextVisibility == View.GONE) {
            Log.i(TAG, "getRightTextView: is visiable gone just not add");
            return null;
        }

        if (mRightTextView == null) {
            mRightTextView = new TextView(getContext());
            mRightTextView.setId(R.id.rightText);
            mRightTextView.setText(mRightStr);
            mRightTextView.setPadding(mGapSize, mGapSize, mGapSize, mGapSize);
            mRightTextView.setGravity(Gravity.CENTER);
            mRightTextView.setTextColor(mRightTextColor);
            mRightTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize);
            mRightTextView.setSingleLine();
            mRightTextView.setOnClickListener(this);
            // 至少要这么宽 位了美观
            mRightTextView.setMinWidth(mTopBarHeight);
            mRightTextView.setEllipsize(TextUtils.TruncateAt.END);
            if (mRightTextBackground != null) {
                mRightTextView.setBackground(mRightTextBackground);
            } else {
                mRightTextView.setBackgroundResource(mRightBackground);
            }
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
            mTitleView.setPadding(leftTotal + mGapSize, mSmallGapSize, rightTotal + difference + mGapSize, mSmallGapSize);
        } else {
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
            } else {
                //mHandler.removeMessages(HANDLER_BATTER);
                //mHandler.sendEmptyMessageDelayed(HANDLER_BATTER, 400);
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
        mTitleView.setText(title);
    }

    /**
     * 设置标题颜色
     */
    public void setTextColor(@ColorRes int color) {
        mTitleColor = getResources().getColorStateList(color);
        mTitleView.setTextColor(mTitleColor);
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
        mSubTitleView.setText(title);
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
        //TODO
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
        //TODO
    }


    public void setLeftImageDrawable(Drawable d) {
        if (mLeftImageView != null) {
            mLeftImageView.setImageDrawable(d);
        }
    }

    public void setLeftImageResource(int resId) {
        if (mLeftImageView != null) {
            mLeftImageView.setImageResource(resId);
        }
    }

    public void setRightImageDrawable(Drawable d) {
        if (mRightImageView != null) {
            mRightImageView.setImageDrawable(d);
        }
    }

    public void setRightImageResource(int resId) {
        if (mRightImageView != null && resId != 0) {
            mRightImageView.setImageResource(resId);
        }
    }


    /**
     * 设置左边是否可见
     */
    public void setLeftVisibility(int visibility) {
        //TODO
    }

    /**
     * 设置右边是否可见
     */
    public void setRightVisibility(int visibility) {
        //TODO
    }

    public void setRightImageVisibility(int visibility) {
        if (mRightImageView != null) {
            mRightImageView.setVisibility(visibility);
        }
    }

    public void setRightTextVisibility(int visibility) {
        if (mRightTextView != null) {
            mRightTextView.setVisibility(visibility);
        }
    }


    private void computeTitleDesireWidth() {
        if (mTitleView == null) {
            mTitleDesireWidth = 0;
        }

        mTitleDesireWidth = (int) Layout.getDesiredWidth(mTitleView.getText(), mTitleView.getPaint());
    }

    private void computeSubTitleDesireWidth() {
        if (mSubTitleView == null) {
            mSubTitleDesireWidth = 0;
        }

        mSubTitleDesireWidth = (int) Layout.getDesiredWidth(mSubTitleView.getText(), mSubTitleView.getPaint());
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
     * 包裹类
     * 目的是可以中断事件往下传递
     */
    private class ContentView extends LinearLayout {
        private boolean isInterceptEnable;

        public ContentView(Context context) {
            super(context);
        }

        public void setInterceptTouchEventEnable(boolean enable) {
            isInterceptEnable = enable;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return isInterceptEnable;
        }
    }
}