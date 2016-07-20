package com.brightyu.androidcommon;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bright.common.BaseActivity;
import com.bright.common.widget.dialog.ShowPhotoDialog;
import com.brightyu.androidcommon.test.Test;
import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = "nian";
    private ImageView mPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPhoto = (ImageView) findViewById(R.id.photo);
        final List<String> headers = Arrays.asList(Test.getPhotos(3));
        Glide.with(this)
                .load(headers.get(0))
                .into(mPhoto);
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPhotoDialog dialog = new ShowPhotoDialog(MainActivity.this);
                dialog.setBeforeView(mPhoto);
                dialog.setData(headers);
                dialog.show();
            }
        });

    }
}
