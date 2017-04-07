package com.ishow.common.utils.image.loader;

import android.content.Context;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Bright on 2017/4/7.
 * 图片加载的框架
 */

public class ImageLoader {

    /**
     * 居中
     */
    @SuppressWarnings("WeakerAccess")
    public static final int MODE_FIT_CENTER = 1;
    /**
     * 剪切
     */
    @SuppressWarnings("WeakerAccess")
    public static final int MODE_CENTER_CROP = 2;


    /**
     * 方案-普通
     * 普通加载方案
     */
    @SuppressWarnings("WeakerAccess")
    public static final int PLAN_NORMAL = 1;

    @IntDef({MODE_FIT_CENTER, MODE_CENTER_CROP})
    @Retention(RetentionPolicy.SOURCE)
    @interface mode {
    }

    @IntDef({PLAN_NORMAL})
    @Retention(RetentionPolicy.SOURCE)
    @interface plan {
    }


    public static ImageLoaderParams width(Context context) {
        return new ImageLoaderParams(context);
    }


}
