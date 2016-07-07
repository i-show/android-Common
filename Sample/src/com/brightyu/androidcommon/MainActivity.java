package com.brightyu.androidcommon;

import android.os.Bundle;
import android.util.Log;

import com.bright.common.BaseActivity;

public class MainActivity extends BaseActivity {
    private static final String TAG = "nian";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: path = " + getFilesDir().getPath());
    }
}
