/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ishow.common.utils.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


class DefaultPermission implements Permission {

    private static final String TAG = "PermissionManager";

    private String[] permissions;
    /**
     * 没有权限的 权限
     */
    @SuppressWarnings("FieldCanBeLocal")
    private String[] deniedPermissions;
    /**
     * 回调的code
     */
    int requestCode;
    /**
     * 提示方式
     */
    int promptType;
    /**
     * 提示信息
     */
    String message;
    /**
     * 使用注解的类
     * 默认是当前的activity，如果是Dialog 那就在Dialog中添加
     */
    Object annotationClass;

    private Object object;

    DefaultPermission(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("The object can not be null.");
        }
        this.object = o;
        this.annotationClass = object;
        this.promptType = PermissionPromptType.Null;
    }

    @NonNull
    @Override
    public Permission permission(String... permissions) {
        if (permissions == null)
            throw new IllegalArgumentException("The permissions can not be null.");
        this.permissions = permissions;
        return this;
    }

    @NonNull
    @Override
    public Permission requestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    @Override
    public Permission annotationClass(@NonNull Object obj) {
        this.annotationClass = obj;
        return this;
    }

    @Override
    public Permission message(String message) {
        this.message = message;
        if (promptType == PermissionPromptType.Null) {
            promptType = PermissionPromptType.Toast;
        }
        return this;
    }

    @Override
    public Permission message(@StringRes int message) {
        Context context = PermissionUtils.getContext(object);
        this.message = context.getString(message);
        if (promptType == PermissionPromptType.Null) {
            promptType = PermissionPromptType.Toast;
        }
        return this;
    }

    @Override
    public Permission promptType(@PermissionPromptType int type) {
        this.promptType = type;
        return this;
    }

    @Override
    @SuppressWarnings("unused")
    public void send() {

        // 添加到信息中去
        PermissionManager.addRequestPermission(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Context context = PermissionUtils.getContext(object);

            final int[] grantResults = new int[permissions.length];
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();

            final int permissionCount = permissions.length;
            for (int i = 0; i < permissionCount; i++) {
                grantResults[i] = packageManager.checkPermission(permissions[i], packageName);
            }
            onRequestPermissionsResult(object, requestCode, permissions, grantResults);
        } else {
            deniedPermissions = getDeniedPermissions(object, permissions);
            // Denied permissions size > 0.
            if (deniedPermissions.length > 0) {
                requestPermissions(object, requestCode, deniedPermissions);
            } else { // All permission granted.
                final int[] grantResults = new int[permissions.length];
                onRequestPermissionsResult(object, requestCode, permissions, grantResults);
            }
        }
    }

    private static String[] getDeniedPermissions(Object o, String... permissions) {
        Context context = PermissionUtils.getContext(o);
        List<String> deniedList = new ArrayList<>(1);
        for (String permission : permissions) {
            if (!PermissionManager.hasPermission(context, permission)) {
                deniedList.add(permission);
            }
        }
        return deniedList.toArray(new String[deniedList.size()]);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void requestPermissions(Object o, int requestCode, String... permissions) {
        if (o instanceof Activity)
            ActivityCompat.requestPermissions(((Activity) o), permissions, requestCode);
        else if (o instanceof android.support.v4.app.Fragment)
            ((android.support.v4.app.Fragment) o).requestPermissions(permissions, requestCode);
        else if (o instanceof android.app.Fragment) {
            ((android.app.Fragment) o).requestPermissions(permissions, requestCode);
            Log.e(TAG, "The " + o.getClass().getName() + " is not support " + "requestPermissions()");
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void onRequestPermissionsResult(Object o, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (o instanceof Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((Activity) o).onRequestPermissionsResult(requestCode, permissions, grantResults);
            } else if (o instanceof ActivityCompat.OnRequestPermissionsResultCallback) {
                ((ActivityCompat.OnRequestPermissionsResultCallback) o).onRequestPermissionsResult(requestCode, permissions, grantResults);
            } else {
                Log.e(TAG, "The " + o.getClass().getName() + " is not support " + "onRequestPermissionsResult()");
            }
        } else if (o instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) o).onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if (o instanceof android.app.Fragment) {
            ((android.app.Fragment) o).onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    int getContextHashCode() {
        return object.hashCode();
    }
}
