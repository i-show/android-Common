package com.ishow.noahark.modules.sample.imageloader;

import android.os.Bundle;
import android.widget.ImageView;

import com.ishow.noahark.R;
import com.ishow.noahark.modules.base.AppBaseActivity;
import com.ishow.noahark.modules.sample.Test;
import com.ishow.common.utils.image.loader.ImageLoader;


public class SampleImageLoaderActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_imageloader);
    }


    @Override
    protected void initViews() {
        super.initViews();
        ImageView imageView = (ImageView) findViewById(R.id.test1);
        
        ImageLoader.with(this)
                .load(Test.HEADER_LIST[1])
                .into(imageView);
    }
}
