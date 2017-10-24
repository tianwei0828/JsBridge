package com.tw.refreshprogressjsbridge;

import java.util.Map;

/**
 * Created by wei.tian
 * 2017/9/23
 */

public interface RefreshCallback {

    Map<String, String> onRefreshBegin();

    void onRefreshing();

    void onRefreshEnd();
}
