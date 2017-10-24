package com.tw.refreshprogressjsbridge;

/**
 * Created by wei.tian
 * 2017/9/24
 */

public interface ScrollChangeListener {
    void onPageEnd(int l, int t, int oldl, int oldt);

    void onPageTop(int l, int t, int oldl, int oldt);

    void onScrollChanged(int l, int t, int oldl, int oldt);
}
