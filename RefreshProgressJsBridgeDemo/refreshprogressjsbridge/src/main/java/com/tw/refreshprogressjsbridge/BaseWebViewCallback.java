package com.tw.refreshprogressjsbridge;

import android.webkit.WebView;

/**
 * Created by wei.tian
 * 2017/9/27
 */

public abstract class BaseWebViewCallback implements WebViewCallback {
    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        return false;
    }

    @Override
    public void onReceivedTitle(WebView webView, String title) {

    }

    @Override
    public void onLoadBegin(WebView webView, String url) {

    }

    @Override
    public void onLoading(WebView webView, int progress) {

    }

    @Override
    public void onLoadEnd(WebView webView, String url) {

    }

    @Override
    public boolean onKeyBack() {
        return true;
    }
}
