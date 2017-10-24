package com.tw.refreshprogressjsbridge;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.qx.wz.jsbridge.R;
import com.tw.refreshprogressjsbridge.internal.BridgeUtil;
import com.tw.refreshprogressjsbridge.internal.IRefreshProgressWebView;
import com.tw.refreshprogressjsbridge.internal.NumberProgressBar;
import com.tw.refreshprogressjsbridge.logger.Logger;
import com.tw.refreshprogressjsbridge.utils.CollectionUtil;
import com.tw.refreshprogressjsbridge.utils.CookieUtil;
import com.tw.refreshprogressjsbridge.utils.ObjectUtil;
import com.tw.refreshprogressjsbridge.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by wei.tian
 * 2017/9/24
 */

public class RefreshProgressWebView extends SwipeRefreshLayout implements IRefreshProgressWebView {
    private ProgressWebView mProView;
    private ScrollChangeListener mScrollChangeListener;
    private WebViewCallback mWebViewCallback;
    private RefreshCallback mRefreshCallback;
    private String mUrl;
    private String mCookieDomain;
    private String mErrorPageUrl;
    private final Map<String, String> mHeaders = new HashMap<>();

    public RefreshProgressWebView(Context context) {
        super(context);
        init(context, null);
    }

    public RefreshProgressWebView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(final Context context, AttributeSet attrs) {
        initProgressWebView(context, attrs);
        this.setOnRefreshListener(new InnerOnRefreshListener());
        this.setEnabled(true);
        this.setRefreshing(false);
        this.setColorSchemeResources(R.color.refresh_cycle);
        addView(mProView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void initProgressWebView(Context context, AttributeSet attrs) {
        mProView = new ProgressWebView(context, attrs);
        mProView.setWebViewClient(new InnerWebViewClient());
        mProView.setWebChromeClient(new InnerWebChromeClient(mProView.getProgressBar()));
        mProView.setOnScrollChangeListener(new InnerScrollChangeListener());
        mProView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (RefreshProgressWebView.this.isRefreshing()) {
                            RefreshProgressWebView.this.setRefreshing(false);
                            RefreshProgressWebView.this.setEnabled(true);
                            if (ObjectUtil.nonNull(mRefreshCallback)) {
                                mRefreshCallback.onRefreshEnd();
                            }
                        }
                        if (ObjectUtil.isNull(mWebViewCallback)) {
                            goBack();
                            return true;
                        }
                        if (mWebViewCallback.onKeyBack()) {
                            goBack();
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    private void goBack() {
        if (ObjectUtil.isNull(mProView)) {
            return;
        }
        if (mProView.canGoBack()) {
            mProView.goBack();
        }
    }


    @Override
    public NumberProgressBar getProgressBar() {
        if (ObjectUtil.isNull(mProView)) {
            return null;
        }
        return mProView.getProgressBar();
    }

    @Override
    public void setCookies(String url, Map<String, String> cookies) {
        if (ObjectUtil.nonNull(mProView)) {
            mCookieDomain = url;
            mProView.setCookies(url, cookies);
        }
    }

    @Override
    public String getCookies(String url) {
        if (ObjectUtil.nonNull(mProView)) {
            return mProView.getCookies(url);
        }
        return null;
    }

    @Override
    public void removeAllCookies() {
        Logger.d("removeAllCookies");
        if (ObjectUtil.nonNull(mProView)) {
            mProView.removeAllCookies();
        }
    }


    @Override
    public void registerJsHandler(String handlerName, JsHandler jsHandler) {
        if (ObjectUtil.nonNull(mProView)) {
            mProView.registerJsHandler(handlerName, jsHandler);
        }
    }

    @Override
    public void registerJsHandler(List<String> handlersName, JsHandler jsHandler) {
        if (ObjectUtil.nonNull(mProView)) {
            mProView.registerJsHandler(handlersName, jsHandler);
        }
    }

    @Override
    public void registerJavaHandler(String handlerName, String javaData, JavaHandler javaHandler) {
        if (ObjectUtil.nonNull(mProView)) {
            mProView.registerJavaHandler(handlerName, javaData, javaHandler);
        }
    }

    @Override
    public void registerJavaHandler(Map<String, String> handlerInfos, JavaHandler javaHandler) {
        if (ObjectUtil.nonNull(mProView)) {
            mProView.registerJavaHandler(handlerInfos, javaHandler);
        }
    }


    private void updateUrl(String url) {
        if (StringUtil.isBlank(url)) {
            return;
        }
        if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) {
            return;
        }
        Logger.d("updateUrl: " + url);
        mUrl = url;
    }

    @Override
    public void destroy() {
        if (ObjectUtil.nonNull(mProView)) {
            mProView.stopLoading();
            mProView.destroy();
            mProView = null;
        }
        this.setRefreshing(false);
        mCookieDomain = null;
        mUrl = null;
    }


    @Override
    public ProgressWebView getWebView() {
        return mProView;
    }

    @Override
    public void setScrollChangeListener(ScrollChangeListener scrollChangeListener) {
        mScrollChangeListener = ObjectUtil.requireNonNull(scrollChangeListener, "scrollChangeListener == null");
    }

    @Override
    public void setWebViewCallback(WebViewCallback webViewCallback) {
        mWebViewCallback = ObjectUtil.requireNonNull(webViewCallback, "webViewCallback == null");
    }

    @Override
    public WebViewCallback getWebViewCallback() {
        return mWebViewCallback;
    }

    @Override
    public void setRefreshCallback(RefreshCallback refreshCallback) {
        mRefreshCallback = ObjectUtil.requireNonNull(refreshCallback, "refreshCallback == null");
    }

    @Override
    public RefreshCallback getRefreshCallback() {
        return mRefreshCallback;
    }

    @Override
    public void setHeader(String header, String value) {
        StringUtil.checkNotBlank(header, "header is blank");
        StringUtil.checkNotBlank(value, "value is blank");
        mHeaders.put(header, value);
    }

    @Override
    public String getHeader(String key) {
        StringUtil.checkNotBlank(key, "key is blank");
        return mHeaders.get(key);
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        CollectionUtil.checkNotEmpty(headers, "headers are empty");
        mHeaders.putAll(headers);
    }

    @Override
    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    @Override
    public void setErrorPageUrl(String url) {
        mErrorPageUrl = url;
    }

    @Override
    public String getErrorPageUrl() {
        return mErrorPageUrl;
    }

    @Override
    public void load(String url) {
        if (ObjectUtil.nonNull(mProView)) {
            getWebView().loadUrl(url);
        }
    }

    private class InnerWebChromeClient extends ProgressWebChromeClient {

        public InnerWebChromeClient(NumberProgressBar progressBar) {
            super(progressBar);
        }

        @Override
        public void onReceivedTitle(WebView webView, String title) {
            Logger.d("onReceivedTitle: " + title);
            if (ObjectUtil.nonNull(mWebViewCallback)) {
                mWebViewCallback.onReceivedTitle(webView, title);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (ObjectUtil.nonNull(mWebViewCallback)) {
                mWebViewCallback.onLoading(view, newProgress);
            }
        }
    }


    private class InnerWebViewClient extends ProgressWebViewClient {
        public InnerWebViewClient() {
            this(mProView);
        }

        public InnerWebViewClient(BridgeWebView webView) {
            super(webView);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            updateUrl(url);
            RefreshProgressWebView.this.setEnabled(false);
            if (ObjectUtil.nonNull(mWebViewCallback)) {
                mWebViewCallback.onLoadBegin(view, url);
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            boolean result = super.shouldOverrideUrlLoading(webView, url);
            if (ObjectUtil.nonNull(mWebViewCallback)) {
                result = mWebViewCallback.shouldOverrideUrlLoading(webView, url);
            }
            Logger.d("shouldOverrideUrlLoading: " + url + " -- result: " + result);
            return result;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Logger.d("onPageFinished: " + url);
            RefreshProgressWebView.this.setRefreshing(false);
            RefreshProgressWebView.this.setEnabled(true);
            if (ObjectUtil.nonNull(mRefreshCallback)) {
                mRefreshCallback.onRefreshEnd();
            }
            if (ObjectUtil.nonNull(mWebViewCallback)) {
                mWebViewCallback.onLoadEnd(view, url);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            RefreshProgressWebView.this.setRefreshing(false);
            RefreshProgressWebView.this.setEnabled(true);
            if (ObjectUtil.nonNull(mRefreshCallback)) {
                mRefreshCallback.onRefreshEnd();
            }
            if (ObjectUtil.nonNull(mWebViewCallback)) {
                mWebViewCallback.onLoadError(view, errorCode, description, failingUrl);
            }
            Logger.e("onReceivedError   code: " + errorCode + " -- " + description);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public String onPageError(String url) {
            return getErrorPageUrl();
        }

        @Override
        public Map<String, String> onPageHeaders(String url) {
            return getHeaders();
        }
    }

    private class InnerOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            RefreshProgressWebView.this.setRefreshing(true);
            if (ObjectUtil.nonNull(mRefreshCallback)) {
                Map<String, String> cookies = mRefreshCallback.onRefreshBegin();
                if (StringUtil.isNotBlank(mCookieDomain) && CollectionUtil.notEmpty(cookies)) {
                    Logger.d("onRefresh   setCookies: " + cookies);
                    setCookies(mCookieDomain, cookies);
                    Logger.d("onRefresh   load: " + mUrl + " -- cookie: " + CookieUtil.getCookies(mCookieDomain));
                }
            }
            if (ObjectUtil.nonNull(mProView)) {
                if (ObjectUtil.nonNull(mRefreshCallback)) {
                    mRefreshCallback.onRefreshing();
                }
                mProView.loadUrl(mUrl);
            } else {
                RefreshProgressWebView.this.setRefreshing(false);
                if (ObjectUtil.nonNull(mRefreshCallback)) {
                    mRefreshCallback.onRefreshEnd();
                }
            }
        }
    }

    private class InnerScrollChangeListener implements ScrollChangeListener {

        @Override
        public void onPageEnd(int l, int t, int oldl, int oldt) {
            if (ObjectUtil.nonNull(mScrollChangeListener)) {
                mScrollChangeListener.onPageEnd(l, t, oldl, oldt);
            }
        }

        @Override
        public void onPageTop(int l, int t, int oldl, int oldt) {
            RefreshProgressWebView.this.setEnabled(true);
            if (ObjectUtil.nonNull(mScrollChangeListener)) {
                mScrollChangeListener.onPageTop(l, t, oldl, oldt);
            }
        }

        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt) {
            RefreshProgressWebView.this.setEnabled(false);
            if (ObjectUtil.nonNull(mScrollChangeListener)) {
                mScrollChangeListener.onScrollChanged(l, t, oldl, oldt);
            }
        }
    }
}
