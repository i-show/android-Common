package com.brightyu.androidcommon.ui.widget.pickview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;
import android.widget.TimePicker;

import com.bright.common.utils.StringUtils;
import com.brightyu.androidcommon.R;
import com.brightyu.androidcommon.ui.widget.pickview.adapter.PickerAdapter;
import com.brightyu.androidcommon.ui.widget.pickview.listener.OnItemSelectedListener;

/**
 * 3d滚轮控件
 */
public class PickerView extends View {
    private static final String TAG = "PickerView";
    /**
     * 默认可以看到的几个Item
     */
    private static final int DEFAULT_VISIABLE_COUNT = 5;

    /**
     * ITEM间距倍数
     */
    private static final float LINE_SPACING_MULTIPLIER = 2.0f;
    /**
     * 未选中TextSize大小比例
     */
    private static final float UNSELECTED_TEXT_SIZE_RATIO = 0.9f;

    private OnItemSelectedListener mItemSelectedListener;

    private Context mContext;

    private Paint mUnselectedTextPaint;
    private Paint mSelectedTextPaint;
    private Paint mDividerPaint;

    private PickerAdapter mAdapter;

    private String mUnit;//附加单位
    private int mTextSize;//选项的文字大小

    private int mDividerColor;
    private int mSelectedTextColor;
    private int mUnselectedTextColor;

    /**
     * 当前的Position
     */
    private int mCurrentPosition;

    /**
     * 几个Item是可见的
     */
    private int mVisibleCount;

    /**
     * 一个Item想要的宽度和高度
     */
    private int mItemDesireWidth;
    private int mItemDesireHeight;

    private int mGravity;
    private int mGap;
    private int mMinVelocity;
    // 滚动的最后Y
    private int mLastY;
    private int mMove;
    // 第一条线Y坐标值
    private float mFirstLineY;
    //第二条线Y坐标
    private float mSecondLineY;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;


    // 是否循环
    private boolean isCyclic;

    public PickerView(Context context) {
        this(context, null);
    }

    public PickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PickerView);
        mGravity = a.getInt(R.styleable.PickerView_android_gravity, Gravity.CENTER);
        mSelectedTextColor = a.getColor(R.styleable.PickerView_selectedTextColor, getDefaultSelectedTextColor());
        mUnselectedTextColor = a.getColor(R.styleable.PickerView_unselectedTextColor, getDefaultUnselectedTextColor());
        mDividerColor = a.getColor(R.styleable.PickerView_divider, getDefaultDividerColor());
        mTextSize = a.getDimensionPixelOffset(R.styleable.PickerView_textSize, getDefaultTextSize());
        mVisibleCount = a.getInteger(R.styleable.PickerView_visiableCount, DEFAULT_VISIABLE_COUNT);
        a.recycle();

        init(context);
    }

    private void init(Context context) {


        mContext = context;
        mGap = context.getResources().getDimensionPixelSize(R.dimen.gap_grade_2);
        mScroller = new Scroller(context);
        isCyclic = true;

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        initPaints();
        forecast();
    }

    private void initPaints() {
        mDividerPaint = new Paint();
        mDividerPaint.setColor(mDividerColor);
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setStrokeWidth((float) 1.0);
        mDividerPaint.setDither(true);

        mSelectedTextPaint = new Paint();
        mSelectedTextPaint.setColor(mSelectedTextColor);
        mSelectedTextPaint.setAntiAlias(true);
        mSelectedTextPaint.setDither(true);
        mSelectedTextPaint.setTypeface(Typeface.MONOSPACE);
        mSelectedTextPaint.setTextSize(mTextSize);

        mUnselectedTextPaint = new Paint();
        mUnselectedTextPaint.setColor(mUnselectedTextColor);
        mUnselectedTextPaint.setAntiAlias(true);
        mUnselectedTextPaint.setDither(true);
        mUnselectedTextPaint.setTypeface(Typeface.MONOSPACE);
        mUnselectedTextPaint.setTextSize(mTextSize * UNSELECTED_TEXT_SIZE_RATIO);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int height = getMeasuredHeight();

        //计算两条横线和控件中间点的Y位置
        mFirstLineY = (height - mItemDesireHeight) / 2;
        mSecondLineY = (height + mItemDesireHeight) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mAdapter == null) {
            Log.i(TAG, "onDraw: adapter is null");
            return;
        }
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();

        //Step. 1 中间两条横线
        drawLine(canvas, width);
        //Step. 2 画选中的Text
        drawSelectedItem(canvas, width, height);
    }


    private void drawLine(Canvas canvas, int viewWidth) {
        canvas.drawLine(0, mFirstLineY, viewWidth, mFirstLineY, mDividerPaint);
        canvas.drawLine(0, mSecondLineY, viewWidth, mSecondLineY, mDividerPaint);
    }

    private void drawSelectedItem(Canvas canvas, int viewWidth, int viewHeight) {
        canvas.save();
        canvas.clipRect(0, mFirstLineY, viewWidth, mFirstLineY + mItemDesireHeight);
        canvas.drawText("AA", viewWidth / 2, mFirstLineY + 10, mSelectedTextPaint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int y = (int) event.getY();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScroller.forceFinished(true);
                mLastY = y;
                mMove = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                mMove += (mLastY - y);
                //changeMoveAndValue();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //countMoveEnd();
                //countVelocityTracker(event);
                return false;
            default:
                break;
        }

        mLastY = y;
        return true;
    }

    private int measureWidth(int widthMeasureSpec) {
        final int mode = MeasureSpec.getMode(widthMeasureSpec);
        final int size = MeasureSpec.getSize(widthMeasureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                return mItemDesireWidth + mGap * 2;
            case MeasureSpec.EXACTLY:
                return size;
            case MeasureSpec.AT_MOST:
                return Math.min(size, mItemDesireWidth + mGap * 2);
        }

        return size;
    }


    private int measureHeight(int heightMeasureSpec) {
        final int mode = MeasureSpec.getMode(heightMeasureSpec);
        final int size = MeasureSpec.getSize(heightMeasureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                return (mVisibleCount * mItemDesireHeight + mGap * 2);
            case MeasureSpec.EXACTLY:
                return size;
            case MeasureSpec.AT_MOST:
                return Math.min(mVisibleCount * mItemDesireHeight + mGap * 2, size);
        }

        return size;
    }


    /**
     * 预先计算Item
     */
    private void forecast() {

        if (mAdapter == null) {
            Log.i(TAG, "forecast: adapter is null");
            return;
        }

        Rect rect = new Rect();
        for (int i = 0; i < mAdapter.getCount(); i++) {
            String centerString;
            if (TextUtils.isEmpty(mUnit)) {
                centerString = mAdapter.getItemText(i);
            } else {
                centerString = StringUtils.plusString(mAdapter.getItemText(i), " ", mUnit);
            }

            mSelectedTextPaint.getTextBounds(centerString, 0, centerString.length(), rect);

            mItemDesireWidth = Math.max(mItemDesireWidth, rect.width());
            mItemDesireHeight = Math.max(mItemDesireHeight, rect.height());
        }
        mItemDesireHeight = (int) (LINE_SPACING_MULTIPLIER * mItemDesireHeight);
    }

    /**
     * GET or SET 配置区域
     */
    /**
     * 设置选中监听
     */
    public final void setOnItemSelectedListener(OnItemSelectedListener OnItemSelectedListener) {
        mItemSelectedListener = OnItemSelectedListener;
    }

    /**
     * 是否循环滚动
     */
    public final boolean isCyclic() {
        return isCyclic;
    }

    /**
     * 设置是否循环滚动
     */
    public final void setCyclic(boolean cyclic) {
        isCyclic = cyclic;
    }

    /**
     * getAdapter
     */
    public final PickerAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * setAdapter
     */
    public final void setAdapter(@NonNull PickerAdapter adapter) {
        mAdapter = adapter;
        forecast();
        requestLayout();
        //postInvalidate();
    }


    /***
     * ============= 默认值区域 ================
     */

    /**
     * 获取未选中的Text的颜色
     */
    protected int getDefaultDividerColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(R.color.line, getContext().getTheme());
        } else {
            return getResources().getColor(R.color.line);
        }
    }


    /**
     * 获取未选中的Text的颜色
     */
    protected int getDefaultUnselectedTextColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(R.color.text_grey_light_normal, getContext().getTheme());
        } else {
            return getResources().getColor(R.color.text_grey_light_normal);
        }
    }


    /**
     * 获取未选中的Text的颜色
     */
    protected int getDefaultSelectedTextColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(R.color.text_grey_dark_normal, getContext().getTheme());
        } else {
            return getResources().getColor(R.color.text_grey_dark_normal);
        }
    }

    /**
     * 获取未选中的Text的颜色
     */
    protected int getDefaultTextSize() {
        return getResources().getDimensionPixelSize(R.dimen.G_title);
    }
}