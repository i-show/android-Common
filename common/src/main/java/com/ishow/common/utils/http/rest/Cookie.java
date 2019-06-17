package com.ishow.common.utils.http.rest;

import androidx.annotation.IntDef;
import androidx.annotation.Keep;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by yuhaiyang on 2017/5/27.
 * Cookie
 */
@Keep
public class Cookie {

    @SuppressWarnings("WeakerAccess")
    @IntDef({Type.NONE, Type.MEMORY, Type.FILE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
        /**
         * 没有cookie
         */
        int NONE = 0;
        /**
         * cookie保存在内存，退出后移除
         */
        int MEMORY = 1;
        /**
         * cookie保存在文件中，退出后移除
         * 超长持久化
         */
        int FILE = 2;
    }
}
