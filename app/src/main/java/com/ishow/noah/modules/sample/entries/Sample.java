package com.ishow.noah.modules.sample.entries;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;

import com.ishow.common.utils.router.AppRouter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by yuhaiyang on 2017/10/12.
 * Sample info
 */

public class Sample {
    public int name;
    public Class action;

    public static Sample newInstance(@StringRes int name, Class action) {
        return new Sample(name, action);
    }

    public Sample(@StringRes int name, Class action) {
        this.name = name;
        this.action = action;
    }

    public String getName(Context context) {
        return context.getString(name);
    }

    public void startAction(Context context) {
        AppRouter.with(context)
                .target(action)
                .start();
    }

    /**
     * 定义图片是单选还是多选
     */
    @IntDef({Cate.Widget})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Cate {
        /**
         * 控件
         */
        int Widget = 1;

    }
}
