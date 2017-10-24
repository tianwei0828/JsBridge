package com.tw.refreshprogressjsbridge.internal;

import com.tw.refreshprogressjsbridge.ProgressWebView;
import com.tw.refreshprogressjsbridge.RefreshCallback;
import com.tw.refreshprogressjsbridge.ScrollChangeListener;
import com.tw.refreshprogressjsbridge.WebViewCallback;

import java.util.Map;

/**
 * Created by wei.tian
 * 2017/9/23
 */

public interface IRefreshProgressWebView extends IProgressWebView {
    ProgressWebView getWebView();

    void setScrollChangeListener(ScrollChangeListener scrollChangeListener);

    void setWebViewCallback(WebViewCallback webViewCallback);

    WebViewCallback getWebViewCallback();

    void setRefreshCallback(RefreshCallback refreshCallback);

    RefreshCallback getRefreshCallback();

    void setHeader(String header, String value);

    String getHeader(String key);

    void setHeaders(Map<String, String> headers);

    Map<String, String> getHeaders();

    void setErrorPageUrl(String url);

    String getErrorPageUrl();

    void load(String url);

    void destroy();
}
