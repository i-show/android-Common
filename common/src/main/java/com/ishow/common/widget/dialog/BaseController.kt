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
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.text.method.LinkMovementMethod
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ishow.common.BR
import com.ishow.common.R
import com.ishow.common.adapter.BindAdapter
import com.ishow.common.widget.recyclerview.RecyclerViewPro
import com.ishow.common.widget.recyclerview.itemdecoration.ColorDecoration
import java.lang.ref.WeakReference
import kotlin.math.max


class BaseController(
    private val mContext: Context,
    private val mDialogInterface: DialogInterface,
    private val mWindow: Window
) {

    private val mDialogLayout: Int
    internal var isFromBottom: Boolean = false
    internal var mWidthProportion: Float = 0F

    private var mTitleView: TextView? = null
    private var mTitle: CharSequence? = null

    private var mScrollView: ScrollView? = null
    private var mMessageView: TextView? = null
    private var mMessage: CharSequence? = null
    private var mMessageGravity: Int = Gravity.CENTER
    private var mMessageMinHeight: Int = 0
    private var mMessageMaxLines: Int = Int.MAX_VALUE

    private var mListView: RecyclerViewPro? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private val mListLayout: Int
    private val mListItemLayout: Int
    /**
     * ??????????????????
     */
    private var mPositiveButton: Button? = null
    private var mPositiveText: CharSequence? = null
    private var mPositiveTextColor: ColorStateList? = null
    private var mPositiveMessage: Message? = null
    /**
     * ??????????????????
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
     * ????????????????????????
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
     * [BaseDialog.onCreate]?????????
     */
    internal fun installContent() {
        mWindow.requestFeature(Window.FEATURE_NO_TITLE)
        mWindow.setContentView(mDialogLayout)
        setupView()
    }

    private fun setupView() {
        if (isFromBottom) {
            //  ??????????????????????????????????????????padding
            val parent = mWindow.findViewById<View>(R.id.parentPanel)
            parent.setPadding(0, 0, 0, 0)
        }

        val topPanel: View = mWindow.findViewById(R.id.topPanel)
        val hasTitle = setupTitle(topPanel)

        val contentPanel: ViewGroup = mWindow.findViewById(R.id.contentPanel)
        setupList()
        setupContent(contentPanel, hasTitle)

        val buttonPanel = mWindow.findViewById<View>(R.id.buttonPanel)
        setupButtons(buttonPanel)
    }

    private fun setupTitle(topPanel: View): Boolean {
        return if (mTitle.isNullOrEmpty()) {
            topPanel.visibility = View.GONE
            false
        } else {
            mTitleView = mWindow.findViewById(R.id.alertTitle)
            mTitleView?.text = mTitle
            true
        }
    }


    private fun setupContent(contentPanel: ViewGroup, hasTitle: Boolean) {
        mScrollView = mWindow.findViewById(R.id.scrollView)
        mScrollView?.isFocusable = false

        mMessageView = mWindow.findViewById(R.id.message)

        if (mMessage != null) {
            mMessageView?.text = mMessage
            mMessageView?.gravity = mMessageGravity
            mMessageView?.maxLines = mMessageMaxLines
            mMessageView?.minHeight = mMessageMinHeight

            mMessageView?.highlightColor = Color.TRANSPARENT
            mMessageView?.movementMethod = LinkMovementMethod.getInstance()

            if (!hasTitle) {
                val paddingTop = (max(mMessageView!!.paddingStart, mMessageView!!.paddingEnd) * 0.8f).toInt()
                mMessageView?.setPadding(
                    mMessageView!!.paddingStart,
                    paddingTop,
                    mMessageView!!.paddingEnd,
                    mMessageView!!.paddingBottom
                )
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

    private fun setupList() {
        mAdapter?.let { mListView?.adapter = it }
    }

    /**
     * ??????Dialog???????????????
     */
    private fun setFromBottom(bottom: Boolean?) {
        bottom?.let { isFromBottom = it }
    }

    private fun setWidthProportion(value: Float?) {
        value?.let { mWidthProportion = it }
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
     * ?????????????????????Gravity
     *
     * @param gravity [Gravity.CENTER]
     */
    private fun setMessageGravity(gravity: Int) {
        mMessageGravity = gravity
        mMessageView?.gravity = mMessageGravity
    }

    /**
     * ???????????????????????????
     *
     * @param gravity [Gravity.CENTER]
     */
    private fun setMessageMinHeight(minHeight: Int) {
        mMessageMinHeight = minHeight
        mMessageView?.minHeight = minHeight
    }


    /**
     * ???????????????????????????
     *
     * @param gravity [Gravity.CENTER]
     */
    private fun setMessageMaxLines(maxLines: Int) {
        mMessageMaxLines = maxLines
        mMessageView?.maxLines = maxLines
    }

    /**
     * ?????????????????????
     */
    fun setFromBottom(fromBottom: Boolean) {
        isFromBottom = fromBottom
    }

    /**
     * ????????????
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
     * ????????????????????????
     */
    private fun setPositiveButtonTextColor(colors: ColorStateList?) {
        mPositiveTextColor = colors
        if (mPositiveButton != null && mPositiveTextColor != null) {
            mPositiveButton?.setTextColor(mPositiveTextColor)
        }
    }

    /**
     * ????????????????????????
     */
    private fun setNegativeButtonTextColor(colors: ColorStateList?) {
        mNegativeTextColor = colors
        if (mNegativeButton != null && mNegativeTextColor != null) {
            mNegativeButton?.setTextColor(mNegativeTextColor)
        }
    }

    /**
     * ??????ButtonLine?????????
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
        private val mInflater: LayoutInflater

        internal var mOnCancelListener: DialogInterface.OnCancelListener? = null
        internal var mOnDismissListener: DialogInterface.OnDismissListener? = null
        internal var mOnKeyListener: DialogInterface.OnKeyListener? = null

        internal var isFromBottom: Boolean? = null
        internal var mWidthProportion: Float? = null

        internal var mTitle: CharSequence? = null
        internal var mMessage: CharSequence? = null
        internal var mMessageGravity: Int = Gravity.CENTER
        internal var mMessageMinHeight: Int = 0
        internal var mMessageMaxLines: Int = 0

        internal var mPositiveText: CharSequence? = null
        internal var mPositiveTextColor: ColorStateList? = null
        internal var mPositiveListener: BaseDialogClickListener? = null

        internal var mNegativeText: CharSequence? = null
        internal var mNegativeTextColor: ColorStateList? = null
        internal var mNegativeListener: BaseDialogClickListener? = null

        internal var mButtonLineColor: Int? = null

        internal var mAdapter: RecyclerView.Adapter<*>? = null
        internal var mOnClickListener: BaseDialogClickListener? = null
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
            val listView = mInflater.inflate(dialog.mListLayout, null) as RecyclerViewPro
            listView.addItemDecoration(ColorDecoration())
            when (mAdapter) {
                null -> {
                    val adapter = BindAdapter<String>()
                    adapter.setOnItemClickListener { position ->
                        mOnClickListener?.let { it(dialog.mDialogInterface, position) }
                        dialog.mDialogInterface.dismiss()
                    }
                    adapter.addLayout(BR.text, dialog.mListItemLayout)
                    @Suppress("UNCHECKED_CAST")
                    adapter.data = mItems?.toList() as MutableList<String>
                    dialog.mAdapter = adapter
                }
                is BindAdapter<*> -> {
                    val adapter: BindAdapter<*> = mAdapter as BindAdapter<*>
                    adapter.setOnItemClickListener { position ->
                        mOnClickListener?.let { it(dialog.mDialogInterface, position) }
                        dialog.mDialogInterface.dismiss()
                    }
                    dialog.mAdapter = adapter
                }
                else -> dialog.mAdapter = mAdapter
            }
            dialog.mListView = listView
        }
    }

    companion object {
        /**
         * ????????????
         */
        private const val DEFAULT_WIDTH_PRO_PORTION = 0.85f

        private const val BIT_BUTTON_POSITIVE = 1
        private const val BIT_BUTTON_NEGATIVE = 2
    }

}
