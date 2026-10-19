package io.github.juevigrace.diva.lib.file.data.services

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.file.domain.services.FileSystemService
import io.github.juevigrace.diva.lib.file.domain.services.withRequester
import io.github.juevigrace.diva.lib.file.models.DeviceFile
import io.github.juevigrace.diva.lib.file.models.Directory
import io.github.juevigrace.diva.lib.file.models.EntryMeta
import io.github.juevigrace.diva.lib.file.models.MimeType
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

actual class FileSystemServiceImpl : FileSystemService {
    private val roots: List<Path> by lazy {
        buildList {
            add(Path.of(System.getProperty("user.home")))
            File.listRoots().forEach { add(it.toPath()) }
        }
            .map { it.toAbsolutePath().normalize() }
            .distinct()
    }

    actual override suspend fun roots(): Result<List<Directory>> {
        return withRequester {
            roots.map {
                Directory(
                    meta = metaFrom(
                        path = it,
                        lastModified = Files.getLastModifiedTime(it).toMillis()
                    )
                )
            }
        }
    }

    actual override suspend fun read(path: String): Result<Directory> {
        return withRequester {
            val base = Path.of(path).toAbsolutePath().normalize()
            require(roots.any { base.startsWith(it) }) { "Path $path is outside the known roots" }

            val directories = mutableListOf<Directory>()
            val files = mutableListOf<DeviceFile>()
            Files.newDirectoryStream(base).use { stream ->
                stream.forEach { child ->
                    val attrs = Files.readAttributes(child, BasicFileAttributes::class.java) ?: return@forEach
                    when {
                        attrs.isDirectory -> directories += Directory(
                            meta = metaFrom(
                                child,
                                attrs.lastModifiedTime().toMillis()
                            )
                        )
                        attrs.isRegularFile -> files += fileFrom(child, attrs)
                    }
                }
            }
            Directory(
                meta = metaFrom(base, Files.getLastModifiedTime(base).toMillis()),
                entries = directories.sortedBy { it.name } + files.sortedBy { it.name },
            )
        }
    }

    private fun metaFrom(path: Path, lastModified: Long): EntryMeta = EntryMeta(
        id = "",
        name = path.fileName?.toString() ?: path.toString(),
        path = path.toString(),
        lastModified = lastModified,
    )

    private fun fileFrom(path: Path, attrs: BasicFileAttributes): DeviceFile {
        val fileName = path.fileName.toString()
        return DeviceFile(
            meta = metaFrom(path, attrs.lastModifiedTime().toMillis()),
            sizeBytes = attrs.size(),
            mimeType = Option.of(MimeType.fromFileName(fileName)),
        )
    }
}
