package com.tw.refreshprogressjsbridge;

import android.webkit.WebView;

/**
 * Created by wei.tian
 * 2017/9/23
 */

public interface WebViewCallback {
    boolean shouldOverrideUrlLoading(WebView webView, String url);

    void onReceivedTitle(WebView webView, String title);

    void onLoadBegin(WebView webView, String url);

    void onLoading(WebView webView, int progress);

    void onLoadEnd(WebView webView, String url);

    void onLoadError(WebView webView, int errorCode, String description, String failingUrl);

    boolean onKeyBack();
}
