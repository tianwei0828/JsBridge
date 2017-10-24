package com.tw.refreshprogressjsbridge;

import android.webkit.WebView;

import com.tw.refreshprogressjsbridge.utils.CollectionUtil;
import com.tw.refreshprogressjsbridge.utils.StringUtil;

import java.util.Map;

/**
 * Created by wei.tian
 * 2017/10/24
 */

public class ProgressWebViewClient extends BridgeWebViewClient {

    public ProgressWebViewClient(BridgeWebView webView) {
        super(webView);
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Map<String, String> pageHeaders = onPageHeaders(url);
        if (CollectionUtil.notEmpty(pageHeaders)) {
            view.loadUrl(url, pageHeaders);
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        String pageErrorUrl = onPageError(failingUrl);
        if (StringUtil.isNotBlank(pageErrorUrl)) {
            view.loadUrl(pageErrorUrl);
        }
    }

    public String onPageError(String url) {
        return null;
    }

    public Map<String, String> onPageHeaders(String url) {
        return null;
    }
}
