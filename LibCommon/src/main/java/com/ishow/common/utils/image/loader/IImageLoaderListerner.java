package com.ishow.common.utils.image.loader;

import android.graphics.drawable.Drawable;

/**
 * Created by Bright on 2017/4/7.
 * Loader的监听
 */
@SuppressWarnings("WeakerAccess")
public interface IImageLoaderListerner {

    void onFailed();

    void onSuccess(Drawable drawable);
}
