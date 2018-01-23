package com.ishow.common.widget.watermark;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.ishow.common.R;

/**
 * Created by yuhaiyang on 2018/1/23.
 * 带有水印的LinearLayout
 */

public class WaterMarkLinearLayout extends LinearLayout implements IWaterMark {

    private WaterMarkHelp mWaterMarkHelp;

    public WaterMarkLinearLayout(Context context) {
        super(context);
        init(context, null);
    }

    public WaterMarkLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WaterMarkLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WaterMarkLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        WaterMarkHelp.Params params = new WaterMarkHelp.Params();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WaterMarkLinearLayout);
        params.text = a.getString(R.styleable.WaterMarkLinearLayout_text);
        params.textSize = a.getDimensionPixelSize(R.styleable.WaterMarkLinearLayout_textSize, WaterMarkHelp.getDefaultTextSize(context));
        params.textColor = a.getColor(R.styleable.WaterMarkLinearLayout_textColor, WaterMarkHelp.getDefaultTextColor(context));
        params.alpha = a.getFloat(R.styleable.WaterMarkLinearLayout_waterMarkAlpha, WaterMarkHelp.getDefaultAlpha());
        params.enable = a.getBoolean(R.styleable.WaterMarkLinearLayout_waterMarkEnable, true);
        params.angle = a.getInt(R.styleable.WaterMarkLinearLayout_waterMarkAngle, WaterMarkHelp.getDefaultAngle());
        params.topPadding = a.getDimensionPixelSize(R.styleable.WaterMarkLinearLayout_topPadding, WaterMarkHelp.getDefaultPadding(context));
        params.bottomPadding = a.getDimensionPixelSize(R.styleable.WaterMarkLinearLayout_bottomPadding, WaterMarkHelp.getDefaultPadding(context));
        params.startPadding = a.getDimensionPixelSize(R.styleable.WaterMarkLinearLayout_startPadding, WaterMarkHelp.getDefaultPadding(context));
        params.endPadding = a.getDimensionPixelSize(R.styleable.WaterMarkLinearLayout_endPadding, WaterMarkHelp.getDefaultPadding(context));
        a.recycle();
        mWaterMarkHelp = new WaterMarkHelp();
        mWaterMarkHelp.init(this, params);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mWaterMarkHelp.draw(canvas, getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    public void setWaterMarkEnable(boolean enable) {
        mWaterMarkHelp.setWaterMarkEnable(enable);
    }
}
