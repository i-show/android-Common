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

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.ishow.common.R;
import com.ishow.common.utils.IntentUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("all")
public class PermissionManager {

    private static final String TAG = "PermissionManager";

    private PermissionManager() {
    }

    /**
     * Check if the calling context has a set of permissions.
     *
     * @param context     {@link Context}.
     * @param permissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasPermission(@NonNull Context context, @NonNull String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;

        for (String permission : permissions) {
            boolean hasPermission = (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) return false;
        }
        return true;
    }


    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param activity          {@link Activity}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    private static boolean hasAlwaysDeniedPermission(@NonNull Object activity, @NonNull List<String> deniedPermissions) {
        // Warning this method only set private
        for (String deniedPermission : deniedPermissions) {
            if (!PermissionUtils.shouldShowRationalePermissions(activity, deniedPermission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param activity          {@link Activity}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(@NonNull Activity activity, @NonNull List<String> deniedPermissions) {
        for (String deniedPermission : deniedPermissions) {
            if (!PermissionUtils.shouldShowRationalePermissions(activity, deniedPermission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param fragment          {@link android.support.v4.app.Fragment}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(@NonNull android.support.v4.app.Fragment fragment, @NonNull List<String> deniedPermissions) {
        for (String deniedPermission : deniedPermissions) {
            if (!PermissionUtils.shouldShowRationalePermissions(fragment, deniedPermission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param fragment          {@link android.app.Fragment}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(@NonNull android.app.Fragment fragment, @NonNull String... deniedPermissions) {
        for (String deniedPermission : deniedPermissions) {
            if (!PermissionUtils.shouldShowRationalePermissions(fragment, deniedPermission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * In the Activity.
     *
     * @param activity {@link Activity}.
     * @return {@link Permission}.
     */
    public static
    @NonNull
    Permission with(@NonNull Activity activity) {
        return new DefaultPermission(activity);
    }

    /**
     * In the Activity.
     *
     * @param fragment {@link android.support.v4.app.Fragment}.
     * @return {@link Permission}.
     */
    public static
    @NonNull
    Permission with(@NonNull android.support.v4.app.Fragment fragment) {
        return new DefaultPermission(fragment);
    }

    /**
     * In the Activity.
     *
     * @param fragment {@link android.app.Fragment}.
     * @return {@link Permission}.
     */
    public static
    @NonNull
    Permission with(@NonNull android.app.Fragment fragment) {
        return new DefaultPermission(fragment);
    }

    /**
     * Request permissions in the activity.
     *
     * @param activity    {@link Activity}.
     * @param requestCode request code.
     * @param permissions all permissions.
     */
    public static void send(@NonNull Activity activity, int requestCode, @NonNull String... permissions) {
        with(activity).requestCode(requestCode).permission(permissions).send();
    }

    /**
     * Request permissions in the activity.
     *
     * @param fragment    {@link android.support.v4.app.Fragment}.
     * @param requestCode request code.
     * @param permissions all permissions.
     */
    public static void send(@NonNull android.support.v4.app.Fragment fragment, int requestCode, @NonNull String...
            permissions) {
        with(fragment).requestCode(requestCode).permission(permissions).send();
    }

    /**
     * Request permissions in the activity.
     *
     * @param fragment    {@link android.app.Fragment}.
     * @param requestCode request code.
     * @param permissions all permissions.
     */
    public static void send(@NonNull android.app.Fragment fragment, int requestCode, @NonNull String... permissions) {
        with(fragment).requestCode(requestCode).permission(permissions).send();
    }

    /**
     * Parse the request results.
     *
     * @param activity     {@link Activity}.
     * @param requestCode  request code.
     * @param permissions  all permissions.
     * @param grantResults results.
     */
    public static void onRequestPermissionsResult(@NonNull Activity activity, int requestCode, @NonNull String[] permissions, int[] grantResults) {
        onRequestPermissionsResult(activity, activity.getClass(), requestCode, permissions, grantResults);
    }


    /**
     * Parse the request results.
     *
     * @param activity     {@link Activity}.
     * @param realizeClass 注解的实现类.
     * @param requestCode  request code.
     * @param permissions  all permissions.
     * @param grantResults results.
     */
    public static void onRequestPermissionsResult(@NonNull Activity activity, Object realizeClass, int requestCode, @NonNull String[] permissions, int[] grantResults) {
        callbackAnnotation(activity, realizeClass, requestCode, permissions, grantResults);
    }

    /**
     * Parse the request results.
     *
     * @param fragment     {@link android.support.v4.app.Fragment}.
     * @param requestCode  request code.
     * @param permissions  all permissions.
     * @param grantResults results.
     */
    public static void onRequestPermissionsResult(@NonNull android.support.v4.app.Fragment fragment, int requestCode, @NonNull String[] permissions, int[] grantResults) {
        onRequestPermissionsResult(fragment, fragment.getClass(), requestCode, permissions, grantResults);
    }

    /**
     * Parse the request results.
     *
     * @param fragment     {@link android.support.v4.app.Fragment}.
     * @param realizeClass 注解的实现类.
     * @param requestCode  request code.
     * @param permissions  all permissions.
     * @param grantResults results.
     */
    public static void onRequestPermissionsResult(@NonNull android.support.v4.app.Fragment fragment, Object realizeClass, int requestCode, @NonNull String[] permissions, int[] grantResults) {
        callbackAnnotation(fragment, realizeClass, requestCode, permissions, grantResults);
    }

    /**
     * Parse the request results.
     *
     * @param o            {@link Activity} or {@link android.support.v4.app.Fragment} or
     *                     {@link android.app.Fragment}.
     * @param requestCode  request code.
     * @param permissions  all permissions.
     * @param grantResults results.
     */
    private static void callbackAnnotation(@NonNull Object context, int requestCode, @NonNull String[] permissions, int[] grantResults) {
        callbackAnnotation(context, null, requestCode, permissions, grantResults);
    }


    /**
     * Parse the request results.
     *
     * @param o            {@link Activity} or {@link android.support.v4.app.Fragment} or
     *                     {@link android.app.Fragment}.
     * @param requestCode  request code.
     * @param permissions  all permissions.
     * @param grantResults results.
     */
    private static void callbackAnnotation(@NonNull Object context, Object realizeClass, int requestCode, @NonNull String[] permissions, int[] grantResults) {
        List<String> grantedList = new ArrayList<>();
        List<String> deniedList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                grantedList.add(permissions[i]);
            } else {
                deniedList.add(permissions[i]);
            }
        }

        dialogDeniedTip(context, deniedList);
        boolean isAllGrant = deniedList.isEmpty();

        Class<? extends Annotation> clazz = isAllGrant ? PermissionGranted.class : PermissionDenied.class;
        List<Method> methods = findMethodForRequestCode(realizeClass.getClass(), clazz, requestCode);
        if (methods.size() == 0) {
            Log.e(TAG, "Not found the callback method, do you forget @PermissionGranted or @permissionNo" +
                    " for callback method ? Or you can use PermissionListener.");
            return;
        }
        // 这里提供了2中返回的方法 1. 带有参数 2. 不带参数
        Object args = isAllGrant ? grantedList : deniedList;
        // 返回（Context， List<Permission>）
        boolean success = invoke(methods, realizeClass, context, args);
        if (!success) {
            // 返回（Context）
            success = invoke(methods, realizeClass, context);
        }
        if (!success) {
            // 返回（ List<Permission>）
            success = invoke(methods, realizeClass, args);
        }

        if (!success) {
            // 返回（）
            invoke(methods, realizeClass);
        }

    }

    private static boolean invoke(List<Method> methods, Object receiver, Object... args) {
        try {
            for (Method method : methods) {
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                method.invoke(receiver, args);
            }
        } catch (Exception e) {
            return false;
        } finally {
            return true;
        }
    }


    private static void dialogDeniedTip(final Object o, List<String> deniedList) {
        if (deniedList == null || deniedList.isEmpty()) {
            Log.i(TAG, "dialogDeniedTip:deniedList is empty ");
            return;
        }

        final Context context = PermissionUtils.getContext(o);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.premission_denied_tip);
        builder.setNegativeButton(R.string.cancel, null);
        if (hasAlwaysDeniedPermission(o, deniedList)) {
            builder.setPositiveButton(R.string.go_settings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    IntentUtils.goToAppSettings(context);
                }
            });
        } else {
            builder.setPositiveButton(R.string.yes, null);
        }

        builder.create().show();
    }

    private static <T extends Annotation> List<Method> findMethodForRequestCode(@NonNull Class<?> source, @NonNull
            Class<T> annotation, int requestCode) {
        List<Method> methods = new ArrayList<>(1);
        for (Method method : source.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation) && isSameRequestCode(method, annotation, requestCode)) {
                methods.add(method);
            }
        }
        return methods;
    }

    private static <T extends Annotation> boolean isSameRequestCode(@NonNull Method method, @NonNull Class<T>
            annotation, int requestCode) {
        if (PermissionGranted.class.equals(annotation)) {
            return method.getAnnotation(PermissionGranted.class).value() == requestCode;
        } else if (PermissionDenied.class.equals(annotation)) {
            return method.getAnnotation(PermissionDenied.class).value() == requestCode;
        }
        return false;
    }

}
