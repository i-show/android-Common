package com.brightyu.androidcommon;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.bright.common.BaseActivity;
import com.bright.common.utils.DataCleanUtils;
import com.bright.common.utils.ScreenUtils;
import com.bright.common.widget.dialog.ShowPhotoDialog;
import com.brightyu.androidcommon.adapter.PhotoAdapter;
import com.brightyu.androidcommon.test.Test;
import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = "nian";
    private PhotoAdapter mAdapter;
    private ImageView mPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataCleanUtils.cleanApplicationData(this);
        GridView gridView = (GridView) findViewById(R.id.gird);
        mAdapter = new PhotoAdapter(gridView, this, ScreenUtils.getScreenSize(this)[0], 20, 20, 3);
        gridView.setAdapter(mAdapter);


        final List<String> headers = Arrays.asList(Test.getPhotos(9));
        mPhoto = (ImageView) findViewById(R.id.photo);
        Glide.with(this)
                .load(headers.get(3))
                .into(mPhoto);


        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPhotoDialog dialog = new ShowPhotoDialog(MainActivity.this);
                dialog.setCurrentPosition(3);
                dialog.setData(headers);
                dialog.setBeforeView(mPhoto);
                dialog.show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        final List<String> headers = Arrays.asList(Test.getPhotos(9));
        mAdapter.setData(headers);
    }
}
