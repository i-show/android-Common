package com.ishow.common.utils.glide.corner

import android.graphics.*
import android.os.Build
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.util.Preconditions
import com.bumptech.glide.util.Synthetic
import com.bumptech.glide.util.Util
import com.ishow.common.extensions.dp2px
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock

/**
 * A [BitmapTransformation] which rounds the corners of a bitmap.
 */
class GlideCorner(roundingRadius: Int, val position: Position = Position.All) : BitmapTransformation() {

    private val radius: Int = roundingRadius.dp2px()

    init {
        Preconditions.checkArgument(roundingRadius > 0, "radius must be greater than 0.")
    }

    override fun transform(pool: BitmapPool, toTransform2: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        Preconditions.checkArgument(radius > 0, "radius must be greater than 0.")
        val safeConfig = getAlphaSafeConfig(toTransform2)
        val toTransform = getAlphaSafeBitmap(pool, toTransform2)
        val result = pool.get(toTransform.width, toTransform.height, safeConfig)

        result.setHasAlpha(true)

        val shader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = shader
        var rect = RectF(0f, 0f, result.width.toFloat(), result.height.toFloat())
        BITMAP_DRAWABLE_LOCK.lock()
        try {
            rect = fixRect(rect)
            val canvas = Canvas(result)
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            canvas.drawRoundRect(rect, radius.toFloat(), radius.toFloat(), paint)
            clear(canvas)
        } finally {
            BITMAP_DRAWABLE_LOCK.unlock()
        }

        if (toTransform != toTransform2) {
            pool.put(toTransform)
        }
        return result
    }

    @Suppress("ProtectedInFinal", "NON_EXHAUSTIVE_WHEN")
    protected fun fixRect(originRect: RectF): RectF {
        val rect = RectF(originRect)
        when (position) {
            Position.Top -> rect.bottom = originRect.bottom + radius
            Position.Bottom -> rect.top = originRect.top - radius
            Position.Start -> rect.right = originRect.right + radius
            Position.End -> rect.left = originRect.left - radius

            Position.TopStart -> {
                rect.right = originRect.right + radius
                rect.bottom = originRect.bottom + radius
            }

            Position.TopEnd -> {
                rect.bottom = originRect.bottom + radius
                rect.left = originRect.left - radius
            }

            Position.BottomStart -> {
                rect.top = originRect.top - radius
                rect.right = originRect.right + radius
            }

            Position.BottomEnd -> {
                rect.top = originRect.top - radius
                rect.left = originRect.left - radius
            }
        }
        return rect
    }


    override fun equals(other: Any?): Boolean {
        return if (other is GlideCorner) {
            radius == other.radius && position == other.position
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return Util.hashCode(ID.hashCode(), Util.hashCode(radius, position.pos))
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
        val value = position.pos * 10000 + radius
        val radiusData = ByteBuffer.allocate(5)
                .putInt(value)
                .array()

        messageDigest.update(radiusData)
    }

    private class NoLock @Synthetic
    internal constructor() : Lock {

        override fun lock() {
            // do nothing
        }

        @Throws(InterruptedException::class)
        override fun lockInterruptibly() {
            // do nothing
        }

        override fun tryLock(): Boolean {
            return true
        }

        @Throws(InterruptedException::class)
        override fun tryLock(time: Long, unit: TimeUnit): Boolean {
            return true
        }

        override fun unlock() {
            // do nothing
        }

        override fun newCondition(): Condition {
            throw UnsupportedOperationException("Should not be called")
        }
    }

    companion object {
        private const val ID = "com.ishow.common.utils.glide.corner.GlideCorner"
        private val ID_BYTES = ID.toByteArray(Key.CHARSET)

        private val BITMAP_DRAWABLE_LOCK = NoLock()

        private fun getAlphaSafeConfig(inBitmap: Bitmap): Bitmap.Config {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Avoid short circuiting the sdk check.
                if (Bitmap.Config.RGBA_F16 == inBitmap.config) { // NOPMD
                    return Bitmap.Config.RGBA_F16
                }
            }

            return Bitmap.Config.ARGB_8888
        }

        private fun getAlphaSafeBitmap(pool: BitmapPool, maybeAlphaSafe: Bitmap): Bitmap {
            val safeConfig = getAlphaSafeConfig(maybeAlphaSafe)
            if (safeConfig == maybeAlphaSafe.config) {
                return maybeAlphaSafe
            }

            val argbBitmap = pool.get(maybeAlphaSafe.width, maybeAlphaSafe.height, safeConfig)
            Canvas(argbBitmap).drawBitmap(maybeAlphaSafe, 0f /*left*/, 0f /*top*/, null/*paint*/)

            // We now own this Bitmap. It's our responsibility to replace it in the pool outside this method
            // when we're finished with it.
            return argbBitmap
        }

        // Avoids warnings in M+.
        private fun clear(canvas: Canvas) {
            canvas.setBitmap(null)
        }
    }


    enum class Position(val pos: Int) {
        All(0),
        Top(1),
        Bottom(2),
        Start(3),
        End(4),
        TopStart(5),
        TopEnd(6),
        BottomStart(7),
        BottomEnd(8)
    }

}

