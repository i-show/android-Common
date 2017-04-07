package com.ishow.common.utils.image.loader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

/**
 * Created by Bright on 2017/4/7.
 * 图片加载的参数
 */

public class ImageLoaderParams {
    private Context mContext;
    private String mUrl;
    private int mMode;
    private int mPlan;

    private int mPlaceholderRes;
    private Drawable mPlaceholderDrawable;

    private int                     mErrorRes;
    private Drawable mErrorDrawable;

    private IImageLoaderListerner mImageLoaderListener;

    public ImageLoaderParams(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public ImageLoaderParams url(@NonNull String url) {
        return this;
    }

    public ImageLoaderParams mode(@ImageLoader.mode int mode) {
        mMode = mode;
        return this;
    }

    @SuppressWarnings("unused")
    public ImageLoaderParams plan(@ImageLoader.plan int plan) {
        mPlan = plan;
        return this;
    }

    @SuppressWarnings("unused")
    public ImageLoaderParams placeholder(@DrawableRes int placeholderRes) {
        mPlaceholderRes = placeholderRes;
        return this;
    }

    @SuppressWarnings("unused")
    public ImageLoaderParams placeholder(@NonNull Drawable drawable) {
        mPlaceholderDrawable = drawable;
        return this;
    }

    public ImageLoaderParams listener(@NonNull IImageLoaderListerner listerner) {
        mImageLoaderListener = listerner;
        return this;
    }

    public ImageLoaderParams error(@DrawableRes int errorRes) {
        mErrorRes = errorRes;
        return this;
    }

    public ImageLoaderParams error(@NonNull Drawable drawable) {
        mErrorDrawable = drawable;
        return this;
    }
}
