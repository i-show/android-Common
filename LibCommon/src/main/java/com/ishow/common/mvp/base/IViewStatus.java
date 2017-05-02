package com.ishow.common.mvp.base;

/**
 * Created by yuhaiyang on 2017/5/2.
 * View状态的更新
 */

public interface IViewStatus {
    /**
     * 显示加载动画
     */
    void showLoading(String message, boolean dialog);

    /**
     * 隐藏加载动画
     */
    void dismissLoading(boolean dialog);

    /**
     * 加载失败
     *
     * @param errorType 预防点击退出
     */
    void showError(String message, boolean dialog, int errorType);

    /**
     * 加载成功
     */
    void showSuccess(String message);

    /**
     * 加载成功
     */
    void showEmpty(String message);
}
