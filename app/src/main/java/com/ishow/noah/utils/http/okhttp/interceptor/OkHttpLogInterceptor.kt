package com.ishow.noah.utils.http.okhttp.interceptor

import com.ishow.common.utils.log.LogUtils
import okhttp3.*
import okhttp3.internal.http.HttpHeaders
import okio.Buffer
import java.io.EOFException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class OkHttpLogInterceptor @JvmOverloads constructor(var level: Level = Level.Body) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val level = this.level
        val request: Request = chain.request()
        // 没有Log输入
        if (level == Level.None) {
            return chain.proceed(request)
        }

        val connection = chain.connection()
        val requestId = genRequestId()
        LogUtils.i(LOG_TAG, "**************** $requestId ****************")
        logRequest(requestId, request, connection)

        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            LogUtils.i(LOG_TAG, " $requestId FAILED: $e")
            throw e
        }

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        logResponse(requestId, response, tookMs)
        return response
    }


    /**
     * Log输出请求
     */
    private fun logRequest(requestId: String, request: Request, connection: Connection?) {
        val logRequestHeader = level >= Level.RequestHeaders
        val requestBody = request.body()
        val protocol = if (connection == null) "" else " ${connection.protocol()}"
        var requestLog = "$requestId ${request.method()} ${request.url()}$protocol"

        if (requestBody != null) {
            requestLog += " (${requestBody.contentLength()}byte)"
        }
        LogUtils.i(LOG_TAG, requestLog)

        if (logRequestHeader) {
            logRequestHeaders(requestId, request)
        }

        if (requestBody == null) {
            return
        }

        val buffer = Buffer()
        requestBody.writeTo(buffer)

        var charset: Charset? = UTF8
        val contentType = requestBody.contentType()
        contentType?.let { charset = it.charset(UTF8) }

        if (isPlaintext(buffer)) {
            LogUtils.i(LOG_TAG, "$requestId PARAMS: ${buffer.readString(charset!!)}")
        } else {
            LogUtils.i(LOG_TAG, "$requestId PARAMS: $contentType  (${requestBody.contentLength()}byte)")
        }
    }

    /**
     * Log输出请求头
     */
    private fun logRequestHeaders(requestId: String, request: Request) {
        val requestBody = request.body()
        val hasRequestBody = requestBody != null

        val logHeaderPrefix = "$requestId HEADER："

        var logHeader = logHeaderPrefix
        if (hasRequestBody) {
            requestBody!!.contentType()?.let { logHeader += "Content-Type: $it $HEADER_SPACER" }
        }

        val headers = request.headers()
        val headerCount = headers.size()

        for (i in 0 until headerCount) {
            val name = headers.name(i)
            if (checkHeaderName(name)) {
                val log = "$name: ${headers.value(i)} $HEADER_SPACER"
                if (logHeader.length + log.length > MAX_LENGTH) {
                    LogUtils.i(LOG_TAG, logHeader)
                    logHeader = logHeaderPrefix + log
                } else {
                    logHeader += log
                }
            }
        }

        LogUtils.i(LOG_TAG, logHeader)
    }

    private fun logResponse(requestId: String, response: Response, tookMs: Long) {
        val responseBody = response.body()
        if (responseBody == null) {
            LogUtils.i(LOG_TAG, "$requestId No data")
            return
        }

        val logHeaders: Boolean = level >= Level.Detail
        val logBody: Boolean = level >= Level.Body

        val headers = response.headers()
        val message = response.message()
        val contentLength = responseBody.contentLength()
        val logMessage = if (message.isNullOrBlank()) "" else ", message: $message"
        val logBodySize = if (contentLength != -1L) " size: $contentLength byte" else " size: unknown"
        val logResponseInfo = "$requestId INFO: code: ${response.code()}$logMessage, time: ${tookMs}ms,$logBodySize"
        LogUtils.i(LOG_TAG, logResponseInfo)

        if (logHeaders) {
            logResponseHeader(requestId, headers)
        }

        if (!logBody || !HttpHeaders.hasBody(response)) {
            LogUtils.i(LOG_TAG, "$requestId END")
            return
        }

        if (bodyHasUnknownEncoding(headers)) {
            LogUtils.i(LOG_TAG, "$requestId END (encoded body omitted)\"")
            return
        }

        val source = responseBody.source()
        source.request(Long.MAX_VALUE) // Buffer the entire body.
        val buffer = source.buffer

        var charset: Charset? = UTF8
        val contentType = responseBody.contentType()
        if (contentType != null) {
            charset = contentType.charset(UTF8)
        }

        if (!isPlaintext(buffer)) {
            LogUtils.i(LOG_TAG, "$requestId RESULT ${buffer.size()} byte body omitted")
            return
        }

        if (contentLength != 0L) {
            val result = buffer.clone().readString(charset!!)
            LogUtils.i(LOG_TAG, "$requestId RESULT: $result ")
        }

    }

    /**
     * Log数据返回的Header
     */
    private fun logResponseHeader(requestId: String, headers: Headers) {
        val logHeaderPrefix = "$requestId RESULT HEADER："
        var logHeader = logHeaderPrefix

        for (i in 0 until headers.size()) {
            val name = headers.name(i)
            val log = "$name: ${headers.value(i)} $HEADER_SPACER"
            if (logHeader.length + log.length > MAX_LENGTH) {
                LogUtils.i(LOG_TAG, logHeader)
                logHeader = logHeaderPrefix + log
            } else {
                logHeader += log
            }
        }
        LogUtils.i(LOG_TAG, logHeader)
    }


    /**
     * 生成请求的Id
     */
    private fun genRequestId(): String {
        return System.nanoTime().toString().substring(10)
    }

    private fun checkHeaderName(name: String): Boolean {
        return !"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(name, ignoreCase = true)
    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return (contentEncoding != null
                && !contentEncoding.equals("identity", ignoreCase = true)
                && !contentEncoding.equals("gzip", ignoreCase = true))
    }

    private fun isPlaintext(buffer: Buffer): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = if (buffer.size() < 64) buffer.size() else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (e: EOFException) {
            return false
        }

    }

    @Suppress("unused")
    enum class Level(val value: Int) {
        None(0),
        Basic(1),
        RequestHeaders(2),
        Body(3),
        Detail(4),
    }

    companion object {
        private const val LOG_TAG = "OkHttp"

        /**
         * Log的最大长度
         */
        private const val MAX_LENGTH = 2 * 1024
        private const val HEADER_SPACER = " "

        private val UTF8 = Charset.forName("UTF-8")
    }
}