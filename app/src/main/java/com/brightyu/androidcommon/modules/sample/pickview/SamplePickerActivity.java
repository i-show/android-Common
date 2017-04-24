package com.brightyu.androidcommon.modules.sample.pickview;

import android.os.Bundle;

import com.ishow.common.widget.pickview.PickerView;
import com.brightyu.androidcommon.R;
import com.brightyu.androidcommon.modules.base.AppBaseActivity;

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
        PickerView pickerView = (PickerView) findViewById(R.id.picker_view);
        pickerView.setAdapter(new SamplePickerAdapter());
    }
}
