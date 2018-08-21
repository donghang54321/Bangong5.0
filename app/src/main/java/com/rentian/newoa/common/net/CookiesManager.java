package com.rentian.newoa.common.net;

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by rentianjituan on 2016/12/13.
 */

public class CookiesManager implements CookieJar {
    private Context context;
    private final PersistentCookieStore cookieStore = new PersistentCookieStore(context);
    public CookiesManager(Context context){
        this.context=context;
    }
    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);

            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }

}
