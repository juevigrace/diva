package com.diva.app.file.data

import com.diva.app.file.domain.services.BaseFileSystemService
import com.diva.app.file.models.DeviceFile
import com.diva.app.file.models.Directory
import com.diva.app.file.models.EntryMeta
import com.diva.app.file.models.MimeType
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.permissions.PermissionRequester
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class JvmFileSystemService(
    override val permissions: PermissionRequester,
) : BaseFileSystemService(permissions) {
    private val roots: List<Path> by lazy {
        buildList {
            add(Path.of(System.getProperty("user.home")))
            File.listRoots().forEach { add(it.toPath()) }
        }
            .map { it.toAbsolutePath().normalize() }
            .distinct()
    }

    override suspend fun roots(): Result<List<Directory>> = withPermission {
        roots.map(::directoryFrom)
    }

    override suspend fun read(path: String): Result<Directory> = withPermission {
        withContext(Dispatchers.IO) {
            val base = Path.of(path).toAbsolutePath().normalize()
            require(roots.any { base.startsWith(it) }) { "Path $path is outside the known roots" }

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
            Directory(
                meta = metaFrom(base),
                entries = directories.sortedBy { it.name } + files.sortedBy { it.name },
            )
        }
    }

    private fun metaFrom(path: Path): EntryMeta = EntryMeta(
        id = "",
        name = path.fileName?.toString() ?: path.toString(),
        path = path.toAbsolutePath().normalize().toString(),
        lastModified = Files.getLastModifiedTime(path).toMillis(),
    )

    private fun directoryFrom(path: Path): Directory = Directory(meta = metaFrom(path))

    private fun fileFrom(path: Path): DeviceFile {
        val fileName = path.fileName.toString()
        return DeviceFile(
            meta = metaFrom(path),
            sizeBytes = Files.size(path),
            mimeType = Option.of(MimeType.fromFileName(fileName)),
        )
    }
}
