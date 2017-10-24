package com.tw.refreshprogressjsbridge.internal;


import android.webkit.WebSettings;

import com.tw.refreshprogressjsbridge.BridgeWebView;
import com.tw.refreshprogressjsbridge.JavaHandler;
import com.tw.refreshprogressjsbridge.JsHandler;
import com.tw.refreshprogressjsbridge.ScrollChangeListener;
import com.tw.refreshprogressjsbridge.WebViewCallback;

import java.util.List;
import java.util.Map;

/**
 * Created by wei.tian
 * 2017/9/23
 */

public interface IProgressWebView {

    NumberProgressBar getProgressBar();

    void setCookies(String url, Map<String, String> cookies);

    String getCookies(String url);

    void removeAllCookies();

    void registerJsHandler(String handlerName, JsHandler jsHandler);

    void registerJsHandler(List<String> handlersName, JsHandler jsHandler);

    void registerJavaHandler(String handlerName, String javaData, JavaHandler javaHandler);

    void registerJavaHandler(Map<String, String> handlerInfos, JavaHandler javaHandler);
}
