package com.ishow.noahark.modules.base;

import com.baidu.mobstat.StatService;
import com.ishow.common.app.fragment.BaseFragment;

/**
 * 基础Fragment
 * 备用：
 */
public class AppBaseFragment extends BaseFragment {
    private static final String TAG = "AppBaseFragment";

    @Override
    public void onPause() {
        super.onPause();
        // 页面埋点
        StatService.onPageEnd(getActivity(), null);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 页面埋点
        StatService.onPageStart(getActivity(), null);
    }

}
