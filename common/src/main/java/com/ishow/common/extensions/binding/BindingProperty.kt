package com.ishow.common.extensions.binding

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Method
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Binding实现的抽象类
 */
abstract class BindingProperty<T, BINDING : ViewBinding>(private val classes: Class<BINDING>, val inflater: LayoutInflater) :
    ReadOnlyProperty<T, BINDING> {

    /**
     * binding的实现类
     */
    protected var binding: BINDING? = null

    /**
     * 通过反射来获取 ViewBinding.inflate 方法
     */
    private val inflateMethod = getMethod()

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: T, property: KProperty<*>): BINDING {
        var viewBinding = binding
        if (viewBinding != null) {
            return viewBinding
        }
        viewBinding = inflateMethod.invoke(null, inflater) as BINDING
        binding = viewBinding
        binding(thisRef, viewBinding)
        return viewBinding
    }

    abstract fun binding(thisRef: T, viewBinding: BINDING)

    /**
     * 获取实现方法
     */
    fun getMethod(): Method {
        return classes.getMethod("inflate", LayoutInflater::class.java)
    }

    protected fun destroyed() {
        binding = null
    }
}