package io.github.juevigrace.diva.lib.file.domain.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.exifinterface.media.ExifInterface
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.Some
import io.github.juevigrace.diva.lib.file.models.DeviceFile
import io.github.juevigrace.diva.lib.file.models.FileCategory
import io.github.juevigrace.diva.lib.file.models.MimeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ThumbnailServiceAndroid(private val context: Context) : ThumbnailService {
    private val mutexes = mutableMapOf<String, Mutex>()
    private val cacheDir: File by lazy {
        File(context.cacheDir, "thumbnails").apply { mkdirs() }
    }

    private fun mutexFor(key: String): Mutex = synchronized(mutexes) {
        mutexes.getOrPut(key) { Mutex() }
    }

    override suspend fun getThumbnail(entry: DeviceFile, sizePx: Int): Option<ByteArray> = withContext(Dispatchers.IO) {
        val mime = entry.mimeType.getOrNull()
        if (entry.category != FileCategory.IMAGE && entry.category != FileCategory.VIDEO) {
            if (mime != MimeType.PNG && mime != MimeType.JPEG && mime != MimeType.WEBP &&
                mime != MimeType.GIF && mime != MimeType.BMP && mime != MimeType.MP4 &&
                mime != MimeType.MKV && mime != MimeType.WEBM && mime != MimeType.MOV &&
                mime != MimeType.AVI
            ) {
                return@withContext None
            }
        }
        val sizeBytes = entry.sizeBytes
        val key = "${entry.meta.id}_${sizePx}_${entry.meta.lastModified}_$sizeBytes"
        mutexFor(key).withLock {
            val cached = File(cacheDir, "$key.webp")
            if (cached.exists() && cached.length() > 0) {
                return@withLock Some(cached.readBytes())
            }
            val bytes = tryLoadFromMediaStore(entry, sizePx) ?: tryLoadManual(entry, sizePx)
            if (bytes == null) {
                return@withLock None
            }
            try {
                FileOutputStream(cached).use { it.write(bytes) }
            } catch (_: Throwable) {
            }
            Some(bytes)
        }
    }

    private fun tryLoadFromMediaStore(entry: DeviceFile, sizePx: Int): ByteArray? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return null
        val uriStr = entry.contentUri.getOrNull() ?: return null
        return try {
            val uri = Uri.parse(uriStr)
            val resolver = context.contentResolver
            val bitmap = resolver.loadThumbnail(uri, Size(sizePx, sizePx), null)
            bitmapToBytes(bitmap)
        } catch (_: Throwable) {
            null
        }
    }

    private fun tryLoadManual(entry: DeviceFile, sizePx: Int): ByteArray? {
        val path = entry.meta.path
        return try {
            when (entry.category) {
                FileCategory.IMAGE -> decodeImage(path, sizePx)
                FileCategory.VIDEO -> decodeVideo(path, sizePx)
                else -> {
                    val mime = entry.mimeType.getOrNull()
                    if (mime == MimeType.PNG || mime == MimeType.JPEG || mime == MimeType.WEBP ||
                        mime == MimeType.GIF || mime == MimeType.BMP
                    ) {
                        decodeImage(path, sizePx)
                    } else if (mime == MimeType.MP4 || mime == MimeType.MKV || mime == MimeType.WEBM ||
                        mime == MimeType.MOV || mime == MimeType.AVI
                    ) {
                        decodeVideo(path, sizePx)
                    } else {
                        null
                    }
                }
            }
        } catch (_: Throwable) {
            null
        }
    }

    private fun decodeImage(path: String, sizePx: Int): ByteArray? {
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(path, options)
        options.inSampleSize = calculateInSampleSize(options, sizePx, sizePx)
        options.inJustDecodeBounds = false
        val bitmap = BitmapFactory.decodeFile(path, options) ?: return null
        val oriented = rotateBitmapIfNeeded(bitmap, path)
        val scaled = scaleBitmap(oriented, sizePx)
        val bytes = bitmapToBytes(scaled)
        if (scaled != oriented) scaled.recycle()
        if (oriented != bitmap) oriented.recycle()
        return bytes
    }

    private fun decodeVideo(path: String, sizePx: Int): ByteArray? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(path)
            val frame = retriever.getFrameAtTime(0) ?: return null
            val scaled = scaleBitmap(frame, sizePx)
            val bytes = bitmapToBytes(scaled)
            if (scaled != frame) scaled.recycle()
            frame.recycle()
            bytes
        } catch (_: Throwable) {
            null
        } finally {
            try {
                retriever.release()
            } catch (_: Throwable) {
            }
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun rotateBitmapIfNeeded(bitmap: Bitmap, path: String): Bitmap {
        return try {
            val exif = ExifInterface(path)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL,
            )
            val matrix = android.graphics.Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                else -> return bitmap
            }
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (_: Throwable) {
            bitmap
        }
    }

    private fun scaleBitmap(bitmap: Bitmap, sizePx: Int): Bitmap {
        if (bitmap.width <= sizePx && bitmap.height <= sizePx) return bitmap
        val ratio = minOf(sizePx.toFloat() / bitmap.width, sizePx.toFloat() / bitmap.height)
        val width = (bitmap.width * ratio).toInt()
        val height = (bitmap.height * ratio).toInt()
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    private fun bitmapToBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 80, stream)
        return stream.toByteArray()
    }
}

actual fun createThumbnailService(): ThumbnailService {
    val context = org.koin.mp.KoinPlatformTools.defaultContext().get().get<android.content.Context>()
    return ThumbnailServiceAndroid(context)
}
