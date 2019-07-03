/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.common.widget.loading

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.Window
import android.view.WindowManager
import com.ishow.common.R
import com.ishow.common.utils.DeviceUtils


class LoadingDialog private constructor(context: Context, themeResId: Int = R.style.Theme_Dialog_Semipermeable) : Dialog(context, themeResId) {

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setTitle("dialog_loading")
        setContentView(R.layout.dialog_loading)
    }

    override fun show() {
        super.show()
        val window = window ?: return

        val lp = window.attributes
        val screen = DeviceUtils.screenSize
        lp.width = screen[0]
        lp.height = screen[1]

        window.setWindowAnimations(R.style.Animation_Windows_Alpha)
        window.attributes = lp
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss()
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {

        private fun show(context: Context): LoadingDialog? {
            if (context !is Activity) {
                return null
            }

            if (context.isFinishing) {
                return null
            }

            val loadingDialog = LoadingDialog(context)
            loadingDialog.show()
            return loadingDialog
        }

        @JvmStatic
        fun show(context: Context, dialog: LoadingDialog?): LoadingDialog? {
            if (dialog == null) {
                return show(context)
            }
            if (!dialog.isShowing) {
                dialog.show()
            }
            return dialog
        }

        @JvmStatic
        fun dismiss(dialog: LoadingDialog?) {
            if (dialog == null) {
                return
            }
            dialog.dismiss()
        }
    }
}
