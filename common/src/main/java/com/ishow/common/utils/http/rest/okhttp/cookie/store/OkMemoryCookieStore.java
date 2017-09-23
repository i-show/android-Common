package com.ishow.common.utils.http.rest.okhttp.cookie.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.HttpUrl;


public class OkMemoryCookieStore implements IOkCookieStore {

    private final HashMap<String, List<Cookie>> mCookies = new HashMap<>();

    @Override
    public void add(HttpUrl url, List<Cookie> cookies) {
        List<Cookie> oldCookies = mCookies.get(url.host());

        if (oldCookies != null) {
            Iterator<Cookie> itNew = cookies.iterator();
            Iterator<Cookie> itOld = oldCookies.iterator();
            while (itNew.hasNext()) {
                String va = itNew.next().name();
                while (va != null && itOld.hasNext()) {
                    String v = itOld.next().name();
                    if (v != null && va.equals(v)) {
                        itOld.remove();
                    }
                }
            }
            oldCookies.addAll(cookies);
        } else {
            mCookies.put(url.host(), cookies);
        }


    }

    @Override
    public List<Cookie> get(HttpUrl uri) {
        List<Cookie> cookies = mCookies.get(uri.host());
        if (cookies == null) {
            cookies = new ArrayList<>();
            mCookies.put(uri.host(), cookies);
        }
        return cookies;

    }

    @Override
    public boolean removeAll() {
        mCookies.clear();
        return true;
    }

    @Override
    public List<Cookie> getCookies() {
        List<Cookie> cookies = new ArrayList<>();
        Set<String> httpUrls = mCookies.keySet();
        for (String url : httpUrls) {
            cookies.addAll(mCookies.get(url));
        }
        return cookies;
    }


    @Override
    public boolean remove(HttpUrl uri, Cookie cookie) {
        List<Cookie> cookies = mCookies.get(uri.host());
        return cookie != null && cookies.remove(cookie);
    }


}
