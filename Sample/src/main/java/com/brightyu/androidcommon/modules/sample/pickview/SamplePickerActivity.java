package com.brightyu.androidcommon.modules.sample.pickview;

import android.os.Bundle;
import android.widget.NumberPicker;

import com.brightyu.androidcommon.R;
import com.brightyu.androidcommon.modules.base.AppBaseActivity;
import com.brightyu.androidcommon.ui.widget.pickview.PickerView;

/**
 * 选择View的Sample
 */
public class SamplePickerActivity extends AppBaseActivity {
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

        NumberPicker np2 = (NumberPicker) findViewById(R.id.np2);
        //设置np2的最小值和最大值
        np2.setMinValue(60);
        np2.setMaxValue(100);
        //设置np2的当前值
        np2.setValue(70);
    }
}
