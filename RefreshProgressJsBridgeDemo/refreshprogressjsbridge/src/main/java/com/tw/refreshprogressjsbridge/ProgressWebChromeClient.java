package com.tw.refreshprogressjsbridge;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.tw.refreshprogressjsbridge.internal.NumberProgressBar;
import com.tw.refreshprogressjsbridge.utils.ObjectUtil;

/**
 * Created by wei.tian
 * 2017/10/24
 */

public class ProgressWebChromeClient extends WebChromeClient {
    private final static int DEF = 98;
    private NumberProgressBar mProgressBar;

    public ProgressWebChromeClient(NumberProgressBar progressBar) {
        this.mProgressBar = ObjectUtil.requireNonNull(progressBar, "progressBar == null");
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (ObjectUtil.isNull(mProgressBar)) {
            return;
        }
        if (newProgress >= DEF) {
            mProgressBar.setVisibility(View.GONE);
        } else {
            if (mProgressBar.getVisibility() == View.GONE) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
            mProgressBar.setProgress(newProgress);
        }
        super.onProgressChanged(view, newProgress);
    }
}
