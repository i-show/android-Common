package com.ishow.noahark.modules.sample.pickview;

import android.os.Bundle;
import android.view.View;

import com.ishow.common.utils.DateUtils;
import com.ishow.common.widget.pickview.DateTimePicker;
import com.ishow.common.widget.pickview.DateTimePickerDialog;
import com.ishow.common.widget.pickview.PickerView;
import com.ishow.noahark.R;
import com.ishow.noahark.modules.base.AppBaseActivity;

/**
 * 选择View的Sample
 */
public class SamplePickerActivity extends AppBaseActivity implements View.OnClickListener {

    private DateTimePicker mDateTimePicker;

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

        mDateTimePicker = (DateTimePicker) findViewById(R.id.datePicker);

        View getTime = findViewById(R.id.get_time);
        getTime.setOnClickListener(this);

        View dialogTest = findViewById(R.id.dialog_test);
        dialogTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_time:
                String time = DateUtils.format(mDateTimePicker.getCurrentTime(), DateUtils.FORMAT_YMDHMS_CN);
                dialog(time);
                break;
            case R.id.dialog_test:
                DateTimePickerDialog dialog = new DateTimePickerDialog(this);
                dialog.show();
                break;
        }
    }
}
