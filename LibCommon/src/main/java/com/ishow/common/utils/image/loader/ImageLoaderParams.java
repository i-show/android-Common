package com.ishow.common.utils.image.loader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

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

    private int mErrorRes;
    private Drawable mErrorDrawable;

    private IImageLoaderListerner mImageLoaderListener;

    ImageLoaderParams(Context context) {
        mContext = context;
        mMode = ImageLoader.MODE_CENTER_CROP;
        mPlan = ImageLoader.PLAN_NORMAL;
    }

    public Context getContext() {
        return mContext;
    }

    public ImageLoaderParams url(@NonNull String url) {
        mUrl = url;
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

    @SuppressWarnings("unused")
    public ImageLoaderParams listener(@NonNull IImageLoaderListerner listerner) {
        mImageLoaderListener = listerner;
        return this;
    }

    @SuppressWarnings("unused")
    public ImageLoaderParams error(@DrawableRes int errorRes) {
        mErrorRes = errorRes;
        return this;
    }

    @SuppressWarnings("unused")
    public ImageLoaderParams error(@NonNull Drawable drawable) {
        mErrorDrawable = drawable;
        return this;
    }

    public ImageLoaderParams into(@NonNull ImageView view) {
        ImageLoader.getExecutor().display(this, view);
        return this;
    }


    public String getUrl() {
        return mUrl;
    }

    /**
     * 获取当前的Mode
     */
    public
    @ImageLoader.mode
    int getMode() {
        return mMode;
    }

    /**
     * 获取档期那的加载类型
     */
    public
    @ImageLoader.plan
    int getPlan() {
        return mPlan;
    }

    public int getPlaceholderRes() {
        return mPlaceholderRes;
    }

    public Drawable getPlaceholderDrawable() {
        return mPlaceholderDrawable;
    }

    public int getErrorRes() {
        return mErrorRes;
    }

    public Drawable getErrorDrawable() {
        return mErrorDrawable;
    }

    public IImageLoaderListerner getImageLoaderListener() {
        return mImageLoaderListener;
    }

    /**
     * 检测参数是否合法
     */
    public void checkValid() {
        if (getContext() == null) {
            throw new IllegalArgumentException(" need a context");
        }

        if (getUrl() == null) {
            throw new IllegalArgumentException(" need a url");
        }
    }
}
