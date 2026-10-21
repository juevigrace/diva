package io.github.juevigrace.diva.lib.file.domain.services

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
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.writeToFile
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation

class ThumbnailServiceIos : ThumbnailService {
    private val mutexes = mutableMapOf<String, Mutex>()
    private val cacheDir = NSFileManager.defaultManager.temporaryDirectory

    private fun mutexFor(key: String): Mutex = synchronized(mutexes) {
        mutexes.getOrPut(key) { Mutex() }
    }

    override suspend fun getThumbnail(entry: DeviceFile, sizePx: Int): Option<ByteArray> = withContext(Dispatchers.IO) {
        val mime = entry.mimeType.getOrNull()
        if (entry.category != FileCategory.IMAGE) {
            if (mime != MimeType.PNG && mime != MimeType.JPEG && mime != MimeType.WEBP &&
                mime != MimeType.GIF && mime != MimeType.BMP
            ) {
                return@withContext None
            }
        }
        val sizeBytes = entry.sizeBytes
        val key = "${entry.meta.id}_${sizePx}_${entry.meta.lastModified}_$sizeBytes"
        mutexFor(key).withLock {
            val cachedPath = "${cacheDir.path}$key.jpg"
            if (NSFileManager.defaultManager.fileExistsAtPath(cachedPath)) {
                val data = NSData.dataWithContentsOfFile(cachedPath)
                if (data != null) {
                    return@withLock Some(ByteArray(data.length.toInt()))
                }
            }
            val bytes = try {
                val image = UIImage.imageWithContentsOfFile(entry.meta.path) ?: return@withLock None
                val scaled = scaleImage(image, sizePx.toDouble())
                val jpeg = UIImageJPEGRepresentation(scaled, 0.8) ?: return@withLock None
                jpeg.writeToFile(cachedPath, atomically = true)
                ByteArray(jpeg.length.toInt())
            } catch (_: Throwable) {
                null
            }
            if (bytes == null) None else Some(bytes)
        }
    }

    private fun scaleImage(image: UIImage, sizePx: Double): UIImage {
        val cgImage = image.CGImage ?: return image
        val width = platform.CoreGraphics.CGImageGetWidth(cgImage).toDouble()
        val height = platform.CoreGraphics.CGImageGetHeight(cgImage).toDouble()
        if (width <= sizePx && height <= sizePx) return image
        val ratio = minOf(sizePx / width, sizePx / height)
        val newWidth = width * ratio
        val newHeight = height * ratio
        val rect = platform.CoreGraphics.CGRectMake(0.0, 0.0, newWidth, newHeight)
        val scaled = platform.CoreGraphics.CGImageCreateWithImageInRect(cgImage, rect) ?: return image
        return UIImage.imageWithCGImage(scaled)
    }
}

actual fun createThumbnailService(): ThumbnailService {
    return ThumbnailServiceIos()
}
