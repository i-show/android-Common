package com.ishow.noah.modules.main.mine

import android.content.Context
import com.ishow.common.mvp.base.IFragmentView

import com.ishow.common.mvp.base.IPresenter
import com.ishow.common.mvp.base.IView
import com.ishow.common.mvp.base.IViewStatus
import com.ishow.noah.entries.UserContainer

/**
 * Created by yuhaiyang on 2018/3/28.
 * Contract
 */

internal interface MineContract {
    interface View : IFragmentView {
        /**
         * 更新信息
         */
        fun update(userContainer: UserContainer?)

    }

    interface Presenter : IPresenter {

        fun onResume()
    }
}
