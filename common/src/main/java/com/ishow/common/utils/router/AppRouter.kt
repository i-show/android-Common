package com.ishow.common.utils.router

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.annotation.StringRes
import android.text.TextUtils
import android.util.Log
import com.ishow.common.utils.cache.LRUCache
import com.ishow.common.utils.log.LogUtils

import java.io.Serializable
import java.lang.IllegalStateException

/**
 * Created by yuhaiyang on 2017/12/12.
 * App 路由
 */

class AppRouter private constructor() {
    private lateinit var context: Context
    private var action: String? = null
    private var url: String? = null
    private var scheme: String? = null
    private var host: String? = null
    private var packageName: String? = null
    private var targetClass: Class<*>? = null

    private var params: Bundle? = null
    private var requestCode: Int = 0
    private var flag: Int = 0
    private val animation: IntArray
    private var isFinish: Boolean = false

    init {
        flag = INVALID_FLAGS
        requestCode = INVALID_FLAGS
        animation = IntArray(2)
    }


    fun action(action: String): AppRouter {
        this.action = action
        return this
    }

    fun url(uri: Uri?): AppRouter {
        if (uri != null) {
            url = uri.toString()
        }
        return this
    }

    fun url(url: String): AppRouter {
        this.url = url
        return this
    }

    fun scheme(scheme: String): AppRouter {
        this.scheme = scheme
        return this
    }

    fun scheme(@StringRes scheme: Int): AppRouter {
        this.scheme = context.getString(scheme)
        return this
    }

    fun host(host: String): AppRouter {
        this.host = host
        return this
    }

    fun host(@StringRes host: Int): AppRouter {
        this.host = context.getString(host)
        return this
    }

    fun target(cls: Class<*>): AppRouter {
        targetClass = cls
        return this
    }

    fun flag(flag: Int): AppRouter {
        this.flag = flag
        return this
    }

    fun requestCode(code: Int): AppRouter {
        requestCode = code
        return this
    }

    /**
     * 设置包名
     */
    fun packageName(packageName: String): AppRouter {
        this.packageName = packageName
        return this
    }

    fun params(bundle: Bundle): AppRouter {
        if (params == null) {
            params = bundle
        } else {
            params!!.putAll(bundle)
        }
        return this
    }

    fun addParam(key: String, value: String): AppRouter {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            Log.i(TAG, "addParam String error ")
            return this
        }

        if (params == null) {
            params = Bundle()
        }
        params!!.putString(key, value)
        return this
    }

    fun addParam(key: String, value: Serializable?): AppRouter {
        if (TextUtils.isEmpty(key) || value == null) {
            Log.i(TAG, "addParam Serializable : params is error ")
            return this
        }

        if (params == null) {
            params = Bundle()
        }
        params!!.putSerializable(key, value)
        return this
    }

    fun addParam(key: String, value: Int): AppRouter {

        if (TextUtils.isEmpty(key)) {
            Log.i(TAG, "addParam: ")
            return this
        }

        if (params == null) {
            params = Bundle()
        }
        params!!.putInt(key, value)
        return this
    }

    /**
     * 补间动画
     */
    fun overridePendingTransition(@AnimRes enterAnim: Int, @AnimRes exitAnim: Int): AppRouter {
        animation[0] = enterAnim
        animation[1] = exitAnim
        return this
    }

    /**
     * 是否关闭上一个应用
     */
    fun finishSelf(): AppRouter {
        isFinish = true
        return this
    }


    /**
     * 返回是否跳转成功
     */
    fun start(): Boolean {
        if (context is Activity && (context as Activity).isFinishing) {
            Log.i(TAG, "start: activity is finishing")
            return false
        }

        config?.config(this)

        val routerKey = StringBuilder()
        // 跳转目标
        var isNoTarget = true
        val intent: Intent
        if (targetClass != null) {
            intent = Intent(context, targetClass)
            routerKey.append(targetClass!!.name)
            isNoTarget = false
        } else if (!TextUtils.isEmpty(action)) {
            intent = Intent(action)
            routerKey.append(action)
            isNoTarget = false
        } else {
            intent = Intent(Intent.ACTION_VIEW)
            routerKey.append(Intent.ACTION_VIEW)
        }

        val uri = makeUri()
        if (uri != null) {
            intent.data = uri
            routerKey.append(uri.toString())
            isNoTarget = false
        }

        if (isNoTarget) {
            LogUtils.e(TAG, "no target activity")
            return false
        }

        if (flag != INVALID_FLAGS) {
            intent.flags = flag
        }

        if (params != null) {
            intent.putExtras(params!!)
        }

        // Android 8.0 需要增加packageName设置否则会出现找不到引用提示
        if (!TextUtils.isEmpty(packageName)) {
            intent.setPackage(packageName)
        }

        if (isFastRouter(routerKey.toString())) {
            return false
        }

        try {
            if (requestCode != INVALID_FLAGS && context is Activity) {
                val activity = context as Activity
                activity.startActivityForResult(intent, requestCode)
            } else {
                context.startActivity(intent)
            }

            if (context is Activity && (animation[0] > 0 || animation[1] > 0)) {
                (context as Activity).overridePendingTransition(animation[0], animation[1])
            }

            if (isFinish && context is Activity) {
                (context as Activity).finish()
            }

        } catch (e: Exception) {
            return false
        }

        return true
    }

    private fun makeUri(): Uri? {
        if (!TextUtils.isEmpty(url)) {
            return Uri.parse(url)
        }

        if (!TextUtils.isEmpty(scheme) && !TextUtils.isEmpty(host)) {
            val builder = Uri.Builder()
            builder.scheme(scheme)
            builder.authority(host)
            return builder.build()
        }
        return null
    }

    /**
     * 是否是迅速的2次跳转
     */
    private fun isFastRouter(key: String): Boolean {
        val nowTime = System.currentTimeMillis()
        val lastTime = getLastRouteTime(key)
        if (nowTime - lastTime < S_INTERVAL_OF_ROUTE_TIME) {
            Log.i(TAG, "start: The interval between activities is too short")
            return true
        }
        sRouteTime[key] = nowTime
        return false
    }

    private fun getLastRouteTime(key: String): Long {
        return if (!sRouteTime.containsKey(key)) 0L else sRouteTime[key]!!
    }

    companion object {
        private const val TAG = "AppRouter"
        /**
         * 路由缓存时间设置
         */
        private const val MAX_ROUTER_CACHE_SIZE = 20
        /**
         * 非法的标记位
         */
        private const val INVALID_FLAGS = -512
        /**
         * 路由跳转的间隔时间
         */
        private const val S_INTERVAL_OF_ROUTE_TIME = 300

        private var config: AbsRouterConfigure? = null
        /**
         * 路由时间判定
         */
        private val sRouteTime = LRUCache<String, Long>(MAX_ROUTER_CACHE_SIZE)


        fun setConfigure(custom: AbsRouterConfigure) {
            config = custom
        }

        fun with(context: Context?): AppRouter {
            if(context == null) throw IllegalStateException("context is null")
            val router = AppRouter()
            router.context = context
            router.packageName = context.packageName
            return router
        }
    }
}
