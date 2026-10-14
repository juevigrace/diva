package com.diva.app.file.di

import com.diva.app.file.domain.services.FileSystemService
import com.diva.app.file.domain.services.guessMimeType
import com.diva.app.file.models.DeviceFile
import com.diva.app.file.models.Directory
import com.diva.app.file.models.DirectoryListing
import com.diva.app.file.models.EntryMeta
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDate
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSNumber
import platform.Foundation.NSURL
import platform.Foundation.NSURLContentModificationDateKey
import platform.Foundation.NSURLFileSizeKey
import platform.Foundation.NSURLIsDirectoryKey
import platform.Foundation.NSUserDomainMask
import kotlin.uuid.Uuid

/**
 * V1 is sandboxed to the app's Documents directory; a security-scoped
 * bookmark based reader can replace this without touching the common interfaces.
 */
class IosFileSystemService : FileSystemService {

    override suspend fun roots(): List<Directory> = withContext(Dispatchers.Default) {
        val documents = NSFileManager.defaultManager
            .URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
            .firstOrNull() as? NSURL
            ?: return@withContext emptyList()
        listOf(directoryFrom(documents))
    }

    override suspend fun listChildren(parent: Directory): DirectoryListing = withContext(Dispatchers.Default) {
        val parentUrl = NSURL.fileURLWithPath(parent.path)
        val children = NSFileManager.defaultManager.contentsOfDirectoryAtURL(
            url = parentUrl,
            includingPropertiesForKeys = listOf(NSURLIsDirectoryKey, NSURLFileSizeKey, NSURLContentModificationDateKey),
            options = 0u,
            error = null,
        ) as? List<*> ?: emptyList<Any>()
        val directories = mutableListOf<Directory>()
        val files = mutableListOf<DeviceFile>()
        children.filterIsInstance<NSURL>().forEach { child ->
            val values = child.resourceValuesForKeys(
                listOf(NSURLIsDirectoryKey, NSURLFileSizeKey, NSURLContentModificationDateKey),
            )
            val isDirectory = (values?.objectForKey(NSURLIsDirectoryKey) as? NSNumber)?.boolValue ?: false
            if (isDirectory) {
                directories += directoryFrom(child)
            } else {
                files += fileFrom(
                    url = child,
                    sizeBytes = (values?.objectForKey(NSURLFileSizeKey) as? NSNumber)?.longLongValue ?: 0L,
                    lastModifiedMs = modificationDateMs(child),
                )
            }
        }
        DirectoryListing(
            entries = directories.sortedBy { it.name } + files.sortedBy { it.name },
        )
    }

    private fun directoryFrom(url: NSURL): Directory = Directory(
        meta = EntryMeta(
            id = Uuid.random().toString(),
            name = url.lastPathComponent ?: "",
            path = url.path ?: error("NSURL has no path"),
            lastModified = modificationDateMs(url),
        ),
    )

    private fun fileFrom(url: NSURL, sizeBytes: Long, lastModifiedMs: Long): DeviceFile {
        val name = url.lastPathComponent ?: ""
        return DeviceFile(
            meta = EntryMeta(
                id = Uuid.random().toString(),
                name = name,
                path = url.path ?: error("NSURL has no path"),
                lastModified = lastModifiedMs,
            ),
            sizeBytes = sizeBytes,
            mimeType = Option.of(guessMimeType(name)),
        )
    }

    private fun modificationDateMs(url: NSURL): Long =
        (url.resourceValuesForKeys(listOf(NSURLContentModificationDateKey))
            ?.objectForKey(NSURLContentModificationDateKey) as? NSDate)
            ?.timeIntervalSince1970
            ?.let { (it * 1000).toLong() } ?: 0L
}

actual fun platformFileSystemServiceModule(): Module = module {
    single<FileSystemService> { IosFileSystemService() }
}
