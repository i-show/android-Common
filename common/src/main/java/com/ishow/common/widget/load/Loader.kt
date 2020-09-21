package com.ishow.common.widget.load

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.ishow.common.app.provider.InitCommonProvider
import com.ishow.common.widget.load.status.ALoadStatus
import com.ishow.common.widget.load.status.EmptyLoadStatus
import com.ishow.common.widget.load.target.ITarget

/**
 * Created by yuhaiyang on 2020/9/15.
 * 加载的工具
 */

class Loader internal constructor() {
    private lateinit var loadLayout: LoadLayout
    private var emptyText: HashMap<Int, String> = hashMapOf()
    private var emptyIcon: HashMap<Int, Int> = hashMapOf()

    private var errorText: HashMap<Int, String> = hashMapOf()
    private var errorIcon: HashMap<Int, Int> = hashMapOf()

    private var loadingText: HashMap<Int, String> = hashMapOf()
    private var loadingIcon: HashMap<Int, Int> = hashMapOf()

    private var successText: HashMap<Int, String> = hashMapOf()
    private var successIcon: HashMap<Int, Int> = hashMapOf()
    private var typeMap = hashMapOf<Type, ALoadStatus>()

    /**
     * 展示状态
     */
    fun show(type: Type) {
        loadLayout.showStatus(typeMap[type]) { view ->
            when (type) {
                Type.Empty -> {
                    emptyText.forEach { (key, value) -> setText(view, key, value) }
                    emptyIcon.forEach { (key, value) -> setImage(view, key, value) }
                }
                Type.Loading -> {
                    loadingText.forEach { (key, value) -> setText(view, key, value) }
                    loadingIcon.forEach { (key, value) -> setImage(view, key, value) }
                }
                Type.Error -> {
                    errorText.forEach { (key, value) -> setText(view, key, value) }
                    errorIcon.forEach { (key, value) -> setImage(view, key, value) }
                }
                Type.Success -> {
                    successText.forEach { (key, value) -> setText(view, key, value) }
                    successIcon.forEach { (key, value) -> setImage(view, key, value) }
                }
                Type.Custom -> {
                }
            }

        }
    }

    private fun setText(container: View, viewId: Int, text: String) {
        val textView: View? = container.findViewById(viewId)
        if (textView is TextView) textView.text = text
    }

    private fun setImage(container: View, viewId: Int, imageRes: Int) {
        val imageView: View? = container.findViewById(viewId)
        if (imageView is ImageView) imageView.setImageResource(imageRes)
    }

    /**
     * 隐藏
     */
    fun dismiss() {

    }

    fun copyBuilder(target: ITarget): Builder {
        val builder = Builder(target)
        builder.statusMap = typeMap
        return builder
    }


    open class Builder internal constructor(protected var target: ITarget) {
        private val context = InitCommonProvider.app

        internal var statusMap = hashMapOf<Type, ALoadStatus>()
        internal val emptyText: HashMap<Int, String> = hashMapOf()
        internal var emptyIcon: HashMap<Int, Int> = hashMapOf()

        internal var errorText: HashMap<Int, String> = hashMapOf()
        internal var errorIcon: HashMap<Int, Int> = hashMapOf()

        internal var loadingText: HashMap<Int, String> = hashMapOf()
        internal var loadingIcon: HashMap<Int, Int> = hashMapOf()

        internal var successText: HashMap<Int, String> = hashMapOf()
        internal var successIcon: HashMap<Int, Int> = hashMapOf()

        fun emptyText(@IdRes viewId: Int, @StringRes resId: Int): Builder {
            emptyText[viewId] = context.getString(resId)
            return this@Builder
        }

        fun emptyText(@IdRes viewId: Int, text: String): Builder {
            emptyText[viewId] = text
            return this@Builder
        }

        fun emptyImage(@IdRes viewId: Int, @DrawableRes resId: Int): Builder {
            emptyIcon[viewId] = resId
            return this@Builder
        }

        fun errorText(@IdRes viewId: Int, @StringRes resId: Int): Builder {
            errorText[viewId] = context.getString(resId)
            return this@Builder
        }

        fun errorText(@IdRes viewId: Int, text: String): Builder {
            errorText[viewId] = text
            return this@Builder
        }

        fun errorImage(@IdRes viewId: Int, @DrawableRes resId: Int): Builder {
            errorIcon[viewId] = resId
            return this@Builder
        }

        fun successText(@IdRes viewId: Int, @StringRes resId: Int): Builder {
            successText[viewId] = context.getString(resId)
            return this@Builder
        }

        fun successText(@IdRes viewId: Int, text: String): Builder {
            successText[viewId] = text
            return this@Builder
        }

        fun successImage(@IdRes viewId: Int, @DrawableRes resId: Int): Builder {
            successIcon[viewId] = resId
            return this@Builder
        }

        fun loadingText(@IdRes viewId: Int, @StringRes resId: Int): Builder {
            loadingText[viewId] = context.getString(resId)
            return this@Builder
        }

        fun loadingText(@IdRes viewId: Int, text: String): Builder {
            loadingText[viewId] = text
            return this@Builder
        }

        fun loadingImage(@IdRes viewId: Int, @DrawableRes resId: Int): Builder {
            loadingIcon[viewId] = resId
            return this@Builder
        }

        fun build(): Loader {
            val loader = Loader()
            loader.typeMap = statusMap
            loader.emptyText = emptyText
            loader.emptyIcon = emptyIcon
            loader.errorText = errorText
            loader.errorIcon = errorIcon
            loader.loadingText = loadingText
            loader.loadingIcon = loadingIcon
            loader.successText = successText
            loader.successIcon = successIcon

            loader.loadLayout = target.replace(loader)
            return loader
        }
    }

    class NewBuilder internal constructor(target: ITarget) : Builder(target) {

        fun empty(loaderStatus: EmptyLoadStatus): NewBuilder {
            statusMap[loaderStatus.type] = loaderStatus
            return this@NewBuilder
        }

        fun error(loaderStatus: EmptyLoadStatus): NewBuilder {
            statusMap[loaderStatus.type] = loaderStatus
            return this@NewBuilder
        }

        fun loading(loaderStatus: EmptyLoadStatus): NewBuilder {
            statusMap[loaderStatus.type] = loaderStatus
            return this@NewBuilder
        }

        fun success(loaderStatus: EmptyLoadStatus): NewBuilder {
            statusMap[loaderStatus.type] = loaderStatus
            return this@NewBuilder
        }
    }

    companion object {
        fun newBuilder(target: ITarget): NewBuilder {
            return NewBuilder(target)
        }
    }

    /**
     * Loader的状态
     */
    enum class Type {
        Loading,
        Empty,
        Error,
        Success,
        Custom
    }
}