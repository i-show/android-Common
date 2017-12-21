package com.ishow.noahark.utils.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;

import java.io.Serializable;
import java.lang.ref.WeakReference;

/**
 * Created by yuhaiyang on 2017/12/12.
 * App 路由
 */

public class AppRouter {
    private static final String TAG = "AppRouter";
    /**
     * 非法的标记位
     */
    @SuppressWarnings("WeakerAccess")
    public static final int INVALID_FLAGS = -512;
    /**
     * 上次路由时间
     */
    private static long mLastRouteTime;
    private Context mContext;
    private String mAction;
    private String mUrl;
    private String mScheme;
    private String mHost;
    private Class<?> mClass;

    private Bundle mParams;
    private int mRequestCode;
    private int mFlag;
    private boolean isFinish;

    private static WeakReference<AppRouter> mRouter;

    private AppRouter() {
        mFlag = INVALID_FLAGS;
        mRequestCode = INVALID_FLAGS;
    }

    public static AppRouter with(Context context) {
        AppRouter router = new AppRouter();
        router.mContext = context;
        mRouter = new WeakReference<>(router);
        return router;
    }


    public AppRouter action(String action) {
        AppRouter router = getRouter();
        if (router == null) {
            return null;
        }
        router.mAction = action;
        return router;
    }

    public AppRouter url(Uri uri) {
        AppRouter router = getRouter();
        if (router == null) {
            return null;
        }
        if (uri != null) {
            router.mUrl = uri.toString();
        }
        return router;
    }

    public AppRouter url(String url) {
        AppRouter router = getRouter();
        if (router == null) {
            return null;
        }
        router.mUrl = url;
        return router;
    }

    public AppRouter scheme(String scheme) {
        AppRouter router = getRouter();
        if (router == null) {
            return null;
        }
        router.mScheme = scheme;
        return router;
    }

    public AppRouter scheme(@StringRes int scheme) {
        AppRouter router = getRouter();
        if (router == null) {
            return null;
        }
        router.mScheme = router.mContext.getString(scheme);
        return router;
    }

    @SuppressWarnings("unused")
    public AppRouter host(String host) {
        AppRouter router = getRouter();
        if (router == null) {
            return null;
        }
        router.mHost = host;
        return router;
    }

    @SuppressWarnings("unused")
    public AppRouter host(@StringRes int host) {
        AppRouter router = getRouter();
        if (router == null) {
            return null;
        }
        router.mHost = router.mContext.getString(host);
        return router;
    }

    @SuppressWarnings("unused")
    public AppRouter target(Class<?> cls) {
        AppRouter router = getRouter();
        if (router == null) {
            return null;
        }
        router.mClass = cls;
        return router;
    }

    @SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
    public AppRouter flag(int flag) {
        AppRouter router = getRouter();
        if (router == null) {
            return null;
        }
        router.mFlag = flag;
        return router;
    }

    @SuppressWarnings("unused")
    public AppRouter requestCode(int code) {
        AppRouter router = getRouter();
        if (router == null) {
            return null;
        }
        router.mRequestCode = code;
        return router;
    }

    public AppRouter params(Bundle bundle) {
        AppRouter router = getRouter();
        if (router == null) {
            return null;
        }
        if (router.mParams == null) {
            router.mParams = bundle;
        } else {
            router.mParams.putAll(bundle);
        }

        return router;
    }

    @SuppressWarnings("WeakerAccess")
    public AppRouter addParam(String key, String value) {
        AppRouter router = getRouter();
        if (router == null) {
            return null;
        }

        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            Log.i(TAG, "addParam String error ");
            return router;
        }

        if (router.mParams == null) {
            router.mParams = new Bundle();
        }
        router.mParams.putString(key, value);
        return router;
    }

    @SuppressWarnings("WeakerAccess")
    public AppRouter addParam(String key, Serializable value) {
        AppRouter router = getRouter();
        if (router == null) {
            return null;
        }

        if (TextUtils.isEmpty(key) || value == null) {
            Log.i(TAG, "addParam Serializable : params is error ");
            return router;
        }

        if (router.mParams == null) {
            router.mParams = new Bundle();
        }
        router.mParams.putSerializable(key, value);
        return router;
    }

    @SuppressWarnings("unused")
    public AppRouter addParam(String key, int value) {
        AppRouter router = getRouter();
        if (router == null) {
            return null;
        }
        if (TextUtils.isEmpty(key)) {
            Log.i(TAG, "addParma: ");
            return router;
        }

        if (router.mParams == null) {
            router.mParams = new Bundle();
        }
        router.mParams.putInt(key, value);
        return router;
    }

    /**
     * 是否关闭上一个应用
     */
    public AppRouter finishSelf() {
        AppRouter router = getRouter();
        if (router == null) {
            return null;
        }

        router.isFinish = true;
        return router;
    }


    /**
     * 返回是否跳转成功
     */
    public boolean start() {
        final long nowTime = System.currentTimeMillis();
        if (nowTime - mLastRouteTime < 800) {
            return false;
        }
        mLastRouteTime = nowTime;

        AppRouter router = getRouter();
        if (router == null || router.mContext == null) {
            return false;
        }

        if (router.mContext instanceof Activity && ((Activity) router.mContext).isFinishing()) {
            Log.i(TAG, "start: activity is finishing");
            return false;
        }

        AppRouterConfigure.config(router);

        Intent intent;
        if (router.mClass != null) {
            intent = new Intent(mContext, router.mClass);
        } else if (!TextUtils.isEmpty(router.mAction)) {
            intent = new Intent(router.mAction);
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
        }

        Uri uri = makrUri(router);
        if (uri != null) {
            intent.setData(uri);
        }

        if (router.mFlag != INVALID_FLAGS) {
            intent.setFlags(router.mFlag);
        }

        if (router.mParams != null) {
            intent.putExtras(router.mParams);
        }


        try {
            if (router.mRequestCode != INVALID_FLAGS && router.mContext instanceof Activity) {
                Activity activity = (Activity) router.mContext;
                activity.startActivityForResult(intent, router.mRequestCode);
            } else {
                router.mContext.startActivity(intent);
            }

            if (router.isFinish && router.mContext instanceof Activity) {
                ((Activity) router.mContext).finish();
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private AppRouter getRouter() {
        if (mRouter == null) {
            return null;
        }
        return mRouter.get();
    }


    private Uri makrUri(AppRouter router) {
        if (!TextUtils.isEmpty(router.mUrl)) {
            return Uri.parse(router.mUrl);
        }

        if (!TextUtils.isEmpty(router.mScheme) && !TextUtils.isEmpty(router.mHost)) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(router.mScheme);
            builder.authority(router.mHost);
            return builder.build();
        }
        return null;
    }


    public Context getContext() {
        return mContext;
    }

    public String getScheme() {
        return mScheme;
    }

    public String getUrl() {
        return mUrl;
    }

    @SuppressWarnings("WeakerAccess")
    public String getHost() {
        return mHost;
    }

    @SuppressWarnings("WeakerAccess")
    public int getFlag() {
        return mFlag;
    }
}
