package com.ishow.noah.ui.widget;

/**
 * Created by yuhaiyang on 2020/7/6.
 */

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;

import org.xmlpull.v1.XmlPullParser;

/**
 * Extension of {@link DrawableWrapper} which scales the child drawables by a fixed amount.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class FixedScaleDrawable extends DrawableWrapper {

    // TODO b/33553066 use the constant defined in MaskableIconDrawable
    private static final float LEGACY_ICON_SCALE = 1F;
    private float mScaleX, mScaleY;

    public FixedScaleDrawable() {
        super(new ColorDrawable(Color.RED));
        mScaleX = LEGACY_ICON_SCALE;
        mScaleY = LEGACY_ICON_SCALE;
    }

    @Override
    public void draw(Canvas canvas) {
        int saveCount = canvas.save();
        canvas.scale(mScaleX, mScaleY, getBounds().exactCenterX(), getBounds().exactCenterY());
        super.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    @Override
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs) {
    }

    @Override
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) {
    }

    public void setScale(float scale) {
        float h = getIntrinsicHeight();
        float w = getIntrinsicWidth();
        mScaleX = scale * LEGACY_ICON_SCALE;
        mScaleY = scale * LEGACY_ICON_SCALE;
        if (h > w && w > 0) {
            mScaleX *= w / h;
        } else if (w > h && h > 0) {
            mScaleY *= h / w;
        }
    }


    /**
     * Drawable转换成一个Bitmap
     *
     * @param drawable drawable对象
     * @return
     */
    public static final Bitmap drawableToBitmap(Drawable drawable, Float scale) {
        final int width = (int) (drawable.getIntrinsicWidth() * scale);
        final int height = (int) (drawable.getIntrinsicHeight() * scale);
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.RED);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
}
