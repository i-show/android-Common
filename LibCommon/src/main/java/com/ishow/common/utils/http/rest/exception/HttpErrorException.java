package com.ishow.common.utils.http.rest.exception;

import android.support.annotation.NonNull;

import com.ishow.common.utils.http.rest.HttpError;

/**
 * Created by yuhaiyang on 2017/5/2.
 * HttpError
 */

public class HttpErrorException extends Exception {
    private HttpError mHttpError;

    public void setHttpError(@NonNull HttpError error) {
        mHttpError = error;
    }

    public HttpError getHttpError() {
        return mHttpError;
    }
}
