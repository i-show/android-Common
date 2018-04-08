package com.ishow.noah.utils.http;

import android.content.Context;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.ishow.common.utils.http.rest.Headers;
import com.ishow.common.utils.http.rest.HttpError;
import com.ishow.common.utils.http.rest.callback.CallBack;
import com.ishow.common.utils.http.rest.config.HttpConfig;
import com.ishow.common.utils.http.rest.exception.HttpErrorException;
import com.ishow.common.utils.http.rest.request.Request;
import com.ishow.common.utils.http.rest.response.Response;
import com.ishow.noah.constant.Configure;
import com.ishow.noah.entries.http.AppHttpResult;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by yuhaiyang on 2017/5/2.
 * 对应项目的处理
 */

public abstract class AppHttpCallBack<T> extends CallBack<T> {

    protected AppHttpCallBack(Context context) {
        super(context);
    }


    @Override
    public T parseResponse(@NonNull Request request, @NonNull Response response) throws HttpErrorException {
        Headers headers = response.getHeaders();
        HttpConfig.setHeader(Configure.HTTP_TOKEN, headers.get(Configure.HTTP_TOKEN));

        Type genType = getClass().getGenericSuperclass();
        Type type;
        if (genType instanceof ParameterizedType) {
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            type = params[0];
        } else {
            type = String.class;
        }


        String body = new String(response.getBody());
        response.setDebugString(body);

        AppHttpResult result = JSON.parseObject(body, AppHttpResult.class);
        if (result.isSuccess()) {
            Object value = result.getValue();
            if (value == null) {
                return null;
            }

            String valueString = String.valueOf(value);
            if (!valueString.startsWith("[") && !valueString.startsWith("{")) {
                valueString = JSON.toJSONString(value);
            }
            return JSON.parseObject(valueString, type);
        } else {
            HttpError error = HttpError.makeError(request);
            error.setCode(result.getCode());
            error.setMessage(result.getMessage());
            HttpErrorException e = new HttpErrorException();
            e.setHttpError(error);
            throw e;
        }
    }

}
