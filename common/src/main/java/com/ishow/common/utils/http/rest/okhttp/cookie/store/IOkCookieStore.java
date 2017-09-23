package com.ishow.common.utils.http.rest.okhttp.cookie.store;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public interface IOkCookieStore {

    void add(HttpUrl url, List<Cookie> cookie);

    List<Cookie> get(HttpUrl url);

    List<Cookie> getCookies();

    boolean remove(HttpUrl url, Cookie cookie);

    boolean removeAll();
}
