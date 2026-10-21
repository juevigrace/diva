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
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

class ThumbnailServiceJvm(private val cacheDirBase: File) : ThumbnailService {
    private val mutexes = mutableMapOf<String, Mutex>()
    private val cacheDir: File by lazy {
        File(cacheDirBase, "thumbnails").apply { mkdirs() }
    }

    private fun mutexFor(key: String): Mutex = synchronized(mutexes) {
        mutexes.getOrPut(key) { Mutex() }
    }

    override suspend fun getThumbnail(entry: DeviceFile, sizePx: Int): Option<ByteArray> = withContext(Dispatchers.IO) {
        val mime = when (val m = entry.mimeType) {
            is Some<*> -> m.value as MimeType?
            else -> null
        }
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
            val cached = File(cacheDir, "$key.jpg")
            if (cached.exists() && cached.length() > 0) {
                return@withLock Option.some(cached.readBytes())

            }
            val bytes = try {
                decodeImage(entry.meta.path, sizePx)
            } catch (_: Throwable) {
                null
            }
            if (bytes == null) {
                return@withLock None
            }
            try {
                cached.writeBytes(bytes)
            } catch (_: Throwable) {
            }
            return@withLock io.github.juevigrace.diva.core.Option.of(bytes)


        }
    }

    private fun decodeImage(path: String, sizePx: Int): ByteArray? {
        val input = File(path)
        if (!input.exists()) return null
        val original = ImageIO.read(input) ?: return null
        val ratio = minOf(sizePx.toFloat() / original.width, sizePx.toFloat() / original.height)
        if (ratio >= 1f) {
            val out = ByteArrayOutputStream()
            ImageIO.write(original, "jpg", out)
            return out.toByteArray()
        }
        val width = (original.width * ratio).toInt()
        val height = (original.height * ratio).toInt()
        val scaled = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val g: Graphics2D = scaled.createGraphics()
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.drawImage(original, 0, 0, width, height, null)
        g.dispose()
        val out = ByteArrayOutputStream()
        ImageIO.write(scaled, "jpg", out)
        return out.toByteArray()
    }
}

actual fun createThumbnailService(): ThumbnailService {
    return ThumbnailServiceJvm(File(System.getProperty("user.home"), ".cache/diva"))
}
