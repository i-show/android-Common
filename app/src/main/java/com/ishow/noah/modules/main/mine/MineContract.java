package com.ishow.noah.modules.main.mine;

import android.content.Context;

import com.ishow.common.mvp.base.IPresenter;
import com.ishow.common.mvp.base.IView;
import com.ishow.common.mvp.base.IViewStatus;
import com.ishow.noah.entries.UserContainer;

/**
 * Created by yuhaiyang on 2018/3/28.
 * Contract
 */

interface MineContract {
    interface View extends IView, IViewStatus {
        /**
         * 更新信息
         */
        void update(UserContainer userContainer);

    }

    interface Presenter extends IPresenter {

        void onResume(Context context);
    }
}
