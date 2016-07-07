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
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bright.common.R;

/**
 * 自定义的Toast
 */
public class YToast extends Toast {
    /**
     */
    public YToast(Context context) {
        super(context);
    }

    public static Toast makeText(Context context, CharSequence text, int duration) {
        Toast result = new Toast(context);

        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.widget_ytoast, null);
        TextView tv = (TextView) v.findViewById(R.id.message);
        tv.setText(text);

        result.setView(v);
        result.setDuration(duration);
        int y = result.getYOffset();
        result.setGravity(result.getGravity(), result.getXOffset(), (int) (y * 1.5));
        return result;
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
