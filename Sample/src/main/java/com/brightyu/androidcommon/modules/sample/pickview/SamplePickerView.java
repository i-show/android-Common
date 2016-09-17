package com.brightyu.androidcommon.modules.sample.pickview;

import android.os.Bundle;

import com.brightyu.androidcommon.R;
import com.brightyu.androidcommon.modules.base.AppBaseActivity;
import com.brightyu.androidcommon.ui.widget.pickview.PickerView;

/**
 * 选择View的Sample
 */
public class SamplePickerView extends AppBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_pickview);
    }

    @Override
    protected void initViews() {
        super.initViews();
        PickerView wheelView = (PickerView) findViewById(R.id.wheel);
        wheelView.setAdapter(new SampleWheelAdapter());
    }
}
