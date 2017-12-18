package com.ishow.noahark.utils.router;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by yuhaiyang on 2017/11/27.
 * App路由配置
 */

public class AppRouter {
    private Context mContext;

    public static void with(Context context) {

    }

    public void target(Uri url) {

    }

    public void target(Class<?> cls) {
        Intent intent = new Intent(mContext, cls);
    }

    public void target(String action) {

    }

    public void target(String action, Class<?> cls) {

    }

    public void target(String scheme, String host) {

    }
}
