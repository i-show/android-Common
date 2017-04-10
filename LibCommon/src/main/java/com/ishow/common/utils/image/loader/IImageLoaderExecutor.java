package com.ishow.common.utils.image.loader;

import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * Created by Bright on 2017/4/7.
 * 执行
 */
@SuppressWarnings("WeakerAccess,unused")
public interface IImageLoaderExecutor {
    /**
     * Display 用来显示图片
     */
    void display(@NonNull ImageLoaderParams params, @NonNull ImageView view);
}
