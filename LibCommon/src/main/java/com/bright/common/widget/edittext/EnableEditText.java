package com.bright.common.widget.edittext;

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
