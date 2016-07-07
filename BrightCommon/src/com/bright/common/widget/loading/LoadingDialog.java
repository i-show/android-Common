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

package com.bright.common.widget.loading;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.StringRes;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bright.common.R;
import com.bright.common.utils.Utils;
import com.bright.common.utils.glide.GlideUtils;


public class LoadingDialog extends Dialog {

    public static final int HANDLE_DISMISS = 0;
    public static final int DISMISS_DELAY = 300;
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoadingDialog dialog = (LoadingDialog) msg.obj;
            dialog.dismiss();
        }
    };
    private LinearLayout mContent;
    private boolean hasActionBar = true;
    private boolean isSemipermeable;

    public LoadingDialog(Context context) {
        this(context, true, true);
    }

    public LoadingDialog(Context context, boolean hasBar, boolean semipermeable) {
        this(context, R.style.Dialog_Transparent, hasBar, semipermeable);
    }


    public LoadingDialog(Context context, int themeResId, boolean hasBar, boolean semipermeable) {
        super(context, themeResId);
        hasActionBar = hasBar;
        isSemipermeable = semipermeable;
    }

    public static LoadingDialog show(Context context) {
        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.show();
        return loadingDialog;
    }

    public static LoadingDialog show(Context context, OnDismissListener listener) {
        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.show();
        loadingDialog.setOnDismissListener(listener);
        return loadingDialog;
    }

    public static LoadingDialog show(Context context, boolean hasBar) {
        LoadingDialog loadingDialog = new LoadingDialog(context, hasBar, true);
        loadingDialog.show();
        return loadingDialog;
    }

    public static LoadingDialog show(Context context, boolean hasBar, boolean semipermeable) {
        LoadingDialog loadingDialog = new LoadingDialog(context, hasBar, semipermeable);
        loadingDialog.show();
        return loadingDialog;
    }

    public static LoadingDialog show(Context context, String loadingText) {
        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.setLoadingText(loadingText);
        loadingDialog.show();
        return loadingDialog;
    }

    public static LoadingDialog show(Context context, @StringRes int loadingText) {
        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.setLoadingText(loadingText);
        loadingDialog.show();
        return loadingDialog;
    }

    public static LoadingDialog showSystom(Context context, @StringRes int loadingText) {
        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.setLoadingText(loadingText);
        loadingDialog.getWindow().setType(LayoutParams.TYPE_TOAST);
        loadingDialog.show();
        return loadingDialog;
    }

    public static void dismiss(LoadingDialog dialog) {
        if (dialog != null) {
            Message message = new Message();
            message.obj = dialog;
            message.what = HANDLE_DISMISS;
            mHandler.sendMessageDelayed(message, DISMISS_DELAY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("dialog_loading");// for ddms show
        setContentView(R.layout.dialog_loading);
        ImageView photo = (ImageView) findViewById(R.id.photo);
        View semipermeable = findViewById(R.id.semipermeable_bg);
        mContent = (LinearLayout) findViewById(R.id.content);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContent.getLayoutParams();
        if (!hasActionBar) {
            lp.topMargin = 0;
        }

        if (isSemipermeable) {
            mContent.setBackgroundResource(android.R.color.transparent);
            semipermeable.setBackgroundResource(android.R.color.transparent);
            photo.setBackgroundResource(R.drawable.pilfe_loading_bg);

        }

        GlideUtils.load(getContext(), R.drawable.default_loading, photo, false);

    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        final int screen[] = Utils.getScreenSize(getContext());
        lp.width = screen[0];
        lp.height = screen[1];

        window.setWindowAnimations(R.style.AlphaAnimation);
        window.setAttributes(lp);
    }

    public void setLoadingText(CharSequence charSequence) {

    }

    public void setLoadingText(@StringRes int text) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }
}
