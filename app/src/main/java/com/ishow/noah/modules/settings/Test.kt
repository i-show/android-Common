package com.ishow.noah.modules.settings

import android.content.Context
import android.content.DialogInterface

import com.ishow.common.utils.ToastUtils
import com.ishow.common.widget.dialog.BaseDialog
import com.ishow.noah.R

class Test {
    fun test(context: Context) {
        BaseDialog.Builder(context)
                .setMessage("Hello")
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes) { _, _ ->ToastUtils.show(context, "hello") }
                .show()
    }
}
