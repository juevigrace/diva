package com.diva.app.file.di

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
import java.nio.file.Files
import java.nio.file.Path
import kotlin.uuid.Uuid

class JvmFileSystemService : FileSystemService {

    override suspend fun roots(): List<Directory> = withContext(Dispatchers.IO) {
        listOf(directoryFrom(Path.of(System.getProperty("user.home"))))
    }

    override suspend fun listChildren(parent: Directory): DirectoryListing = withContext(Dispatchers.IO) {
        val base = Path.of(parent.path)
        val directories = mutableListOf<Directory>()
        val files = mutableListOf<DeviceFile>()
        Files.list(base).use { stream ->
            stream.forEach { child ->
                when {
                    Files.isDirectory(child) -> directories += directoryFrom(child)
                    Files.isRegularFile(child) -> files += fileFrom(child)
                }
            }
        }
        DirectoryListing(
            directories = directories.sortedBy { it.name },
            files = files.sortedBy { it.name },
        )
    }

    private fun directoryFrom(path: Path): Directory = Directory(
        id = Uuid.random().toString(),
        name = path.fileName?.toString() ?: path.toString(),
        path = path.toAbsolutePath().normalize().toString(),
        parent = None,
    )

    private fun fileFrom(path: Path): DeviceFile {
        val fileName = path.fileName.toString()
        return DeviceFile(
            id = Uuid.random().toString(),
            name = fileName,
            path = path.toAbsolutePath().normalize().toString(),
            parent = None,
            mimeType = Option.of(guessMimeType(fileName)),
            sizeBytes = Files.size(path),
            lastModified = Files.getLastModifiedTime(path).toMillis(),
        )
    }
}

actual fun platformFileSystemServiceModule(): Module = module {
    single<FileSystemService> { JvmFileSystemService() }
}
