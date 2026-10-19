package io.github.juevigrace.diva.lib.file.data.services

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.file.domain.services.FileSystemService
import io.github.juevigrace.diva.lib.file.domain.services.withRequester
import io.github.juevigrace.diva.lib.file.models.DeviceFile
import io.github.juevigrace.diva.lib.file.models.Directory
import io.github.juevigrace.diva.lib.file.models.EntryMeta
import io.github.juevigrace.diva.lib.file.models.MimeType
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDate
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSNumber
import platform.Foundation.NSURL
import platform.Foundation.NSURLContentModificationDateKey
import platform.Foundation.NSURLFileSizeKey
import platform.Foundation.NSURLIsDirectoryKey
import platform.Foundation.NSUserDomainMask
import platform.Foundation.timeIntervalSince1970

actual class FileSystemServiceImpl : FileSystemService {
    private val root: String = (
        NSFileManager.defaultManager
            .URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
            .firstOrNull() as? NSURL
        )?.path
        ?: error("Documents directory is unavailable")

    actual override suspend fun roots(): Result<List<Directory>> {
        return withRequester {
            listOf(directoryFrom(NSURL.fileURLWithPath(root)))
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual override suspend fun read(path: String): Result<Directory> {
        return withRequester {
            val rootPrefix = if (root.endsWith("/")) root else "$root/"
            require(path == root || path.startsWith(rootPrefix)) {
                "Path $path is outside the root $root"
            }

            val parentUrl = NSURL.fileURLWithPath(path)
            val children = NSFileManager.defaultManager.contentsOfDirectoryAtURL(
                url = parentUrl,
                includingPropertiesForKeys = RESOURCE_KEYS,
                options = 0u,
                error = null,
            ) ?: emptyList<Any>()

            val directories = ArrayList<Directory>()
            val files = ArrayList<DeviceFile>()
            children.filterIsInstance<NSURL>().forEach { child ->
                val values = child.resourceValuesForKeys(RESOURCE_KEYS, null) ?: return@forEach
                val name = child.lastPathComponent ?: ""
                val meta = metaFrom(child, modificationDateMs(values))
                val isDirectory = (values[NSURLIsDirectoryKey] as? NSNumber)?.boolValue ?: false
                if (isDirectory) {
                    directories += Directory(meta = meta)
                } else {
                    files += DeviceFile(
                        meta = meta,
                        sizeBytes = (values[NSURLFileSizeKey] as? NSNumber)?.longLongValue ?: 0L,
                        mimeType = Option.of(MimeType.fromFileName(name)),
                    )
                }
            }
            Directory(
                meta = metaFrom(parentUrl, modificationDateMs(parentUrl)),
                entries = directories.sortedBy { it.name } + files.sortedBy { it.name },
            )
        }
    }

    private fun metaFrom(url: NSURL, lastModified: Long): EntryMeta = EntryMeta(
        id = "",
        name = url.lastPathComponent ?: "",
        path = url.path ?: error("NSURL has no path"),
        lastModified = lastModified,
    )

    private fun directoryFrom(url: NSURL): Directory = Directory(meta = metaFrom(url, modificationDateMs(url)))

    @OptIn(ExperimentalForeignApi::class)
    private fun modificationDateMs(url: NSURL): Long =
        (
            url.resourceValuesForKeys(listOf(NSURLContentModificationDateKey), null)
                ?.get(NSURLContentModificationDateKey) as? NSDate
            )
            ?.timeIntervalSince1970
            ?.let { (it * 1000).toLong() } ?: 0L

    private fun modificationDateMs(values: Map<Any?, *>): Long =
        (values[NSURLContentModificationDateKey] as? NSDate)
            ?.timeIntervalSince1970
            ?.let { (it * 1000).toLong() } ?: 0L

    private companion object {
        val RESOURCE_KEYS = listOf(NSURLIsDirectoryKey, NSURLFileSizeKey, NSURLContentModificationDateKey)
    }
}
