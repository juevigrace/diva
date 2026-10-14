package com.diva.app.file.di

import android.os.Environment
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
import java.io.File
import kotlin.uuid.Uuid

/**
 * V1 reads the shared external storage root directly; a Storage-Access-Framework
 * based reader can replace this without touching the common interfaces.
 */
class AndroidFileSystemService : FileSystemService {

    override suspend fun roots(): List<Directory> = withContext(Dispatchers.IO) {
        listOf(directoryFrom(Environment.getExternalStorageDirectory()))
    }

    override suspend fun listChildren(parent: Directory): DirectoryListing = withContext(Dispatchers.IO) {
        val base = File(parent.path)
        val children = base.listFiles().orEmpty()
        DirectoryListing(
            entries = children.filter { it.isDirectory }.map(::directoryFrom).sortedBy { it.name } +
                children.filter { it.isFile }.map(::fileFrom).sortedBy { it.name },
        )
    }

    private fun directoryFrom(file: File): Directory = Directory(
        meta = EntryMeta(
            id = Uuid.random().toString(),
            name = file.name,
            path = file.absoluteFile.normalize().path,
            lastModified = file.lastModified(),
        ),
    )

    private fun fileFrom(file: File): DeviceFile = DeviceFile(
        meta = EntryMeta(
            id = Uuid.random().toString(),
            name = file.name,
            path = file.absoluteFile.normalize().path,
            lastModified = file.lastModified(),
        ),
        sizeBytes = file.length(),
        mimeType = Option.of(guessMimeType(file.name)),
    )
}

actual fun platformFileSystemServiceModule(): Module = module {
    single<FileSystemService> { AndroidFileSystemService() }
}
