package com.ishow.common.app.provider

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.facebook.drawee.backends.pipeline.Fresco
import com.ishow.common.manager.LogManager
import com.ishow.common.utils.StorageUtils

/**
 * Created by yuhaiyang on 2019-10-17.
 * 初始化部分内容
 */
class InitCommonProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        val context = context

        if (context is Application) {
            StorageUtils.init(context)
            Fresco.initialize(context)
            LogManager.init(context)
        } else {
            Log.i("yhy", "InitCommonProvider not application")
        }
        return true
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw Exception("unimplemented")
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        throw Exception("unimplemented")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        throw Exception("unimplemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        throw Exception("unimplemented")
    }

    override fun getType(uri: Uri): String? {
        throw Exception("unimplemented")
    }

}