package com.ishow.noah.modules.sample.entries


import androidx.annotation.IntDef

/**
 * Created by yuhaiyang on 2017/10/12.
 * Sample info
 */

class Sample(var name: String, var action: Class<*>) {

    /**
     * 定义图片是单选还是多选
     */
    @IntDef(Cate.Widget)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Cate {
        companion object {
            /**
             * 控件
             */
            const val Widget = 1
        }

    }

    companion object {
        @JvmStatic
        fun instance(name: String, action: Class<*>): Sample {
            return Sample(name, action)
        }
    }
}
