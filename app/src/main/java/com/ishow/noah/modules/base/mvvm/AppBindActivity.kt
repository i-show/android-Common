package com.ishow.noah.modules.base.mvvm

import android.app.Dialog
import androidx.databinding.ViewDataBinding
import com.baidu.mobstat.StatService
import com.ishow.common.app.mvvm.view.BindActivity
import com.ishow.common.widget.watermark.WaterMarkView
import com.ishow.noah.AppApplication
import com.ishow.noah.manager.VersionManager
import com.ishow.noah.ui.widget.dialog.VersionDialog


/**
 * Created by yuhaiyang on 2018/8/8.
 * App层面的BaseActivity
 */
abstract class AppBindActivity<T: ViewDataBinding> : BindActivity<T>() {

    /**
     * 检测升级的Dialog
     */
    private var mVersionDialog: Dialog? = null

    /**
     * 获取应用的Application
     */
    @Suppress("unused")
    protected val appApplication: AppApplication
        get() = application as AppApplication


    override fun initViews() {
        super.initViews()
        WaterMarkView.attachToActivity(this)
    }

    override fun onResume() {
        super.onResume()
        // 百度统计
        StatService.onResume(this)

        if (needShowUpdateVersionDialog() && VersionManager.instance.hasNewVersion(context)) {
            showVersionDialog()
        }
    }

    override fun onPause() {
        super.onPause()
        // 百度统计
        StatService.onPause(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        dismissVersionDialog()
    }

    /**
     * 是否需要显示升级dialog
     */
    protected open fun needShowUpdateVersionDialog(): Boolean {
        return true
    }

    /**
     * 显示升级Dialog
     */
    private fun showVersionDialog() {
        if (mVersionDialog == null) {
            mVersionDialog = VersionDialog(this)
        }

        if (!mVersionDialog!!.isShowing) {
            mVersionDialog!!.show()
        }
    }

    /**
     * 隐藏升级的Dialog
     */
    private fun dismissVersionDialog() {
        if (mVersionDialog != null && mVersionDialog!!.isShowing) {
            mVersionDialog!!.dismiss()
        }
        mVersionDialog = null
    }

}
