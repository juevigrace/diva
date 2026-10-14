package com.diva.app.file.di

import android.os.Environment
import com.diva.app.file.domain.services.FileSystemService
import com.diva.app.file.domain.services.guessMimeType
import com.diva.app.file.models.DeviceFile
import com.diva.app.file.models.Directory
import com.diva.app.file.models.DirectoryListing
import io.github.juevigrace.diva.core.None
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
            directories = children.filter { it.isDirectory }.map(::directoryFrom).sortedBy { it.name },
            files = children.filter { it.isFile }.map(::fileFrom).sortedBy { it.name },
        )
    }

    private fun directoryFrom(file: File): Directory = Directory(
        id = Uuid.random().toString(),
        name = file.name,
        path = file.absoluteFile.normalize().path,
        parent = None,
    )

    private fun fileFrom(file: File): DeviceFile = DeviceFile(
        id = Uuid.random().toString(),
        name = file.name,
        path = file.absoluteFile.normalize().path,
        parent = None,
        mimeType = Option.of(guessMimeType(file.name)),
        sizeBytes = file.length(),
        lastModified = file.lastModified(),
    )
}

actual fun platformFileSystemServiceModule(): Module = module {
    single<FileSystemService> { AndroidFileSystemService() }
}
