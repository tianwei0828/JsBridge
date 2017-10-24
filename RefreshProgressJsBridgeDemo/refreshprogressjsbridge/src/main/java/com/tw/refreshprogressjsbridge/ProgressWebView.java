package com.tw.refreshprogressjsbridge;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;

import com.tw.refreshprogressjsbridge.internal.BridgeHandler;
import com.tw.refreshprogressjsbridge.internal.CallBackFunction;
import com.tw.refreshprogressjsbridge.internal.IProgressWebView;
import com.tw.refreshprogressjsbridge.internal.NumberProgressBar;
import com.tw.refreshprogressjsbridge.utils.CollectionUtil;
import com.tw.refreshprogressjsbridge.utils.CookieUtil;
import com.tw.refreshprogressjsbridge.utils.ObjectUtil;
import com.tw.refreshprogressjsbridge.utils.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by wei.tian
 * 2017/9/23
 */

public class ProgressWebView extends BridgeWebView implements IProgressWebView {
    private NumberProgressBar mProgressBar;

    public ProgressWebView(Context context) {
        super(context);
        init(context, null);
    }

    public ProgressWebView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgressWebView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 初始化进度条
        mProgressBar = new NumberProgressBar(context, attrs);
        addView(mProgressBar,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        initSettings();
        initClient();
        initListener();
    }


    private void initSettings() {
        WebSettings webviewSettings = getSettings();
        // 判断系统版本是不是5.0或之上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //让系统不屏蔽混合内容和第三方Cookie
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
            webviewSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // 不支持缩放
        webviewSettings.setSupportZoom(false);
        //设置存储
        webviewSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webviewSettings.setAllowFileAccess(true);
        webviewSettings.setAppCacheEnabled(true);
        webviewSettings.setDomStorageEnabled(true);
        webviewSettings.setDatabaseEnabled(true);
        // 自适应屏幕大小
        webviewSettings.setUseWideViewPort(true);
        webviewSettings.setLoadWithOverviewMode(true);
    }


    private void initClient() {
        initWebChromeClient();
    }

    private void initWebChromeClient() {
        this.setWebChromeClient(/*default web chrome client*/new ProgressWebChromeClient(mProgressBar));
    }


    private void initListener() {
        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        this.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (ProgressWebView.this.canGoBack()) {
                            ProgressWebView.this.goBack();
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }


    @Override
    public NumberProgressBar getProgressBar() {
        return mProgressBar;
    }

    @Override
    public void setCookies(String url, Map<String, String> cookies) {
        StringUtil.checkNotBlank(url, "url is blank");
        CollectionUtil.checkNotEmpty(cookies, "cookies are empty");
        CookieUtil.synCookies(getContext(), url, cookies);
    }

    @Override
    public String getCookies(String url) {
        StringUtil.checkNotBlank(url, "url is blank");
        return CookieUtil.getCookies(url);
    }

    @Override
    public void removeAllCookies() {
        CookieUtil.removeAllCookies();
    }

    @Override
    public void registerJsHandler(final String handlerName, final JsHandler jsHandler) {
        StringUtil.checkNotBlank(handlerName, "handlerName is blank");
        ObjectUtil.checkNonNull(jsHandler, "jsHandler == null");
        this.registerHandler(handlerName, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                jsHandler.onHandler(handlerName, data, function);
            }
        });
    }

    @Override
    public void registerJsHandler(List<String> handlersName, final JsHandler jsHandler) {
        CollectionUtil.checkNotEmpty(handlersName, "handlersName are empty");
        ObjectUtil.checkNonNull(jsHandler, "jsHandler == null");
        for (final String handlerName : handlersName) {
            if (StringUtil.isBlank(handlerName)) {
                return;
            }
            this.registerHandler(handlerName, new BridgeHandler() {
                @Override
                public void handler(String data, CallBackFunction function) {
                    jsHandler.onHandler(handlerName, data, function);
                }
            });
        }
    }

    @Override
    public void registerJavaHandler(final String handlerName, String javaData, final JavaHandler javaHandler) {
        StringUtil.checkNotBlank(handlerName, "handlerName is blank");
        StringUtil.checkNotBlank(javaData, "javaData is blank");
        ObjectUtil.checkNonNull(javaHandler, "javaHandler == null");
        this.callHandler(handlerName, javaData, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                javaHandler.onHandler(handlerName, data);
            }
        });
    }

    @Override
    public void registerJavaHandler(Map<String, String> handlerInfos, final JavaHandler javaHandler) {
        CollectionUtil.checkNotEmpty(handlerInfos, "handlerInfos are empty");
        ObjectUtil.checkNonNull(javaHandler, "javaHandler == null");
        for (Map.Entry<String, String> me : handlerInfos.entrySet()) {
            final String handlerName = me.getKey();
            final String javaData = me.getValue();
            if (StringUtil.isBlank(handlerName) || StringUtil.isBlank(javaData)) {
                return;
            }
            this.callHandler(handlerName, javaData, new CallBackFunction() {
                @Override
                public void onCallBack(String data) {
                    javaHandler.onHandler(handlerName, data);
                }
            });
        }
    }
}
