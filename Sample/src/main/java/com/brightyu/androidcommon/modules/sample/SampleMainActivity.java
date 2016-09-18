package com.brightyu.androidcommon.modules.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.brightyu.androidcommon.R;
import com.brightyu.androidcommon.modules.base.AppBaseActivity;
import com.brightyu.androidcommon.modules.sample.pickview.SamplePickerActivity;

/**
 * 测试Demo
 */
public class SampleMainActivity extends AppBaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_main);
    }

    @Override
    protected void initViews() {
        super.initViews();

        View pickView = findViewById(R.id.pickview_sample);
        pickView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.pickview_sample:
                intent = new Intent(this, SamplePickerActivity.class);
                startActivity(intent);
                break;
        }
    }
}
