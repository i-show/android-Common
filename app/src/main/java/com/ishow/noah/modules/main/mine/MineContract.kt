package com.ishow.noah.modules.main.mine

import com.ishow.common.app.mvp.IFragmentView

import com.ishow.common.app.mvp.IPresenter
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
