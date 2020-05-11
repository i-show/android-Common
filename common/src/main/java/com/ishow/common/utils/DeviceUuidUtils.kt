package com.ishow.common.utils

import android.Manifest
import android.annotation.SuppressLint
import android.os.Environment
import com.ishow.common.app.provider.InitCommonProvider
import com.ishow.common.utils.permission.PermissionManager
import java.io.File
import java.util.*

class DeviceUuidUtils @SuppressLint("HardwareIds") private constructor() {

    private object Key {
        const val SAVE_GROUP = "key_device_uuid_group"
        const val UUID = "key_device_id"
    }

    companion object {

        private val UUID_FILE_NAME: String by lazy {
            val fileName = EncryptUtils.md5(InitCommonProvider.app.packageName, false)
            ".${fileName}.conf"
        }

        private var deviceUUID: UUID? = null
        val uuid: String
            get() = initUUID().toString()

        private fun save(fileName: String, value: String) {
            if (!hasWriteStoragePermission()) return

            val dirPath = Environment.getExternalStorageDirectory()
            if (dirPath?.exists() == false) {
                dirPath.mkdirs()
            }

            val targetFile = File(dirPath, fileName)
            if (!targetFile.exists()) {
                targetFile.writeText(value)
            }
        }

        private fun read(fileName: String): String? {
            if (!hasReadStoragePermission()) return null

            val dirPath = Environment.getExternalStorageDirectory()
            val targetFile = File(dirPath, fileName)
            return if (targetFile.exists()) targetFile.readText() else null
        }


        private fun hasReadStoragePermission(): Boolean {
            val app = InitCommonProvider.app
            return PermissionManager.hasPermission(app, Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        private fun hasWriteStoragePermission(): Boolean {
            val app = InitCommonProvider.app
            return PermissionManager.hasPermission(app, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        private fun initUUID(): UUID {
            val uuid = this.deviceUUID
            if (uuid != null) {
                return uuid
            }

            synchronized(DeviceUuidUtils::class.java) {
                if (deviceUUID != null) {
                    return deviceUUID!!
                }

                var savedUUID: String? = StorageUtils.get()
                    .group(Key.SAVE_GROUP)
                    .key(Key.UUID)
                    .apply()

                if (savedUUID.isNullOrEmpty()) {
                    savedUUID = read(UUID_FILE_NAME)
                }

                if (savedUUID.isNullOrEmpty()) {
                    val androidId = DeviceUtils.deviceId(InitCommonProvider.app)
                    val newUUID = if (androidId.isNotEmpty() && "9774d56d682e549c" != androidId) {
                        UUID.nameUUIDFromBytes(androidId.toByteArray())
                    } else {
                        UUID.randomUUID()
                    }

                    saveUUID(newUUID.toString())
                    deviceUUID = newUUID
                    return newUUID
                } else {
                    val newUUID = UUID.fromString(savedUUID)
                    saveUUID(newUUID.toString())
                    deviceUUID = newUUID
                    return newUUID
                }
            }
        }


        private fun saveUUID(uuid: String) {
            StorageUtils.save()
                .group(Key.SAVE_GROUP)
                .addParam(Key.UUID, uuid)
                .apply()

            save(UUID_FILE_NAME, uuid)
        }
    }


}