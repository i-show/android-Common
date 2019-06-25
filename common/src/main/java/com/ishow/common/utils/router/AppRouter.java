package com.ishow.common.utils.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.AnimRes;
import androidx.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;
import com.ishow.common.utils.cache.LRUCache;
import com.ishow.common.utils.log.LogUtils;

import java.io.Serializable;

/**
 * Created by yuhaiyang on 2017/12/12.
 * App 路由
 */

public class AppRouter {
    private static final String TAG = "AppRouter";
    /**
     * 路由缓存时间设置
     */
    private static final int MAX_ROUTER_CACHE_SIZE = 20;
    /**
     * 非法的标记位
     */
    public static final int INVALID_FLAGS = -512;
    /**
     * 路由跳转的间隔时间
     */
    private static final int S_INTERVAL_OF_ROUTE_TIME = 300;

    private Context mContext;
    private String mAction;
    private String mUrl;
    private String mScheme;
    private String mHost;
    private String mPackageName;
    private Class<?> mClass;

    private Bundle mParams;
    private int mRequestCode;
    private int mFlag;
    private int[] mAnimation;
    private boolean isFinish;

    private static AbsRouterConfigure config;
    /**
     * 路由时间判定
     */
    private static LRUCache<String, Long> sRouteTime = new LRUCache<>(MAX_ROUTER_CACHE_SIZE);

    private AppRouter() {
        mFlag = INVALID_FLAGS;
        mRequestCode = INVALID_FLAGS;
        mAnimation = new int[2];
    }


    public static void setConfigure(AbsRouterConfigure custom) {
        config = custom;
    }

    public static AppRouter with(Context context) {
        AppRouter router = new AppRouter();
        router.mContext = context;
        router.mPackageName = context.getPackageName();
        return router;
    }


    public AppRouter action(String action) {
        mAction = action;
        return this;
    }

    public AppRouter url(Uri uri) {
        if (uri != null) {
            mUrl = uri.toString();
        }
        return this;
    }

    public AppRouter url(String url) {
        mUrl = url;
        return this;
    }

    @SuppressWarnings("unused")
    public AppRouter scheme(String scheme) {
        mScheme = scheme;
        return this;
    }

    @SuppressWarnings({"UnusedReturnValue"})
    public AppRouter scheme(@StringRes int scheme) {
        mScheme = mContext.getString(scheme);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRouter host(String host) {
        mHost = host;
        return this;
    }

    @SuppressWarnings("unused")
    public AppRouter host(@StringRes int host) {
        mHost = mContext.getString(host);
        return this;
    }

    @SuppressWarnings("unused")
    public AppRouter target(Class<?> cls) {
        mClass = cls;
        return this;
    }

    @SuppressWarnings({"UnusedReturnValue"})
    public AppRouter flag(int flag) {
        mFlag = flag;
        return this;
    }

    public AppRouter requestCode(int code) {
        mRequestCode = code;
        return this;
    }

    /**
     * 设置包名
     */
    @SuppressWarnings("unused")
    public AppRouter packageName(String packageName) {
        mPackageName = packageName;
        return this;
    }

    public AppRouter params(Bundle bundle) {

        if (mParams == null) {
            mParams = bundle;
        } else {
            mParams.putAll(bundle);
        }
        return this;
    }

    public AppRouter addParam(String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            Log.i(TAG, "addParam String error ");
            return this;
        }

        if (mParams == null) {
            mParams = new Bundle();
        }
        mParams.putString(key, value);
        return this;
    }

    public AppRouter addParam(String key, Serializable value) {
        if (TextUtils.isEmpty(key) || value == null) {
            Log.i(TAG, "addParam Serializable : params is error ");
            return this;
        }

        if (mParams == null) {
            mParams = new Bundle();
        }
        mParams.putSerializable(key, value);
        return this;
    }

    public AppRouter addParam(String key, int value) {

        if (TextUtils.isEmpty(key)) {
            Log.i(TAG, "addParma: ");
            return this;
        }

        if (mParams == null) {
            mParams = new Bundle();
        }
        mParams.putInt(key, value);
        return this;
    }

    /**
     * 补间动画
     */
    @SuppressWarnings("unused")
    public AppRouter overridePendingTransition(@AnimRes int enterAnim, @AnimRes int exitAnim) {
        mAnimation[0] = enterAnim;
        mAnimation[1] = exitAnim;
        return this;
    }

    /**
     * 是否关闭上一个应用
     */
    public AppRouter finishSelf() {
        isFinish = true;
        return this;
    }


    /**
     * 返回是否跳转成功
     */
    public boolean start() {
        if (mContext == null) {
            return false;
        }


        if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
            Log.i(TAG, "start: activity is finishing");
            return false;
        }

        if (config != null) {
            config.config(this);
        }

        StringBuilder routerKey = new StringBuilder();
        // 跳转目标
        boolean isNoTarget = true;
        Intent intent;
        if (mClass != null) {
            intent = new Intent(mContext, mClass);
            routerKey.append(mClass.getName());
            isNoTarget = false;
        } else if (!TextUtils.isEmpty(mAction)) {
            intent = new Intent(mAction);
            routerKey.append(mAction);
            isNoTarget = false;
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            routerKey.append(Intent.ACTION_VIEW);
        }

        Uri uri = makeUri();
        if (uri != null) {
            intent.setData(uri);
            routerKey.append(uri.toString());
            isNoTarget = false;
        }

        if (isNoTarget) {
            LogUtils.e(TAG, "no target activity");
            return false;
        }

        if (mFlag != INVALID_FLAGS) {
            intent.setFlags(mFlag);
        }

        if (mParams != null) {
            intent.putExtras(mParams);
        }

        // Android 8.0 需要增加packageName设置否则会出现找不到引用提示
        if (!TextUtils.isEmpty(mPackageName)) {
            intent.setPackage(mPackageName);
        }

        if (isFastRouter(routerKey.toString())) {
            return false;
        }

        try {
            if (mRequestCode != INVALID_FLAGS && mContext instanceof Activity) {
                Activity activity = (Activity) mContext;
                activity.startActivityForResult(intent, mRequestCode);
            } else {
                mContext.startActivity(intent);
            }

            if (mContext instanceof Activity && (mAnimation[0] > 0 || mAnimation[1] > 0)) {
                ((Activity) mContext).overridePendingTransition(mAnimation[0], mAnimation[1]);
            }

            if (isFinish && mContext instanceof Activity) {
                ((Activity) mContext).finish();
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private Uri makeUri() {
        if (!TextUtils.isEmpty(mUrl)) {
            return Uri.parse(mUrl);
        }

        if (!TextUtils.isEmpty(mScheme) && !TextUtils.isEmpty(mHost)) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(mScheme);
            builder.authority(mHost);
            return builder.build();
        }
        return null;
    }

    /**
     * 是否是迅速的2次跳转
     */
    private boolean isFastRouter(String key) {
        final long nowTime = System.currentTimeMillis();
        final long lastTime = getLastRouteTime(key);
        if (nowTime - lastTime < S_INTERVAL_OF_ROUTE_TIME) {
            Log.i(TAG, "start: The interval between activities is too short");
            return true;
        }
        sRouteTime.put(key, nowTime);
        return false;
    }

    private long getLastRouteTime(String key) {
        if (sRouteTime == null) {
            return 0L;
        }

        if (!sRouteTime.containsKey(key)) {
            return 0L;
        }
        return sRouteTime.get(key);
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

    public String getHost() {
        return mHost;
    }

    public int getFlag() {
        return mFlag;
    }
}
