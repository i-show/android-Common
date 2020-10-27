package com.ishow.common.widget.load.status

import android.content.Context
import android.view.View
import com.ishow.common.widget.load.Loader
import com.ishow.common.widget.load.OnLoaderClickBlock
import java.io.Serializable

/**
 * Created by yuhaiyang on 2020/9/15.
 * 加载StatusView
 */
abstract class ALoadStatus(val type: Loader.Type) : Serializable {
    private var rootView: View? = null

    var block: OnLoaderClickBlock? = null

    /**
     * 获取当前的Layout
     */
    protected abstract fun getLayout(): Int

    /**
     * View已经创建
     */
    protected open fun onViewCreate(context: Context, view: View) {}


    fun buildView(context: Context): View {
        var view = rootView
        if (view != null) {
            return view
        }

        view = View.inflate(context, getLayout(), null)
        onViewCreate(context, view)
        rootView = view
        return view
    }

    protected fun notifyClick(view: View) {
        block?.invoke(type, view)
    }

    /**
     * Called when the rootView of Callback is attached to its LoadLayout.
     */
    open fun onAttach() {}

    /**
     * Called when the rootView of Callback is removed from its LoadLayout.
     */
    open fun onDetach() {}
}