package com.ishow.common.utils

import java.lang.reflect.InvocationTargetException

/**
 * Created by yuhaiyang on 2019-09-02.
 */
@Suppress("unused")
object ReflectionUtils {
    /**
     * first of all, int.class != Integer.class. but int.class == Integer.TYPE
     * the is similarly to other primitive type, such as float, boolean, double, byte, char, short, long.
     *
     *
     * ---------------------------------
     * int a = 12;
     * Object[] args = new Object[]{a};
     * ---------------------------------
     *
     *
     * in last code block, args[0].class is Integer.class because JVM cast a to Integer type in Java.
     * in that case, this getClassTypes method will return wrong so if args contains primitive type(like int, boolean) you should not use this method.
     *
     * @param args 参数
     * @return the class type of args
     */
    fun getClassTypes(args: Array<Any>?): Array<Class<*>?>? {
        if (null == args || args.isEmpty()) {
            return null
        }
        val argClasses = arrayOfNulls<Class<*>>(args.size)
        for (i in args.indices) {
            argClasses[i] = args[i].javaClass
        }
        return argClasses
    }

    /**
     * 根据类名和构造函数的参数新建一个对象实例，只能通过 public 的构造函数新建实例。
     *
     * @param className  类名
     * @param args       构造函数的参数
     * @param argClasses 构造函数的参数的 class type
     * @return 该类的对象实例
     */
    fun newInstance(className: String, args: Array<Any>, argClasses: Array<Class<*>>): Any? {
        var instance: Any? = null
        try {
            val classObj = Class.forName(className)
            val cons = classObj.getConstructor(*argClasses)
            instance = cons.newInstance(*args)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return instance
    }

    /**
     * 获得类的静态属性值，不管是 public 或者 private 的都可以。
     *
     * @param className 类名
     * @param fieldName 属性名
     * @return 该类的静态属性值
     */
    fun getStaticField(className: String, fieldName: String): Any? {
        var fieldValue: Any? = null
        try {
            val classObj = Class.forName(className)
            val field = classObj.getDeclaredField(fieldName)
            field.isAccessible = true
            fieldValue = field.get(null)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return fieldValue
    }

    /**
     * 修改类的静态属性值，不管是 public 或者 private 的都可以。
     *
     * @param className 类名
     * @param fieldName 属性名
     * @param value     修改后的属性值
     * @return true，如果修改成功; false，如果修改失败
     */
    fun setStaticField(className: String, fieldName: String, value: Any): Boolean {
        var result = false
        try {
            val classObj = Class.forName(className)
            val field = classObj.getDeclaredField(fieldName)
            field.isAccessible = true
            field.set(null, value)
            result = true
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return result
    }

    /**
     * 获得对象的属性值，不管是 public 或者 private 的都可以，
     * 但是该方法只能获得在该对象的类中所定义的成员变量的值，不能获得其父类的成员变量的值。
     *
     * @param owner     对象
     * @param fieldName 对象所属类的属性名
     * @return 对象所属类中的属性值
     */
    fun getField(owner: Any, fieldName: String): Any? {
        var fieldValue: Any? = null
        try {
            val classObj = owner.javaClass
            val field = classObj.getDeclaredField(fieldName)
            field.isAccessible = true
            fieldValue = field.get(owner)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return fieldValue
    }

    /**
     * 获得对象的属性值（属性是对象的父类中定义的），不管是 public 或者 private 的都可以。
     *
     * @param owner     对象
     * @param className 对象所属类的父类的类名
     * @param fieldName 对象所属类的父类的属性名
     * @return 对象所属类的父类的属性值
     */
    fun getField(owner: Any, className: String, fieldName: String): Any? {
        var fieldValue: Any? = null
        try {
            val classObj = Class.forName(className)
            val field = classObj.getDeclaredField(fieldName)
            field.isAccessible = true
            fieldValue = field.get(owner)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

        return fieldValue
    }

    /**
     * 修改对象的属性值，不管是 public 或者 private 的都可以，
     * 但是该方法只能修改在该对象的类中所定义的成员变量的值，不能修改其父类的成员变量的值。
     *
     * @param owner     对象
     * @param fieldName 对象所属类的属性名
     * @param value     修改后的属性值
     * @return true，如果修改成功; false，如果修改失败
     */
    fun setField(owner: Any, fieldName: String, value: Any): Boolean {
        var result = false
        try {
            val classObj = owner.javaClass
            val field = classObj.getDeclaredField(fieldName)
            field.isAccessible = true
            field.set(owner, value)
            result = true
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return result
    }

    /**
     * 修改对象的属性值（属性是对象的父类中定义的），不管是 public 或者 private 的都可以。
     *
     * @param owner     对象
     * @param className 对象所属类的父类的类名
     * @param fieldName 对象所属类的父类的属性名
     * @param value     修改后的属性值
     * @return true，如果修改成功; false，如果修改失败
     */
    fun setField(owner: Any, className: String, fieldName: String, value: Any): Boolean {
        var result = false
        try {
            val classObj = Class.forName(className)
            val field = classObj.getDeclaredField(fieldName)
            field.isAccessible = true
            field.set(owner, value)
            result = true
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

        return result
    }

    /**
     * 调用类的静态方法，不管是 public 或者 private 的都可以。
     *
     * @param className  类名
     * @param methodName 静态方法名
     * @param args       方法的参数
     * @param argClasses 方法的参数的 class type
     * @return 静态方法的返回值
     */
    fun invokeStaticMethod(className: String, methodName: String, args: Array<Any>, argClasses: Array<Class<*>>): Any? {
        var result: Any? = null
        try {
            val classObj = Class.forName(className)
            val method = classObj.getDeclaredMethod(methodName, *argClasses)
            method.isAccessible = true
            result = method.invoke(null, *args)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return result
    }

    /**
     * 调用对象的方法，不管是 public 或者 private 的都可以。
     * 但是该方法只能调用对象所属类中定义的方法，不能调用其父类中定义的方法。
     *
     * @param owner      对象
     * @param methodName 对象所属类的方法名
     * @param args       方法的参数
     * @param argClasses 方法的参数的 class type
     * @return 方法的返回值
     */
    fun invokeMethod(owner: Any, methodName: String, args: Array<Any>, argClasses: Array<Class<*>>): Any? {
        var result: Any? = null
        try {
            val classObj = owner.javaClass
            val method = classObj.getDeclaredMethod(methodName, *argClasses)
            method.isAccessible = true
            result = method.invoke(owner, *args)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return result
    }

    /**
     * 调用对象的方法，不管是 public 或者 private 的都可以。
     * 但是该方法只能调用对象所属类中定义的方法，不能调用其父类中定义的方法。
     *
     * @param owner      对象
     * @param methodName 对象所属类的方法名
     * @param args       方法的参数
     * @param argClasses 方法的参数的 class type
     * @return 方法的返回值
     */
    fun invokeMethod(owner: Any, methodName: String, args: Any, argClasses: Class<*>, showLog: Boolean = true): Any? {
        var result: Any? = null
        try {
            val classObj = owner.javaClass
            val method = classObj.getDeclaredMethod(methodName, argClasses)
            method.isAccessible = true
            result = method.invoke(owner, args)
        } catch (e: NoSuchMethodException) {
            if (showLog) e.printStackTrace()
        } catch (e: InvocationTargetException) {
            if (showLog) e.printStackTrace()
        } catch (e: IllegalAccessException) {
            if (showLog) e.printStackTrace()
        }

        return result
    }

    /**
     * 调用对象的方法（方法是在对象所属类的父类中定义的），不管是 public 或者 private 的都可以。
     * 如果这个方法在子类中重写的话，实际上还是调用子类的重写方法
     *
     * @param owner      对象
     * @param className  对象所属类的父类的类名
     * @param methodName 对象所属类的父类的方法名
     * @param args       方法参数
     * @param argClasses 方法的参数的 class type
     * @return 方法的返回值
     */
    fun invokeMethod(
        owner: Any,
        className: String,
        methodName: String,
        args: Array<Any>,
        argClasses: Array<Class<*>>
    ): Any? {
        var result: Any? = null
        try {
            val classObj = Class.forName(className)
            val method = classObj.getDeclaredMethod(methodName, *argClasses)
            method.isAccessible = true
            result = method.invoke(owner, *args)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return result
    }

    /**
     * 新建一个类型的数组
     *
     * @param className 数组的类型的类名
     * @param len       数组的大小
     * @return 数组
     */
    fun newArrayInstance(className: String, len: Int): Any? {
        var result: Any? = null
        try {
            val classObj = Class.forName(className)
            result = java.lang.reflect.Array.newInstance(classObj, len)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        return result
    }

    /**
     * 获得数组中的一个元素
     *
     * @param array 数组
     * @param index 元素在数组中的位置
     * @return 数组中的这个元素
     */
    fun getObjectInArray(array: Any, index: Int): Any? {
        return java.lang.reflect.Array.get(array, index)
    }

    /**
     * 修改数组中的一个元素的值
     *
     * @param array 数组
     * @param index 元素在数组中的位置
     * @param value 修改后的元素值
     */
    fun setObjectInArray(array: Any, index: Int, value: Any) {
        java.lang.reflect.Array.set(array, index, value)
    }
}
