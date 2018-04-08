/*
 * Copyright (C) 2017. The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.noah.modules.sample.dialog.select;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ishow.common.entries.IUnitPicker;
import com.ishow.common.entries.IUnitSelect;
import com.ishow.common.widget.dialog.picker.PickerDialog;
import com.ishow.common.widget.dialog.select.SelectDialog;
import com.ishow.noah.R;
import com.ishow.noah.modules.base.AppBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhaiyang on 2017/6/13.
 * 选择弹框和滚动选择额
 */

public class SampleSelectDialogAndPickerDialog extends AppBaseActivity implements View.OnClickListener {
    private TextView mSelect;
    private TextView mPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_dialog_select_and_picker);
    }

    @Override
    protected void initViews() {
        super.initViews();
        mSelect = (TextView) findViewById(R.id.select);
        mSelect.setOnClickListener(this);

        mPicker = (TextView) findViewById(R.id.picker);
        mPicker.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select:
                testSelect();
                break;
            case R.id.picker:
                testPicker();
                break;
        }
    }

    private void testSelect() {
        SelectDialog<DemoEntry> dialog = new SelectDialog<>(this);
        dialog.setData(getDatas());
        dialog.setOnSelectedListener(new SelectDialog.OnSelectedListener<DemoEntry>() {
            @Override
            public void onSelected(DemoEntry demoEntry) {
                mSelect.setText(demoEntry.getTitle(SampleSelectDialogAndPickerDialog.this));
            }
        });
        dialog.show();
    }

    private void testPicker() {
        PickerDialog<DemoEntry> dialog = new PickerDialog<>(this);
        dialog.setData(getDatas());
        dialog.setOnSelectedListener(new PickerDialog.OnPickedListener<DemoEntry>() {
            @Override
            public void onSelectPicked(DemoEntry demoEntry) {
                mPicker.setText(demoEntry.getTitle(SampleSelectDialogAndPickerDialog.this));
            }
        });
        dialog.show();
    }

    private List<DemoEntry> getDatas() {
        List<DemoEntry> entryList = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            entryList.add(new DemoEntry(i));
        }
        return entryList;
    }


    //偷懒Entry 写在这里
    private class DemoEntry implements IUnitSelect, IUnitPicker {
        int day;

        DemoEntry(int day) {
            this.day = day;
        }

        @Override
        public String getTitle(Context context) {
            return day + " 天";
        }

        @Override
        public String getSubTitle(Context context) {
            return null;
        }
    }

}
