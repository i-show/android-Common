/**
 * Copyright (C) 2015  Haiyang Yu Android Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bright.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.bright.common.R;

public class DashLine extends View {
    private static final int COLOR = 0XFF808080;
    private Paint mLinePaint;

    public DashLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        int gap = getResources().getDimensionPixelOffset(R.dimen.dp_3);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(COLOR);
        mLinePaint.setStrokeWidth(gap / 2);
        PathEffect effects = new DashPathEffect(new float[]{gap, gap, gap, gap}, 1);
        mLinePaint.setPathEffect(effects);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int x = width / 2;
        int y = height / 2;
        canvas.drawLine(0, y, width, y, mLinePaint);
    }
}