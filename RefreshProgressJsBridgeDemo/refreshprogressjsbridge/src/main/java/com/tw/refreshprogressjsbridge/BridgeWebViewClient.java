package com.tw.refreshprogressjsbridge;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tw.refreshprogressjsbridge.internal.BridgeUtil;
import com.tw.refreshprogressjsbridge.internal.Message;
import com.tw.refreshprogressjsbridge.logger.Logger;
import com.tw.refreshprogressjsbridge.utils.ObjectUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by bruce on 10/28/15.
 */
public class BridgeWebViewClient extends WebViewClient {
    private BridgeWebView webView;

    public BridgeWebViewClient(BridgeWebView webView) {
        ObjectUtil.checkNonNull(webView, "webView == null");
        this.webView = webView;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Logger.d("old shouldOverrideUrlLoading: " + url);
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
            webView.handlerReturnData(url);
            return true;
        } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
            webView.flushMessageQueue();
            return true;
        } else {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        if (BridgeWebView.toLoadJs != null) {
            Logger.d("start: " + System.currentTimeMillis());
            BridgeUtil.webViewLoadLocalJs(view, BridgeWebView.toLoadJs);
        }

        //
        if (webView.getStartupMessage() != null) {
            for (Message m : webView.getStartupMessage()) {
                webView.dispatchMessage(m);
            }
            webView.setStartupMessage(null);
        }
        Logger.d("end: " + System.currentTimeMillis());
    }
}