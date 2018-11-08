package com.ishow.common.widget.watermark;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ishow.common.R;

/**
 * Created by yuhaiyang on 2018/1/23.
 * 带有水印的LinearLayout
 */

public class WaterMarkRelativeLayout extends RelativeLayout implements IWaterMark {

    private WaterMarkHelp mWaterMarkHelp;

    public WaterMarkRelativeLayout(Context context) {
        super(context);
        init(context, null);
    }

    public WaterMarkRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WaterMarkRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WaterMarkRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        WaterMarkHelp.Params params = new WaterMarkHelp.Params();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WaterMarkRelativeLayout);
        params.text = a.getString(R.styleable.WaterMarkRelativeLayout_text);
        params.textSize = a.getDimensionPixelSize(R.styleable.WaterMarkRelativeLayout_textSize, WaterMarkHelp.getDefaultTextSize(context));
        params.textColor = a.getColor(R.styleable.WaterMarkRelativeLayout_textColor, WaterMarkHelp.getDefaultTextColor(context));
        params.alpha = a.getFloat(R.styleable.WaterMarkRelativeLayout_waterMarkAlpha, WaterMarkHelp.getDefaultAlpha());
        params.enable = a.getBoolean(R.styleable.WaterMarkRelativeLayout_waterMarkEnable, true);
        params.angle = a.getInt(R.styleable.WaterMarkRelativeLayout_waterMarkAngle, WaterMarkHelp.getDefaultAngle());
        params.topPadding = a.getDimensionPixelSize(R.styleable.WaterMarkRelativeLayout_topPadding, WaterMarkHelp.getDefaultPadding(context));
        params.bottomPadding = a.getDimensionPixelSize(R.styleable.WaterMarkRelativeLayout_bottomPadding, WaterMarkHelp.getDefaultPadding(context));
        params.startPadding = a.getDimensionPixelSize(R.styleable.WaterMarkRelativeLayout_startPadding, WaterMarkHelp.getDefaultPadding(context));
        params.endPadding = a.getDimensionPixelSize(R.styleable.WaterMarkRelativeLayout_endPadding, WaterMarkHelp.getDefaultPadding(context));
        a.recycle();
        mWaterMarkHelp = new WaterMarkHelp();
        mWaterMarkHelp.init(this, params);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        mWaterMarkHelp.draw(canvas, getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    public void setWaterMarkEnable(boolean enable) {
        mWaterMarkHelp.setWaterMarkEnable(enable);
    }
}
