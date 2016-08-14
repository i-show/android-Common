/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
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
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bright.common.R;
import com.bright.common.utils.ScreenUtils;

import java.lang.ref.WeakReference;

/**
 * 自定义的Toast
 * <p/>
 * YToast 集成为单例Toast 预防多次提示
 */
public class YToast extends Toast {
    private static final String TAG = "YToast";

    private static WeakReference<YToast> mToast;
    private static int mScreenHeight;

    public YToast(Context context) {
        super(context);
        mScreenHeight = ScreenUtils.getScreenSize(context)[1];
    }

    public static void show(Context context, @StringRes int text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, @StringRes int text, int duration) {
        String content = context.getString(text);
        show(context, content, duration);
    }

    public static void show(Context context, CharSequence text, int duration) {
        YToast.makeText(context.getApplicationContext(), text, duration).show();
    }

    public static Toast makeText(Context context, CharSequence text, int duration) {
        YToast toast;
        if (mToast == null || mToast.get() == null) {
            Log.i(TAG, "makeText: 1111");
            toast = new YToast(context);
            mToast = new WeakReference<>(toast);
        } else {
            Log.i(TAG, "makeText: 2222");
            toast = mToast.get();
        }

        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.widget_ytoast, null);
        TextView tv = (TextView) v.findViewById(R.id.message);
        tv.setText(text);

        toast.setView(v);
        toast.setDuration(duration);
        toast.setGravity(toast.getGravity(), toast.getXOffset(), mScreenHeight / 5);
        return toast;
    }

    /**
     * Make a standard toast that just contains a text view with the text from a resource.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param resId    The resource id of the string resource to use.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or
     *                 {@link #LENGTH_LONG}
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    public static Toast makeText(Context context, @StringRes int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

}
