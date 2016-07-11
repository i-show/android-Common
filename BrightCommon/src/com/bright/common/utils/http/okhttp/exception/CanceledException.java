package com.bright.common.utils.http.okhttp.exception;

/**
 * 已经被取消了的错误
 */
public class CanceledException extends Exception {
    public CanceledException() {
        super("Call is Canceled");
    }
}
