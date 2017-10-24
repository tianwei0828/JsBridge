package com.tw.refreshprogressjsbridge.logger;

public interface LogStrategy {

  void log(int priority, String tag, String message);
}
