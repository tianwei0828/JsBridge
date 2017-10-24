package com.tw.refreshprogressjsbridgedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tw.refreshprogressjsbridge.JavaHandler;
import com.tw.refreshprogressjsbridge.JsHandler;
import com.tw.refreshprogressjsbridge.RefreshProgressWebView;
import com.tw.refreshprogressjsbridge.internal.CallBackFunction;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RefreshProgressWebView refreshProgressWebView = (RefreshProgressWebView) findViewById(R.id.rpwv);
        // 注册 java 调用 js 中名为 jsHandlerName 的 方法
        refreshProgressWebView.registerJsHandler("jsHandlerName", new JsHandler() {
            @Override
            public void onHandler(String handlerName, String responseData, CallBackFunction function) {
                //js返回值
            }
        });
        // 注册 js 调用 java 中名为 javaHandlerName 的方法，并且在调用的时候，java将sendToJsData 给到js
        refreshProgressWebView.registerJavaHandler("javaHandlerName", "sendToJsData", new JavaHandler() {
            @Override
            public void onHandler(String handlerName, String jsResponseData) {
                //js 返回值
            }
        });
        //添加cookie
        Map<String, String> cookies = new HashMap<>();
        cookies.put("key", "value");
        refreshProgressWebView.setCookies("url", cookies);
        //开启下拉刷新功能，默认开启
        refreshProgressWebView.setEnabled(true);
        //添加header
        refreshProgressWebView.setHeader("key","value");
        refreshProgressWebView.load("https://www.baidu.com");
    }


}
