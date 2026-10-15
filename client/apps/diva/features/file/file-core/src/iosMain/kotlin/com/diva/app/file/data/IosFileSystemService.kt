package com.diva.app.file.data

import com.diva.app.file.domain.services.FileSystemService
import com.diva.app.file.models.DeviceFile
import com.diva.app.file.models.Directory
import com.diva.app.file.models.EntryMeta
import com.diva.app.file.models.MimeType
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.permissions.PermissionRequester
import io.github.juevigrace.diva.core.permissions.aggregate
import io.github.juevigrace.diva.core.permissions.toException
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

/**
 * V1 is sandboxed to the app's Documents directory; a security-scoped bookmark
 * based reader can replace this without touching the common interfaces.
 */
class IosFileSystemService(
    private val permissions: PermissionRequester,
) : FileSystemService {

    private val root: String = (
        NSFileManager.defaultManager
            .URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
            .firstOrNull() as? NSURL
        )?.path
        ?: error("Documents directory is unavailable")

    override suspend fun roots(): Result<List<Directory>> = withPermission {
        listOf(directoryFrom(NSURL.fileURLWithPath(root)))
    }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun read(path: String): Result<Directory> = withPermission {
        withContext(Dispatchers.Default) {
            val rootPrefix = if (root.endsWith("/")) root else "$root/"
            require(path == root || path.startsWith(rootPrefix)) {
                "Path $path is outside the root $root"
            }

            val parentUrl = NSURL.fileURLWithPath(path)
            val children = NSFileManager.defaultManager.contentsOfDirectoryAtURL(
                url = parentUrl,
                includingPropertiesForKeys = listOf(
                    NSURLIsDirectoryKey,
                    NSURLFileSizeKey,
                    NSURLContentModificationDateKey
                ),
                options = 0u,
                error = null,
            ) ?: emptyList<Any>()

            val directories = mutableListOf<Directory>()
            val files = mutableListOf<DeviceFile>()
            children.filterIsInstance<NSURL>().forEach { child ->
                val values = child.resourceValuesForKeys(
                    listOf(NSURLIsDirectoryKey, NSURLFileSizeKey, NSURLContentModificationDateKey),
                    null,
                )
                val isDirectory = (values?.get(NSURLIsDirectoryKey) as? NSNumber)?.boolValue ?: false
                if (isDirectory) {
                    directories += directoryFrom(child)
                } else {
                    files += fileFrom(
                        url = child,
                        sizeBytes = (values?.get(NSURLFileSizeKey) as? NSNumber)?.longLongValue ?: 0L,
                        lastModifiedMs = modificationDateMs(child),
                    )
                }
            }
            Directory(
                meta = metaFrom(parentUrl),
                entries = directories.sortedBy { it.name } + files.sortedBy { it.name },
            )
        }
    }

    private inline fun <T> withPermission(block: () -> T): Result<T> {
        if (permissions.hasPermission) return runCatching(block)

        return Result.failure(
            permissions.requiredPermissions
                .flatMap { permissions.checkPermission(it).permissions.values }
                .aggregate()
                .toException(),
        )
    }

    private fun metaFrom(url: NSURL): EntryMeta = EntryMeta(
        id = "",
        name = url.lastPathComponent ?: "",
        path = url.path ?: error("NSURL has no path"),
        lastModified = modificationDateMs(url),
    )

    private fun directoryFrom(url: NSURL): Directory = Directory(meta = metaFrom(url))

    private fun fileFrom(url: NSURL, sizeBytes: Long, lastModifiedMs: Long): DeviceFile {
        val name = url.lastPathComponent ?: ""
        return DeviceFile(
            meta = metaFrom(url).copy(lastModified = lastModifiedMs),
            sizeBytes = sizeBytes,
            mimeType = Option.of(MimeType.fromFileName(name)),
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun modificationDateMs(url: NSURL): Long =
        (
            url.resourceValuesForKeys(listOf(NSURLContentModificationDateKey), null)
                ?.get(NSURLContentModificationDateKey) as? NSDate
            )
            ?.timeIntervalSince1970
            ?.let { (it * 1000).toLong() } ?: 0L
}
