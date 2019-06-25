package com.ishow.common.widget.watermark;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.ishow.common.R;
import com.ishow.common.utils.log.LogUtils;

public class WaterMarkView extends View {
    private static final String TAG = "WaterMarkView";
    private WaterMarkHelp mWaterMarkHelp;

    public WaterMarkView(Context context) {
        super(context);
        init(context, null);
    }

    public WaterMarkView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WaterMarkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WaterMarkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        WaterMarkHelp.Params params = new WaterMarkHelp.Params();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WaterMarkView);
        params.text = a.getString(R.styleable.WaterMarkView_text);
        params.textSize = a.getDimensionPixelSize(R.styleable.WaterMarkView_textSize, WaterMarkHelp.getDefaultTextSize(context));
        params.textColor = a.getColor(R.styleable.WaterMarkView_textColor, WaterMarkHelp.getDefaultTextColor(context));
        params.alpha = a.getFloat(R.styleable.WaterMarkView_waterMarkAlpha, WaterMarkHelp.getDefaultAlpha());
        params.enable = a.getBoolean(R.styleable.WaterMarkView_waterMarkEnable, true);
        params.angle = a.getInt(R.styleable.WaterMarkView_waterMarkAngle, WaterMarkHelp.getDefaultAngle());
        params.topPadding = a.getDimensionPixelSize(R.styleable.WaterMarkView_topPadding, WaterMarkHelp.getDefaultPadding(context));
        params.bottomPadding = a.getDimensionPixelSize(R.styleable.WaterMarkView_bottomPadding, WaterMarkHelp.getDefaultPadding(context));
        params.startPadding = a.getDimensionPixelSize(R.styleable.WaterMarkView_startPadding, WaterMarkHelp.getDefaultPadding(context));
        params.endPadding = a.getDimensionPixelSize(R.styleable.WaterMarkView_endPadding, WaterMarkHelp.getDefaultPadding(context));
        a.recycle();
        mWaterMarkHelp = new WaterMarkHelp();
        mWaterMarkHelp.init(this, params);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWaterMarkHelp.draw(canvas, getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * 设置是否可见
     */
    @SuppressWarnings("unused")
    public static void setVisibility(Activity activity, int visibility) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        WaterMarkView markView = activity.findViewById(R.id.water_mark_view);
        if (markView == null) {
            return;
        }

        markView.setVisibility(visibility);
    }

    /**
     * 添加到attachView
     */
    @SuppressWarnings("unused")
    public static void attachToActivity(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        WaterMarkView markView = activity.findViewById(R.id.water_mark_view);
        if (markView != null) {
            LogUtils.i(TAG, "already add");
            return;
        }
        markView = new WaterMarkView(activity);
        markView.setId(R.id.water_mark_view);
        ViewGroup root = activity.findViewById(android.R.id.content);

        root.addView(markView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * 从Activity中移除
     */
    @SuppressWarnings("unused")
    public static void detachedFromActivity(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        WaterMarkView markView = activity.findViewById(R.id.water_mark_view);
        if (markView == null) {
            return;
        }
        ViewGroup root = activity.findViewById(android.R.id.content);
        root.removeView(markView);
    }
}
