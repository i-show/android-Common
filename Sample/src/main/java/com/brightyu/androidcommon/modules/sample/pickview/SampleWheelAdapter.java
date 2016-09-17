package com.brightyu.androidcommon.modules.sample.pickview;

import com.brightyu.androidcommon.ui.widget.pickview.adapter.PickerAdapter;

/**
 * Created by yuhaiyang on 2016/9/16.
 */
public class SampleWheelAdapter implements PickerAdapter<String> {
    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public String getItem(int index) {
        return String.valueOf(index);
    }

    @Override
    public int indexOf(String o) {
        return Integer.valueOf(o);
    }

    @Override
    public String getItemText(int position) {
        return String.valueOf(position);
    }
}
