# JsBridge

###  现在H5嵌入 Android App开发中已是一种潮流，经常会遇到H5与java交互的场景，本文根据 https://github.com/lzyzsd/JsBridge/ 封装了可以同步Cookie，header ，带有加载进度，以及下拉刷新的JsBridge，并解决了部分问题。
  
  ```java
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
 ```
