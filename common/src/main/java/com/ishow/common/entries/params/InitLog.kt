package com.ishow.common.entries.params

/**
 * Created by yuhaiyang on 2019-08-29.
 *
 */
class InitLog {
    var app: App? = null
    var device: Device? = null
    var dateTime: Long? = null

    class App {
        var appId: String? = null
        var appName: String? = null
        var appVersion: String? = null
        var commonVersion: String? = null
        var commonArtifact: String? = null
    }

    class Device {
        var deviceId: String? = null
        var type: String? = null
        var manufacturer: String? = null
        var model: String? = null
        var sdk: String? = null
        var version: String? = null
        var sw: String? = null
        var resolution: String? = null
    }
}