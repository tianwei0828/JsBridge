package com.tw.refreshprogressjsbridge.utils;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;


import com.tw.refreshprogressjsbridge.logger.Logger;

import java.util.Map;

/**
 * Created by wei.tian
 * 2017/9/20
 */

public final class CookieUtil {

    private CookieUtil() {
        throw new IllegalStateException("No instance!");
    }


    public static void synCookies(Context context, final String url, final Map<String, String> cookies) {
        Logger.d("synCookies-- url: " + url + " -- cookies: " + cookies);
        if (CollectionUtil.isEmpty(cookies)) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context);
        }
        CookieManager.getInstance().setAcceptCookie(true);
        syn(url, cookies);
        String getCookie = CookieManager.getInstance().getCookie(url);
        Logger.d("getCookie: " + getCookie);
    }


    private static void syn(String url, Map<String, String> cookies) {
        Logger.d("syn: " + url);
        for (Map.Entry<String, String> me : cookies.entrySet()) {
            String key = me.getKey();
            String value = me.getValue();
            Logger.d("syn key: " + key + "     value: " + value);
            if (StringUtil.isBlank(key) /*|| StringUtil.isBlank(value)*/) {
                continue;
            }
            CookieManager.getInstance().setCookie(url, key + "=" + value);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager.getInstance().sync();
        }
    }

    public static String getCookies(String url) {
        return CookieManager.getInstance().getCookie(url);
    }

    public static void removeAllCookies() {
        Logger.d("removeAllCookies");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeSessionCookies(null);
            CookieManager.getInstance().removeAllCookies(null);
        } else {
            CookieManager.getInstance().removeSessionCookie();
            CookieManager.getInstance().removeAllCookie();
        }
    }
}
