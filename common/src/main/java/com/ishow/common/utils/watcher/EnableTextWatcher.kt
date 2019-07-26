package com.ishow.common.utils.watcher

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.ishow.common.utils.StringUtils
import com.ishow.common.utils.watcher.checker.EmptyChecker
import com.ishow.common.utils.watcher.checker.ITextChecker
import com.ishow.common.widget.edittext.EditTextPro

import java.util.HashMap

class EnableTextWatcher : TextWatcher {
    private var mEnableView: View? = null

    private val mViewMap = HashMap<View, ITextChecker>()


    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        check()
    }

    fun setEnableView(view: View): EnableTextWatcher {
        mEnableView = view
        view.isEnabled = false
        return this
    }

    /**
     * 添加监控View
     */
    @JvmOverloads
    fun addChecker(view: View?, checker: ITextChecker = EmptyChecker()): EnableTextWatcher {
        when (view) {
            is EditText -> {
                view.addTextChangedListener(this)
            }

            is EditTextPro -> {
                view.addInputWatcher(this)
            }
        }
        view?.let {
            mViewMap[view] = checker
        }

        return this
    }

    /**
     * 移除监听
     */
    fun removeChecker(view: View?) {
        when (view) {
            is EditText -> {
                view.removeTextChangedListener(this)
            }

            is EditTextPro -> {
                view.removeInputWatcher(this)
            }
        }

        view?.let {
            mViewMap.remove(view)
        }
    }

    /**
     * 检测是否可以点击
     */
    public fun check() {
        if (mEnableView == null) {
            return
        }

        var enable = true

        for ((view, checker) in mViewMap) {
            enable = when (view) {
                is EditText -> {
                    checker.check(view, view.text.toString())
                }
                is EditTextPro -> {
                    checker.check(view, view.inputText)
                }
                else -> {
                    checker.check(view, StringUtils.EMPTY)
                }
            }
            if (!enable) {
                break
            }
        }
        mEnableView?.isEnabled = enable
    }


}
