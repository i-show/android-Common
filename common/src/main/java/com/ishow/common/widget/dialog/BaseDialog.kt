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

package com.ishow.common.widget.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.KeyEvent
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ishow.common.R
import com.ishow.common.adapter.BindAdapter
import com.ishow.common.extensions.dp2px
import com.ishow.common.extensions.getDimensionPixelSize
import com.ishow.common.manager.DialogManager
import com.ishow.common.utils.DeviceUtils


/**
 * BaseDialog
 */
open class BaseDialog constructor(context: Context, theme: Int) : Dialog(context, theme), DialogInterface, DialogManager.Dismissible {
    private val mController: BaseController

    private var dismissListener: DialogManager.IDismissListener? = null

    constructor(context: Context) : this(context, R.style.Theme_Dialog) {
        setActivity(context)
    }

    init {
        setActivity(context)
        @Suppress("LeakingThis")
        mController = BaseController(getContext(), this, window!!)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mController.installContent()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (mController.onKeyDown(event)) true else super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return if (mController.onKeyUp(event)) true else super.onKeyUp(keyCode, event)
    }

    override fun dismiss() {
        super.dismiss()
        dismissListener?.onDismiss()
    }

    /**
     * ????????????????????????
     */
    fun fromBottom(bottom: Boolean) {
        mController.isFromBottom = bottom
    }

    /**
     * ??????
     */
    fun setWidthProportion(widthPro: Float) {
        mController.mWidthProportion = widthPro
    }

    override fun show() {
        val context = context
        if (context is Activity && context.isFinishing) {
            Log.i(TAG, "show:  activity is null or finishing")
            return
        }

        val window = window
        if (window == null) {
            Log.i(TAG, "show: window is null")
            return
        }
        super.show()

        val lp = window.attributes
        val width = DeviceUtils.screenSize[0]
        val height = DeviceUtils.screenSize[1]
        if (mController.isFromBottom) {
            lp.width = (width * mController.mWidthProportion).toInt()
            lp.gravity = Gravity.BOTTOM
            window.setWindowAnimations(R.style.Animation_Windows_Bottom)
        } else {
            if (width > height) {
                lp.width = height
            } else {
                lp.width = (width * mController.mWidthProportion).toInt()
            }
        }
        window.attributes = lp
    }

    /**
     * ????????????Activity
     */
    private fun setActivity(context: Context?) {
        if (context == null) {
            Log.i(TAG, "setActivity: context is null")
            return
        }

        if (context !is Activity) {
            Log.i(TAG, "setActivity: context is not activity")
            return
        }
        setOwnerActivity(context)
    }

    @Suppress("unused")
    class Builder @JvmOverloads constructor(context: Context?, private val mTheme: Int = R.style.Theme_Dialog) {
        private val mParams: BaseController.Params = BaseController.Params(ContextThemeWrapper(context, mTheme))

        val context: Context
            get() = mParams.mContext

        /**
         * ????????????
         */
        fun setTitle(@StringRes titleId: Int): Builder {
            mParams.mTitle = mParams.mContext.getText(titleId)
            return this
        }

        /**
         * ????????????
         */
        fun setTitle(title: CharSequence): Builder {
            mParams.mTitle = title
            return this
        }

        /**
         * ????????????
         */
        fun setMessage(messageId: Int): Builder {
            mParams.mMessage = mParams.mContext.getText(messageId)
            return this
        }

        /**
         * ????????????
         */
        fun setMessage(message: CharSequence): Builder {
            mParams.mMessage = message
            return this
        }

        /**
         * ??????MessageGravity
         */
        fun setMessageGravity(gravity: Int): Builder {
            mParams.mMessageGravity = gravity
            return this
        }

        /**
         * ??????Message???????????????
         */
        fun setMessageMinHeight(height: Int): Builder {
            mParams.mMessageMinHeight = height.dp2px()
            return this
        }

        /**
         * ??????Message???????????????
         */
        fun setMessageMinHeightResource(@DimenRes height: Int): Builder {
            mParams.mMessageMinHeight = context.getDimensionPixelSize(height)
            return this
        }

        /**
         * ??????Message???????????????
         */
        fun setMessageMaxLines(@IntRange(from = 0) lines: Int): Builder {
            mParams.mMessageMaxLines = lines
            return this
        }

        /**
         * ????????????????????????
         * @param listener [DialogInterface.OnClickListener] ???kotlin??????????????????
         */
        @JvmOverloads
        fun setPositiveButton(textId: Int, listener: BaseDialogClickListener? = null): Builder {
            mParams.mPositiveText = mParams.mContext.getText(textId)
            mParams.mPositiveListener = listener
            return this
        }

        /**
         * ????????????????????????
         */
        @JvmOverloads
        fun setPositiveButton(text: CharSequence, listener: BaseDialogClickListener? = null): Builder {
            mParams.mPositiveText = text
            mParams.mPositiveListener = listener
            return this
        }

        /**
         * ??????????????????
         */
        fun setPositiveButtonTextColorResource(@ColorRes color: Int): Builder {
            mParams.mPositiveTextColor = ContextCompat.getColorStateList(context, color)
            return this
        }

        /**
         * ??????????????????
         */
        fun setPositiveButtonTextColor(@ColorInt color: Int): Builder {
            mParams.mPositiveTextColor = ColorStateList.valueOf(color)
            return this
        }

        /**
         * ??????????????????
         */
        fun setPositiveButtonTextColor(colors: ColorStateList?): Builder {
            if (colors == null) {
                throw NullPointerException()
            }
            mParams.mPositiveTextColor = colors
            return this
        }

        /**
         * ??????????????????
         */
        @JvmOverloads
        fun setNegativeButton(@StringRes textId: Int, listener: BaseDialogClickListener? = null): Builder {
            mParams.mNegativeText = mParams.mContext.getText(textId)
            mParams.mNegativeListener = listener
            return this
        }

        /**
         * ??????????????????
         */
        @JvmOverloads
        fun setNegativeButton(text: CharSequence, listener: BaseDialogClickListener? = null): Builder {
            mParams.mNegativeText = text
            mParams.mNegativeListener = listener
            return this
        }


        /**
         * ??????????????????
         */
        fun setNegativeButtonTextColorResource(@ColorRes color: Int): Builder {
            mParams.mNegativeTextColor = ContextCompat.getColorStateList(context, color)
            return this
        }

        /**
         * ??????????????????
         */
        fun setNegativeButtonTextColor(@ColorInt color: Int): Builder {
            mParams.mNegativeTextColor = ColorStateList.valueOf(color)
            return this
        }

        /**
         * ??????????????????
         */
        fun setNegativeButtonTextColor(colors: ColorStateList?): Builder {
            mParams.mNegativeTextColor = colors
            return this
        }

        /**
         * ??????????????????
         */
        fun setButtonLineColor(@ColorInt color: Int): Builder {
            mParams.mButtonLineColor = color
            return this
        }

        /**
         * ???????????????????????????????????????true
         */
        fun setCancelable(cancelable: Boolean): Builder {
            mParams.mCancelable = cancelable
            return this
        }

        /**
         * ??????????????????
         */
        fun setOnCancelListener(listener: DialogInterface.OnCancelListener): Builder {
            mParams.mOnCancelListener = listener
            return this
        }

        /**
         * ??????????????????
         */
        fun setOnDismissListener(listener: DialogInterface.OnDismissListener): Builder {
            mParams.mOnDismissListener = listener
            return this
        }

        /**
         * ????????????
         */
        fun setOnKeyListener(onKeyListener: DialogInterface.OnKeyListener): Builder {
            mParams.mOnKeyListener = onKeyListener
            return this
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of the
         */
        @JvmOverloads
        fun setItems(@ArrayRes itemsId: Int, listener: BaseDialogClickListener? = null): Builder {
            mParams.mItems = mParams.mContext.resources.getTextArray(itemsId)
            mParams.mOnClickListener = listener
            return this
        }

        /**
         * Set a list of items to be displayed in the dialog as the content, you will be notified of the
         */
        @JvmOverloads
        fun setItems(items: Array<CharSequence>, listener: BaseDialogClickListener? = null): Builder {
            mParams.mItems = items
            mParams.mOnClickListener = listener
            return this
        }

        /**
         * ??????Adapter
         */
        fun setAdapter(adapter: RecyclerView.Adapter<*>): Builder {
            mParams.mAdapter = adapter
            return this
        }

        /**
         * ??????Adapter
         */
        @JvmOverloads
        fun setAdapter(adapter: BindAdapter<*>, listener: BaseDialogClickListener? = null): Builder {
            mParams.mAdapter = adapter
            mParams.mOnClickListener = listener
            return this
        }

        /**
         * ????????????????????????
         */
        fun fromBottom(bottom: Boolean): Builder {
            mParams.isFromBottom = bottom
            return this
        }

        /**
         * ??????Dialog???????????????????????????
         */
        fun setWidthProportion(@FloatRange(from = 0.1, to = 1.0) proportion: Float): Builder {
            mParams.mWidthProportion = proportion
            return this
        }

        fun create(): BaseDialog {
            val dialog = BaseDialog(mParams.mContext, mTheme)
            dialog.setCancelable(mParams.mCancelable)
            dialog.setCanceledOnTouchOutside(mParams.mCancelable)
            dialog.setOnCancelListener(mParams.mOnCancelListener)
            dialog.setOnDismissListener(mParams.mOnDismissListener)
            dialog.setOnKeyListener(mParams.mOnKeyListener)
            mParams.apply(dialog.mController)
            return dialog
        }

        fun show(): BaseDialog {
            val dialog = create()
            dialog.show()
            return dialog
        }
    }

    companion object {
        private const val TAG = "BaseDialog"
    }

    override fun addDismissListener(listener: DialogManager.IDismissListener) {
        dismissListener = listener
    }
}

typealias BaseDialogClickListener = (dialog: DialogInterface, which: Int) -> Unit
