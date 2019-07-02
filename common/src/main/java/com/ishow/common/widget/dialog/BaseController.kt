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

import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Handler
import android.os.Message
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ishow.common.R
import com.ishow.common.widget.recyclerview.RecyclerViewPro
import java.lang.ref.WeakReference
import kotlin.math.max


class BaseController(private val mContext: Context, private val mDialogInterface: DialogInterface, private val mWindow: Window) {

    private val mDialogLayout: Int
    internal var isFromBottom: Boolean = false
    internal var mWidthProportion: Float = 0F

    private var mTitleView: TextView? = null
    private var mTitle: CharSequence? = null

    private var mScrollView: ScrollView? = null
    private var mMessageView: TextView? = null
    private var mMessage: CharSequence? = null
    private var mMessageGravity: Int = Gravity.CENTER

    private var mListView: RecyclerViewPro? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private val mListLayout: Int
    private val mListItemLayout: Int
    /**
     * 右侧确定按钮
     */
    private var mPositiveButton: Button? = null
    private var mPositiveText: CharSequence? = null
    private var mPositiveTextColor: ColorStateList? = null
    private var mPositiveMessage: Message? = null
    /**
     * 左侧取消按钮
     */
    private var mNegativeButton: Button? = null
    private var mNegativeText: CharSequence? = null
    private var mNegativeTextColor: ColorStateList? = null
    private var mNegativeMessage: Message? = null

    private var mButtonLineColor: Int? = null

    private val mHandler: Handler = ButtonHandler(mDialogInterface)

    private val mButtonHandler = View.OnClickListener { v ->
        var m: Message? = null
        when (v) {
            mPositiveButton -> {
                mPositiveMessage?.let {
                    m = Message.obtain(mPositiveMessage)
                }
            }
            mNegativeButton -> {
                mNegativeMessage?.let {
                    m = Message.obtain(mNegativeMessage)
                }
            }
        }
        m?.sendToTarget()
        mHandler.obtainMessage(ButtonHandler.MSG_DISMISS_DIALOG, mDialogInterface).sendToTarget()
    }

    /**
     * 获取默认线的颜色
     */
    private val defaultLineColor: Int
        get() = ContextCompat.getColor(mContext, R.color.line)


    init {
        val a = mContext.obtainStyledAttributes(null, R.styleable.BaseDialog, R.attr.dialogStyle, 0)
        mDialogLayout = a.getResourceId(R.styleable.BaseDialog_dialogMainLayout, R.layout.dialog)
        mListLayout = a.getResourceId(R.styleable.BaseDialog_dialogListLayout, R.layout.dialog_select)
        mListItemLayout = a.getResourceId(R.styleable.BaseDialog_dialogListItem, R.layout.dialog_select_item)
        isFromBottom = a.getBoolean(R.styleable.BaseDialog_fromBottom, false)
        mWidthProportion = a.getFloat(R.styleable.BaseDialog_widthProportion, DEFAULT_WIDTH_PRO_PORTION)
        a.recycle()
    }

    /**
     * [BaseDialog.onCreate]中调用
     */
    internal fun installContent() {
        mWindow.requestFeature(Window.FEATURE_NO_TITLE)
        mWindow.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        mWindow.setContentView(mDialogLayout)
        setupView()
    }


    private fun setupView() {
        if (isFromBottom) {
            //  如果从底部弹出那么不需要设置padding
            val parent = mWindow.findViewById<View>(R.id.parentPanel)
            parent.setPadding(0, 0, 0, 0)
        }

        val topPanel = mWindow.findViewById<LinearLayout>(R.id.topPanel)
        val hasTitle = setupTitle(topPanel)

        val contentPanel = mWindow.findViewById<LinearLayout>(R.id.contentPanel)
        setupContent(contentPanel, hasTitle)

        val buttonPanel = mWindow.findViewById<View>(R.id.buttonPanel)
        setupButtons(buttonPanel)

        setupList(hasTitle)
    }

    private fun setupTitle(topPanel: LinearLayout): Boolean {
        return if (mTitle.isNullOrEmpty()) {
            topPanel.visibility = View.GONE
            false
        } else {
            mTitleView = mWindow.findViewById(R.id.alertTitle)
            mTitleView?.text = mTitle
            true
        }
    }


    private fun setupContent(contentPanel: LinearLayout, hasTitle: Boolean) {
        mScrollView = mWindow.findViewById(R.id.scrollView)
        mScrollView?.isFocusable = false

        mMessageView = mWindow.findViewById(R.id.message)

        if (mMessage != null) {
            mMessageView?.text = mMessage
            mMessageView?.gravity = mMessageGravity
            if (!hasTitle) {
                val paddingTop = (max(mMessageView!!.paddingStart, mMessageView!!.paddingEnd) * 0.8f).toInt()
                mMessageView?.setPadding(mMessageView!!.paddingStart, paddingTop, mMessageView!!.paddingEnd, mMessageView!!.paddingBottom)
            }
        } else {
            mMessageView?.visibility = View.GONE
            mScrollView?.removeView(mMessageView)

            if (mListView != null) {
                contentPanel.removeView(mScrollView)
                contentPanel.addView(mListView, LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
            } else {
                contentPanel.visibility = View.GONE
            }
        }
    }

    private fun setupButtons(buttonPanel: View) {
        var whichButtons = 0
        val topLine = mWindow.findViewById<View>(R.id.button_top_line)
        val middleLine = mWindow.findViewById<View>(R.id.button_middle_line)

        mPositiveButton = mWindow.findViewById(R.id.positive)
        mPositiveButton?.setOnClickListener(mButtonHandler)

        if (mPositiveText.isNullOrEmpty()) {
            mPositiveButton?.visibility = View.GONE
        } else {
            mPositiveButton?.visibility = View.VISIBLE
            mPositiveButton?.text = mPositiveText
            mPositiveTextColor?.let {
                mPositiveButton?.setTextColor(mPositiveTextColor)
            }
            whichButtons = whichButtons or BIT_BUTTON_POSITIVE
        }

        mNegativeButton = mWindow.findViewById(R.id.negative)
        mNegativeButton?.setOnClickListener(mButtonHandler)

        if (mNegativeText.isNullOrEmpty()) {
            mNegativeButton?.visibility = View.GONE
        } else {
            mNegativeButton?.visibility = View.VISIBLE
            mNegativeButton?.text = mNegativeText
            mNegativeTextColor?.let {
                mNegativeButton?.setTextColor(mNegativeTextColor)
            }
            whichButtons = whichButtons or BIT_BUTTON_NEGATIVE
        }

        // get Res ID
        val a = mContext.obtainStyledAttributes(null, R.styleable.BaseDialogButtons, R.attr.dialogButtonStyle, 0)
        val leftBg = a.getResourceId(R.styleable.BaseDialogButtons_leftBackground, 0)
        val rightBg = a.getResourceId(R.styleable.BaseDialogButtons_rightBackground, 0)
        val wholeBg = a.getResourceId(R.styleable.BaseDialogButtons_wholeBackground, 0)
        val lineColor = a.getColor(R.styleable.BaseDialogButtons_lineColor, defaultLineColor)
        a.recycle()
        /*
         * NEGATIVE    POSITIVE
         *   BU2          BU1
         *    2            1
         *   10            1
         */
        //  3 = 011 7 = 111
        when (whichButtons) {
            1 -> mPositiveButton?.setBackgroundResource(wholeBg)
            2 -> mNegativeButton?.setBackgroundResource(wholeBg)
            3 -> {
                mPositiveButton?.setBackgroundResource(rightBg)
                mNegativeButton?.setBackgroundResource(leftBg)
            }
        }

        mButtonLineColor = lineColor

        topLine?.let {
            topLine.setBackgroundColor(mButtonLineColor!!)
            topLine.visibility = if (whichButtons == 0) View.GONE else View.VISIBLE
        }
        middleLine?.let {
            middleLine.setBackgroundColor(mButtonLineColor!!)
            middleLine.visibility = if (whichButtons == 3) View.VISIBLE else View.GONE
        }

        buttonPanel.visibility = if (whichButtons == 0) View.GONE else View.VISIBLE
    }

    private fun setupList(hasTitle: Boolean) {
        if (mListView != null && mAdapter != null) {
            mListView!!.adapter = mAdapter
        }
    }

    /**
     * 设置Dialog从底部弹出
     */
    private fun setFromBottom(bottom: Boolean?) {
        bottom?.let {
            isFromBottom = bottom
        }
    }

    private fun setWidthProportion(value: Float?) {
        value?.let {
            mWidthProportion = it
        }
    }

    fun setTitle(title: CharSequence?) {
        mTitle = title
        mTitleView?.text = title
    }

    fun setMessage(message: CharSequence?) {
        mMessage = message
        mMessageView?.text = message
    }

    /**
     * 设置文本内容的Gravity
     *
     * @param gravity [Gravity.CENTER]
     */
    private fun setMessageGravity(gravity: Int) {
        mMessageGravity = gravity
        mMessageView?.gravity = mMessageGravity
    }

    /**
     * 是否从底部弹出
     */
    fun setFromBottom(fromBottom: Boolean) {
        isFromBottom = fromBottom
    }

    /**
     * 设置按钮
     */
    private fun setButton(whichButton: Int, text: CharSequence?, listener: ((DialogInterface, Int) -> Unit)?) {
        var msg: Message? = null
        listener?.let {
            msg = mHandler.obtainMessage(whichButton, listener)
        }

        when (whichButton) {
            DialogInterface.BUTTON_POSITIVE -> {
                mPositiveText = text
                mPositiveMessage = msg
            }
            DialogInterface.BUTTON_NEGATIVE -> {
                mNegativeText = text
                mNegativeMessage = msg
            }
        }
    }

    /**
     * 设置右侧字体颜色
     */
    private fun setPositiveButtonTextColor(colors: ColorStateList?) {
        mPositiveTextColor = colors
        if (mPositiveButton != null && mPositiveTextColor != null) {
            mPositiveButton?.setTextColor(mPositiveTextColor)
        }
    }

    /**
     * 设置左侧字体颜色
     */
    private fun setNegativeButtonTextColor(colors: ColorStateList?) {
        mNegativeTextColor = colors
        if (mNegativeButton != null && mNegativeTextColor != null) {
            mNegativeButton?.setTextColor(mNegativeTextColor)
        }
    }

    /**
     * 设置ButtonLine的颜色
     */
    private fun setButtonLineColor(color: Int?) {
        mButtonLineColor = color
    }

    internal fun onKeyDown(event: KeyEvent): Boolean {
        return mScrollView != null && mScrollView!!.executeKeyEvent(event)
    }

    internal fun onKeyUp(event: KeyEvent): Boolean {
        return mScrollView != null && mScrollView!!.executeKeyEvent(event)
    }

    private class ButtonHandler constructor(dialogInterface: DialogInterface) : Handler() {
        private val dialogReference = WeakReference(dialogInterface)
        @Suppress("UNCHECKED_CAST")
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                DialogInterface.BUTTON_POSITIVE,
                DialogInterface.BUTTON_NEGATIVE -> {
                    val listener = msg.obj as ((DialogInterface?, Int) -> Unit)
                    listener(dialogReference.get(), msg.what)
                }
                MSG_DISMISS_DIALOG -> {
                    (msg.obj as DialogInterface).dismiss()
                }
            }
        }

        companion object {
            const val MSG_DISMISS_DIALOG = 1
        }
    }


    class Params internal constructor(val mContext: Context) {
        internal val mInflater: LayoutInflater

        internal var mOnCancelListener: DialogInterface.OnCancelListener? = null
        internal var mOnDismissListener: DialogInterface.OnDismissListener? = null
        internal var mOnKeyListener: DialogInterface.OnKeyListener? = null

        internal var isFromBottom: Boolean? = null
        internal var mWidthProportion: Float? = null

        internal var mTitle: CharSequence? = null
        internal var mMessage: CharSequence? = null
        internal var mMessageGravity: Int = Gravity.CENTER

        internal var mPositiveText: CharSequence? = null
        internal var mPositiveTextColor: ColorStateList? = null
        internal var mPositiveListener: ((DialogInterface, Int) -> Unit)? = null

        internal var mNegativeText: CharSequence? = null
        internal var mNegativeTextColor: ColorStateList? = null
        internal var mNegativeListener: ((DialogInterface, Int) -> Unit)? = null

        internal var mButtonLineColor: Int? = null

        internal var mAdapter: RecyclerView.Adapter<*>? = null
        internal var mOnClickListener: ((DialogInterface, Int) -> Unit)? = null
        internal var mItems: Array<CharSequence>? = null
        internal var mCancelable: Boolean = false

        init {
            mCancelable = true
            mInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        internal fun apply(controller: BaseController) {
            controller.setFromBottom(isFromBottom)
            controller.setWidthProportion(mWidthProportion)

            controller.setTitle(mTitle)
            controller.setMessage(mMessage)
            controller.setMessageGravity(mMessageGravity)

            controller.setPositiveButtonTextColor(mPositiveTextColor)
            controller.setButton(DialogInterface.BUTTON_POSITIVE, mPositiveText, mPositiveListener)

            controller.setNegativeButtonTextColor(mNegativeTextColor)
            controller.setButton(DialogInterface.BUTTON_NEGATIVE, mNegativeText, mNegativeListener)
            controller.setButtonLineColor(mButtonLineColor)
            // For a list, the client can either supply an array of items or an
            // adapter or a cursor
            if (mItems != null || mAdapter != null) {
                createListView(controller)
            }
        }

        private fun createListView(dialog: BaseController) {

            /*

              val listView = mInflater.inflate(dialog.mListLayout, null) as ListViewPro
            val adapter: Adapter<*>? =


                    /* Don't directly set the adapter on the ListView as we might
                     * want to add a footer to the ListView later.
                     */
                    dialog.mAdapter = adapter
            dialog.mListView = listView
            */
        }
    }

    companion object {
        /**
         * 默认宽度
         */
        private const val DEFAULT_WIDTH_PRO_PORTION = 0.8f

        private const val BIT_BUTTON_POSITIVE = 1
        private const val BIT_BUTTON_NEGATIVE = 2
        /**
         * 当前包含的View是否可以输入
         */
        private fun canTextInput(v: View): Boolean {
            if (v.onCheckIsTextEditor()) {
                return true
            }

            if (v !is ViewGroup) {
                return false
            }


            var child: View
            var i = v.childCount
            while (i > 0) {
                i--
                child = v.getChildAt(i)
                if (canTextInput(child)) {
                    return true
                }
            }
            return false
        }
    }

}
