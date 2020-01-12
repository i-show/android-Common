package com.ishow.common.utils.image.compress

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.annotation.IntRange
import com.ishow.common.utils.MimeType
import com.ishow.common.extensions.rotate
import com.ishow.common.utils.image.compress.adapter.IRenameAdapter
import com.ishow.common.utils.image.compress.filter.ICompressFilter
import com.ishow.common.utils.image.compress.listener.OnCompleteListener
import com.ishow.common.utils.image.compress.wrapper.ImageWrapper
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

/**
 * Created by yuhaiyang on 2019-12-23.
 * 图片压缩工具命名为沙皇
 * 其实就是LuBan压缩工具的Kotlin版本
 */
class ImageCompress private constructor(builder: Builder) {
    private val context: Context
    /**
     * 图片列表
     */
    private val imageList: MutableList<ImageWrapper>

    private val resultList: Array<File?>
    /**
     * 图片过滤器的List
     */
    private val filterList: MutableList<ICompressFilter>?
    private val filterFunctionList: MutableList<((ImageInfo) -> Boolean)>?
    /**
     * 保存的路径
     */
    private val savePath: String?

    /**
     * 图片压缩格式
     */
    private val compressFormat: Bitmap.CompressFormat
    /**
     * 图片的压缩质量
     */
    private val quality: Int
    /**
     * 最多多少个线程同时压缩
     */
    private val threadNumber: Int
    /**
     * 压缩图片的线程池
     */
    private val executorService: ExecutorService

    /**
     * 压缩的监听
     */
    private val completeListener: OnCompleteListener?
    private val completeFunction: ((CompressResult) -> Unit)?

    /**
     * 重命名的Adapter
     */
    private val renameAdapter: IRenameAdapter?
    private val renameFunction: ((ImageInfo) -> String)?

    private val result: CompressResult

    private var isAlready: Boolean = true

    init {
        context = builder.context
        imageList = builder.imageList
        resultList = arrayOfNulls(imageList.size)

        filterList = builder.filterList
        filterFunctionList = builder.filterFunctionList

        savePath = builder.savePath
        compressFormat = builder.compressFormat
        quality = builder.quality
        threadNumber = builder.threadNumber
        executorService = Executors.newFixedThreadPool(threadNumber)

        completeListener = builder.completeListener
        completeFunction = builder.completeFunction

        renameAdapter = builder.renameAdapter
        renameFunction = builder.renameFunction
        result = CompressResult()
    }

    @SuppressLint("HandlerLeak")
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            // 如果 completeListener为空那么不再进行下面处理
            if (completeListener == null && completeFunction == null) {
                return
            }
            when (msg.what) {
                HANDLER_COMPRESS_ERROR -> {
                    val errorInfo: ErrorInfo = msg.obj as ErrorInfo
                    result.code = CompressResult.STATUS_FAILED
                    result.addError(errorInfo)
                    checkFinish()
                }

                HANDLER_COMPRESS_SUCCESS -> {
                    checkFinish()
                }
            }
        }

        private fun checkFinish() {
            if (executorService.isTerminated && isAlready) {
                isAlready = false
                result.imageList = resultList.toMutableList()
                result.image = result.imageList[0]
                completeListener?.onResult(result)
                completeFunction?.let { it(result) }
            }
        }
    }

    /**
     * 开始压缩
     */
    fun start() {
        if (imageList.isNullOrEmpty()) {
            Log.e(TAG, "start: imageList is Empty")
            return
        }

        isAlready = true
        imageList.forEachIndexed { index, image ->
            executorService.execute(CompressRunnable(image, index))
        }
        executorService.shutdown()

    }


    private inner class CompressRunnable(val image: ImageWrapper, val position: Int) : Runnable {
        override fun run() {
            val info = ImageInfo(position)
            info.mimeType = image.mimeType

            val inputStream = image.open()
            if (inputStream == null) {
                handler.sendMessage(handler.obtainMessage(HANDLER_COMPRESS_ERROR, ErrorInfo(position, "图片不存在")))
                image.close()
                return
            }

            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            options.inSampleSize = 1

            info.byteCount = inputStream.available().toLong()
            BitmapFactory.decodeStream(inputStream, null, options)
            info.width = options.outWidth
            info.height = options.outHeight


            filterList?.forEach {
                if (it.filter(info)) {
                    saveInput(image.open(), info)
                    return
                }
            }
            filterFunctionList?.forEach {
                if (it(info)) {
                    saveInput(image.open(), info)
                    return
                }
            }

            val compressOptions = BitmapFactory.Options()
            compressOptions.inSampleSize = computeSize(info)
            var bitmap = BitmapFactory.decodeStream(image.open(), null, compressOptions)
            if (bitmap == null) {
                handler.sendMessage(handler.obtainMessage(HANDLER_COMPRESS_ERROR, ErrorInfo(position, "创建Bitmap失败")))
                image.close()
                return
            }

            if (CompressUtils.isJPG(image.open())) {
                bitmap = bitmap.rotate(CompressUtils.getOrientation(image.open()))
            }
            saveBitmap(bitmap, info, quality)

        }

        private fun saveInput(input: InputStream?, info: ImageInfo) {
            val targetMimeType = compressFormat2MimeType()
            if (targetMimeType == info.mimeType) {
                saveFile(input, info)
            } else {
                saveBitmap(BitmapFactory.decodeStream(input), info)
            }
        }

        private fun saveFile(input: InputStream?, info: ImageInfo) {
            val fileStream = getSaveFile(info).outputStream()
            try {
                input?.copyTo(fileStream, 2048)
            } finally {
                input?.close()
                fileStream.flush()
                fileStream.close()
            }
        }

        /**
         * 保存图片
         */
        private fun saveBitmap(bitmap: Bitmap, info: ImageInfo, saveQuality: Int = DEFAULT_COMPRESS_QUALITY) {
            val file = getSaveFile(info)
            val outputStream = FileOutputStream(file)
            try {
                bitmap.compress(compressFormat, saveQuality, outputStream)
            } catch (e: Exception) {
                handler.sendMessage(handler.obtainMessage(HANDLER_COMPRESS_ERROR, ErrorInfo(position, "压缩失败，error:$e")))
                return
            } finally {
                outputStream.flush()
                outputStream.close()
                bitmap.recycle()
            }

            resultList[position] = file
            handler.sendMessageDelayed(handler.obtainMessage(HANDLER_COMPRESS_SUCCESS), 100)
        }

        private fun getSaveFile(info: ImageInfo): File {
            val folder = if (savePath.isNullOrEmpty()) {
                context.externalCacheDir!!
            } else {
                File(context.externalCacheDir!!, savePath)
            }
            if (!folder.exists()) {
                folder.mkdirs()
            }
            val suffix = when (compressFormat) {
                Bitmap.CompressFormat.JPEG -> ".jpg"
                Bitmap.CompressFormat.PNG -> ".png"
                Bitmap.CompressFormat.WEBP -> ".webp"
            }

            val name = when {
                renameAdapter != null -> {
                    renameAdapter.rename(info) + suffix
                }
                renameFunction != null -> {
                    renameFunction.let { it(info) + suffix }
                }
                else -> {
                    UUID.randomUUID().toString().replace("-", "") + suffix
                }
            }
            return File(folder, name)
        }

        private fun computeSize(info: ImageInfo): Int {
            var srcWidth = info.width
            var srcHeight = info.height

            srcWidth = if (srcWidth % 2 == 1) srcWidth + 1 else srcWidth
            srcHeight = if (srcHeight % 2 == 1) srcHeight + 1 else srcHeight

            val longSide: Int = max(srcWidth, srcHeight)
            val shortSide: Int = min(srcWidth, srcHeight)

            val scale = shortSide.toFloat() / longSide
            return if (scale <= 1 && scale > 0.5625) {
                when {
                    longSide < 1664 -> 1
                    longSide < 4990 -> 2
                    longSide in 4991..10239 -> 4
                    else -> longSide / 1280
                }
            } else if (scale <= 0.5625 && scale > 0.5) {
                if (longSide / 1280 == 0) 1 else longSide / 1280
            } else {
                ceil(longSide / (1280.0 / scale)).toInt()
            }
        }

        private fun compressFormat2MimeType(): String {
            return when (compressFormat) {
                Bitmap.CompressFormat.JPEG -> MimeType.Image.JPEG
                Bitmap.CompressFormat.PNG -> MimeType.Image.PNG
                Bitmap.CompressFormat.WEBP -> MimeType.Image.WEBP
            }

        }

    }

    companion object {
        private const val TAG = "Tsar"
        /**
         * 默认的的压缩图片质量
         */
        private const val DEFAULT_COMPRESS_QUALITY = 90

        private const val HANDLER_COMPRESS_ERROR = 1001

        private const val HANDLER_COMPRESS_SUCCESS = 1002

        fun with(context: Context): Builder {
            return Builder(context)
        }
    }

    class Builder(internal val context: Context) {
        /**
         * 图片列表
         */
        internal val imageList: MutableList<ImageWrapper> = mutableListOf()
        /**
         * 图片过滤器的List
         */
        internal var filterList: MutableList<ICompressFilter>? = null
        internal var filterFunctionList: MutableList<((ImageInfo) -> Boolean)>? = null
        /**
         * 保存的路径
         */
        internal var savePath: String? = null
        /**
         * 图片压缩格式
         */
        internal var compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
        /**
         * 图片的压缩质量
         */
        internal var quality: Int = DEFAULT_COMPRESS_QUALITY
        /**
         * 最多多少个线程同时压缩
         */
        internal var threadNumber: Int = 3
        /**
         * 压缩的监听
         */
        internal var completeListener: OnCompleteListener? = null
        internal var completeFunction: ((CompressResult) -> Unit)? = null

        internal var renameAdapter: IRenameAdapter? = null
        internal var renameFunction: ((ImageInfo) -> String)? = null

        /**
         * 压缩文件
         */
        fun compress(file: File): Builder {
            imageList.add(ImageWrapper.file(file))
            return this
        }

        /**
         * 压缩一个Uri
         * 不再支持路径格式，因为Android10已经不能使用绝对路径了
         */
        fun compress(uri: Uri): Builder {
            imageList.add(ImageWrapper.uri(context, uri))
            return this
        }

        /**
         * listUri
         */
        fun compress(listUri: List<Uri>): Builder {
            listUri.forEach { imageList.add(ImageWrapper.uri(context, it)) }
            return this
        }

        /**
         * 压缩后保存的路径
         * 保存的相对路径 例如："XXXCache"
         */
        fun savePath(path: String): Builder {
            this.savePath = path.trim()
            return this
        }

        /**
         * 压缩图片的格式
         * 默认是JPEG
         */
        fun compressFormat(format: Bitmap.CompressFormat): Builder {
            compressFormat = format
            return this
        }

        /**
         * 压缩图片的质量
         * 默认[DEFAULT_COMPRESS_QUALITY]
         */
        fun quality(@IntRange(from = 0, to = 100) quality: Int): Builder {
            this.quality = quality
            return this
        }

        /**
         * 线程池中最多多少个线程
         */
        fun threadNumber(@IntRange(from = 0) number: Int): Builder {
            this.threadNumber = number
            return this
        }

        /**
         * 添加过滤条件
         */
        fun addFilter(filter: ICompressFilter): Builder {
            if (filterList == null) {
                filterList = mutableListOf(filter)
            } else {
                filterList?.add(filter)
            }
            return this
        }


        /**
         * 添加过滤条件
         */
        fun addFilter(filter: ((ImageInfo) -> Boolean)): Builder {
            if (filterFunctionList == null) {
                filterFunctionList = mutableListOf(filter)
            } else {
                filterFunctionList?.add(filter)
            }
            return this
        }

        /**
         * 设置压缩监听
         */
        fun compressListener(listener: OnCompleteListener): Builder {
            completeListener = listener
            return this
        }

        /**
         * 设置压缩监听
         */
        fun compressListener(function: (CompressResult) -> Unit): Builder {
            completeFunction = function
            return this
        }

        /**
         * 设置重命名的Adapter
         */
        fun renameAdapter(adapter: IRenameAdapter): Builder {
            this.renameAdapter = adapter
            return this
        }

        /**
         * 设置重命名的Adapter
         */
        fun renameAdapter(function: (ImageInfo) -> String): Builder {
            this.renameFunction = function
            return this
        }

        /**
         * 开始压缩
         */
        fun start() {
            ImageCompress(this).start()
        }
    }
}