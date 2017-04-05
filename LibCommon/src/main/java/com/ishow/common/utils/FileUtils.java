/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.common.utils;

import android.net.Uri;
import android.util.Log;

import java.io.File;

public class FileUtils {
    private static final String TAG = "FileUtils";
    public static final String LOCAL_URI_HEADER = "file://";

    /**
     * 根据Url删除一个文件
     */
    public static boolean delete(Uri uri) {
        String uriStr = uri.toString();
        String path = uriStr.substring(LOCAL_URI_HEADER.length());
        Log.i(TAG, "path = " + path);
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        } else {
            Log.i(TAG, "file is not exists");
            return false;
        }
    }

    public static String getPathFromUri(Uri uri) {
        String uriStr = uri.toString();
        return uriStr.substring(LOCAL_URI_HEADER.length());
    }

    public static File getFileFromUri(Uri uri) {
        String uriStr = uri.toString();
        String path = uriStr.substring(LOCAL_URI_HEADER.length());
        Log.i(TAG, "path = " + path);
        return new File(path);
    }

    public static String getFileName(String path) {
        String names[] = path.split("/");
        return names[names.length - 1];
    }

}
