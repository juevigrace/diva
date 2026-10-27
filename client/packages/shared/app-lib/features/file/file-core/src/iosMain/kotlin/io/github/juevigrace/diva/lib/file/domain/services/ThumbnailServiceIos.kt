package io.github.juevigrace.diva.lib.file.domain.services

import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.ioDispatcher
import io.github.juevigrace.diva.lib.file.models.DeviceFile
import io.github.juevigrace.diva.lib.file.models.FileCategory
import io.github.juevigrace.diva.lib.file.models.MimeType
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.writeToFile
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.posix.memcpy

class ThumbnailServiceIos : ThumbnailService {
    private val mutexes = mutableMapOf<String, Mutex>()
    private val mutexesGuard = Mutex()
    private val cacheDir = NSTemporaryDirectory().trimEnd('/')

    private suspend fun mutexFor(key: String): Mutex = mutexesGuard.withLock {
        mutexes.getOrPut(key) { Mutex() }
    }

    override suspend fun getThumbnail(entry: DeviceFile, sizePx: Int): Option<ByteArray> = withContext(ioDispatcher) {
        val mime = entry.mimeType.getOrNull()
        if (entry.category != FileCategory.IMAGE) {
            if (mime != MimeType.PNG && mime != MimeType.JPEG && mime != MimeType.WEBP &&
                mime != MimeType.GIF && mime != MimeType.BMP
            ) {
                return@withContext None
            }
        }
        val key = "${entry.meta.id}_${sizePx}_${entry.meta.lastModified}_${entry.sizeBytes}"
        mutexFor(key).withLock {
            val cachedPath = "$cacheDir/$key.jpg"
            val manager = NSFileManager.defaultManager
            if (manager.fileExistsAtPath(cachedPath)) {
                manager.contentsAtPath(cachedPath)
                    ?.toByteArray()
                    ?.let { return@withLock Option.some(it) }
            }
            val image = UIImage.imageWithContentsOfFile(entry.meta.path) ?: return@withLock None
            val jpeg = UIImageJPEGRepresentation(image, 0.8) ?: return@withLock None
            jpeg.writeToFile(cachedPath, atomically = true)
            Option.some(jpeg.toByteArray())
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    if (size == 0) return ByteArray(0)
    return ByteArray(size).apply {
        usePinned { pinned ->
            memcpy(pinned.addressOf(0), bytes, length)
        }
    }
}

actual fun createThumbnailService(): ThumbnailService = ThumbnailServiceIos()
