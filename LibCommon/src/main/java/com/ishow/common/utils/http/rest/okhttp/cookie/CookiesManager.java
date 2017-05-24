package com.ishow.common.utils.http.rest.okhttp.cookie;

import android.content.Context;
import android.text.LoginFilter;

import com.ishow.common.utils.log.L;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 自动管理Cookies
 */
public class CookiesManager implements CookieJar {
    private static CookiesManager sInstance;
    private final PersistentCookieStore mCookieStore;

    private CookiesManager(Context context) {
        mCookieStore = new PersistentCookieStore(context.getApplicationContext());
    }

    public static CookiesManager init(Context context) {
        if (sInstance == null) {
            synchronized (CookiesManager.class) {
                if (sInstance == null) {
                    sInstance = new CookiesManager(context);
                }
            }
        }
        return sInstance;
    }

    public static CookiesManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (CookiesManager.class) {
                if (sInstance == null) {
                    sInstance = new CookiesManager(context);
                }
            }
        }
        return sInstance;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                mCookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return mCookieStore.get(url);
    }


    public void clearCookie(){
        mCookieStore.clearCookie();
    }
}