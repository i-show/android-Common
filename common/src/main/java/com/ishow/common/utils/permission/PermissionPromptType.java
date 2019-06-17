package com.ishow.common.utils.permission;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by yuhaiyang on 2017/12/18.
 * 提示类型
 */

@IntDef({PermissionPromptType.Null, PermissionPromptType.Toast, PermissionPromptType.Dialog})
@Retention(RetentionPolicy.SOURCE)
public @interface PermissionPromptType {
    /**
     * 不进行提示
     */
    int Null = 100;
    /**
     * Toast 提示
     */
    int Toast = 101;
    /**
     * Dialog样式提示
     */
    int Dialog = 102;
}
