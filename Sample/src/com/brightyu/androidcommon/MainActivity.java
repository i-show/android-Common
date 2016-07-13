package com.brightyu.androidcommon;

import android.os.Bundle;
import android.util.Log;

import com.bright.common.BaseActivity;
import com.bright.common.utils.http.okhttp.OkHttpUtils;
import com.bright.common.utils.http.okhttp.callback.BaseJsonCallBack;

public class MainActivity extends BaseActivity {
    private static final String TAG = "nian";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate: path = " + getFilesDir().getPath());
        OkHttpUtils.get()
                .url("http://tr90appprodsingapore.azurewebsites.net/rest/public/account/areas")
                .build()
                .execute(new BaseJsonCallBack() {
                    @Override
                    public void onSuccess(String result, int id) {
                    }
                });
        // {phone=15900667472, password=1qaz2wsx}
        OkHttpUtils.post()
                .url("http://wx.pinet.cc:8081/plife2/index.php/Api/Login")
                .addParams("phone", "15900667472")
                .addParams("password", "1qaz2wsx")
                .id(123)
                .logTag("nian")
                .build().execute(new BaseJsonCallBack() {
            @Override
            public void onSuccess(String result, int id) {

            }
        });
    }
}
