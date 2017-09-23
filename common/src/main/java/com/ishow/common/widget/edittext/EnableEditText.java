/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
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

package com.ishow.common.widget.edittext;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Bright.Yu on 2016/11/12.
 * 是否可以变编辑
 */

public class EnableEditText extends AppCompatEditText {
    private boolean isEditable;

    public EnableEditText(Context context) {
        super(context);
        init();
    }

    public EnableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EnableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        isEditable = true;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isEditable && super.onTouchEvent(event);
    }
}
