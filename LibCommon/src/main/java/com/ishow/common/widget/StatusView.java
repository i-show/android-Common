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

package com.ishow.common.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishow.common.R;
import com.ishow.common.utils.AnimatorUtils;
import com.bumptech.glide.Glide;

/**
 * 一个状态显示的View
 */
@SuppressWarnings("unused")
public class StatusView extends FrameLayout implements View.OnClickListener {
    /**
     * 正在加载
     */
    public final static int STATUS_LOADING = 1 << 1;
    /**
     * 加载失败
     */
    public final static int STATUS_ERROR = STATUS_LOADING + 1;
    /**
     * 加载为空
     */
    public final static int STATUS_EMPTY = STATUS_LOADING + 2;

    /**
     * 动画时长
     */
    private static final long ANIT_DURATION = 500;


    private static final int HANDLER_DISMISS = 1000;
    private static final int DISMISS_DELAY = 500;

    private View mRoot;
    private ImageView mIconView;
    private TextView mTitle;
    private TextView mSubTitle;
    private TextView mReload;

    private ObjectAnimator mDismissAni;

    private String mLoadingText;
    private String mErrorText;
    private int mErrorImageRes;
    private int mTextColor;
    private int mTextSize;

    private CallBack mCallBack;

    public StatusView(Context context) {
        this(context, null);
    }

    public StatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StatusView);
        mLoadingText = a.getString(R.styleable.StatusView_loadingText);
        mErrorText = a.getString(R.styleable.StatusView_errorText);
        mErrorImageRes = a.getResourceId(R.styleable.StatusView_errorImage, R.drawable.no_picture);
        mTextColor = a.getColor(R.styleable.StatusView_textColor, 0);
        mTextSize = a.getColor(R.styleable.StatusView_textSize, 0);
        a.recycle();

        initView();
    }


    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mRoot = inflater.inflate(R.layout.widget_status_view, this, true);
        mIconView = (ImageView) findViewById(R.id.icon);
        mTitle = (TextView) findViewById(R.id.title);
        mSubTitle = (TextView) findViewById(R.id.subTitle);

        mReload = (TextView) findViewById(R.id.reload);
        mReload.setOnClickListener(this);
    }


    public void showError() {
        Context context = getContext();
        String reload = context.getString(R.string.reload_data);
        String title = context.getString(R.string.load_data_failed);
        String subTitle = context.getString(R.string.load_data_failed_tip);
        showError(reload, title, subTitle, R.drawable.icon_no_server);
    }

    public void showError(@StringRes int title, int icon) {
        Context context = getContext();
        String titleString = context.getString(title);
        showError(null, titleString, null, icon);
    }

    public void showError(@StringRes int title, @StringRes int subTitle, int icon) {
        Context context = getContext();
        String titleString = context.getString(title);
        String subTitleString = context.getString(subTitle);
        showError(null, titleString, subTitleString, icon);
    }

    public void showError(@StringRes int reload, @StringRes int title, @StringRes int subTitle, int icon) {
        Context context = getContext();
        String reloadString = context.getString(reload);
        String titleString = context.getString(title);
        String subTitleString = context.getString(subTitle);
        showError(reloadString, titleString, subTitleString, icon);
    }

    public void showError(String reload, String title, String subTitle, int icon) {
        mTitle.setTextColor(getResources().getColor(R.color.text_grey_normal));
        setText(mReload, reload);
        setText(mTitle, title);
        setText(mSubTitle, subTitle);
        mIconView.setImageResource(icon);
    }

    public void showLoading() {
        showLoading(null);
    }

    public void showLoading(@StringRes int text) {
        String subTitle = getContext().getString(text);
        showLoading(subTitle);
    }

    public void showLoading(String title) {
        setText(mTitle, title);
        mTitle.setTextColor(mSubTitle.getTextColors());
        mSubTitle.setVisibility(GONE);
        mReload.setVisibility(GONE);

        Glide.with(getContext())
                .load(R.drawable.default_loading)
                .asGif()
                .into(mIconView);
    }


    public void showEmpty() {
        showEmpty(R.string.nothing);
    }

    public void showEmpty(@StringRes int text) {
        String title = getContext().getString(text);
        showEmpty(title, R.drawable.icon_no_data);
    }

    public void showEmpty(@StringRes int text, @DrawableRes int icon) {
        String title = getContext().getString(text);
        showEmpty(title, icon);
    }

    public void showEmpty(String title, @DrawableRes int icon) {
        setText(mTitle, title);
        mTitle.setTextColor(mSubTitle.getTextColors());
        mSubTitle.setVisibility(GONE);
        mReload.setVisibility(GONE);

        mIconView.setImageResource(icon);
    }

    public void dismiss() {
        dismiss(true);
    }

    public void dismiss(boolean animation) {
        if (animation) {
            if (getAlpha() == 1F) {
                AnimatorUtils.alpha(mRoot, 1.0F, 0.0F, 800);
            }
        } else {
            setAlpha(0f);
        }
    }

    public void cancelDismiss() {
        setAlpha(1F);
    }


    private void setText(TextView view, String text) {
        if (TextUtils.isEmpty(text)) {
            view.setVisibility(GONE);
        } else {
            view.setVisibility(VISIBLE);
        }
        view.setText(text);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float alpha = getAlpha();
        return alpha != 0;
    }

    @Override
    public void onClick(View v) {
        if (mCallBack != null) {
            mCallBack.onReload();
        }
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public interface CallBack {
        void onReload();
    }
}
