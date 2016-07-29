package com.brightyu.androidcommon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.bright.common.app.BaseActivity;
import com.bright.common.utils.DataCleanUtils;
import com.bright.common.utils.ScreenUtils;
import com.bright.common.utils.image.select.MultipleSelectPhotoUtils;
import com.bright.common.utils.image.select.SelectPhotoUtils;
import com.bright.common.utils.image.select.SingleSelectPhotoUtils;
import com.brightyu.androidcommon.adapter.PhotoAdapter;
import com.brightyu.androidcommon.test.Test;
import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = "nian";
    private PhotoAdapter mAdapter;
    private ImageView mPhoto;
    private MultipleSelectPhotoUtils mSelectPhotoUtils;
    private SingleSelectPhotoUtils mSinglePhotoUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataCleanUtils.cleanApplicationData(this);
        GridView gridView = (GridView) findViewById(R.id.gird);
        mAdapter = new PhotoAdapter(gridView, this, ScreenUtils.getScreenSize(this)[0], 20, 20, 3);
        gridView.setAdapter(mAdapter);
        final ImageView photo = (ImageView) findViewById(R.id.imageView);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: 00000");
                mSinglePhotoUtils.select(SelectPhotoUtils.MODE_CROP);
            }
        });
        mSelectPhotoUtils = new MultipleSelectPhotoUtils(this);
        mSinglePhotoUtils = new SingleSelectPhotoUtils(this);
        mSelectPhotoUtils.setCallBack(new MultipleSelectPhotoUtils.CallBack() {
            @Override
            public void onResult(List<String> urls) {

            }
        });
        mSinglePhotoUtils.setCallBack(new SingleSelectPhotoUtils.CallBack() {
            @Override
            public void onResult(Uri url) {
                Glide.with(MainActivity.this)
                        .load(url)
                        .into(photo);
            }
        });
        Button button = (Button) findViewById(R.id.selectPhoto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectPhotoUtils.select(6);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSelectPhotoUtils.onActivityResult(requestCode, resultCode, data);
        mSinglePhotoUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final List<String> headers = Arrays.asList(Test.getPhotos(9));
        mAdapter.setData(headers);
    }
}
