package com.tw.refreshprogressjsbridge;


import com.tw.refreshprogressjsbridge.internal.CallBackFunction;

/**
 * Created by wei.tian
 * 2017/9/23
 */
public interface JsHandler {
    void onHandler(String handlerName, String responseData, CallBackFunction function);
}
