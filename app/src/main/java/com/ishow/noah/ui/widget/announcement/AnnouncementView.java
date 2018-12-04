package com.ishow.noah.ui.widget.announcement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.ishow.common.utils.UnitUtils;
import com.ishow.noah.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnnouncementView extends FrameLayout implements ViewSwitcher.ViewFactory {
    /**
     * 延迟时间
     */
    private static final int DELAY_TIME = 3000;
    private TextSwitcher mTextSwitcher;
    private List<IAnnouncementData> mData;
    private int mCurrentIndex;
    private int mTextColor;
    private int mTextSize;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            updateView();
            mHandler.sendEmptyMessageDelayed(0, DELAY_TIME);
        }
    };

    public AnnouncementView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public AnnouncementView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AnnouncementView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnnouncementView);
        mTextColor = a.getColor(R.styleable.AnnouncementView_textColor, Color.LTGRAY);
        mTextSize = a.getDimensionPixelSize(R.styleable.AnnouncementView_textSize, UnitUtils.dip2px(12));
        a.recycle();

        mData = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.widget_announcement_view, this, true);

        mTextSwitcher = findViewById(R.id.switcher);
        mTextSwitcher.setFactory(this);
        mTextSwitcher.setText(String.valueOf(new Random().nextInt()));
        mTextSwitcher.setInAnimation(inAnimation());
        mTextSwitcher.setOutAnimation(outAnimation());
    }


    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) {
            mHandler.sendEmptyMessageDelayed(0, DELAY_TIME);
        } else {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public View makeView() {
        TextView textView = new TextView(getContext());
        textView.setTextSize(mTextSize);
        textView.setTextColor(mTextColor);
        return textView;
    }

    public void setData(List<IAnnouncementData> data) {
        if (data == null) {
            mData.clear();
        } else {
            mData = data;
        }
        mCurrentIndex = 0;
        updateView();
    }


    private void updateView() {
        String text = mData.get(mCurrentIndex).getTitle();
        mTextSwitcher.setText(text);
        if (mCurrentIndex >= mData.size() - 1) {
            mCurrentIndex = 0;
        } else {
            mCurrentIndex++;
        }
    }

    /**
     * 定义从右侧进入的动画效果
     */
    private Animation inAnimation() {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    /**
     * 定义从左侧退出的动画效果
     */
    private Animation outAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f);
        outtoLeft.setDuration(500);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }


}
