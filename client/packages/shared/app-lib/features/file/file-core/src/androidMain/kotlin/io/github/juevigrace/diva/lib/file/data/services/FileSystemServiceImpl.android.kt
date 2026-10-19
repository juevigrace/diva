package io.github.juevigrace.diva.lib.file.data.services

import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.system.ErrnoException
import android.system.Os
import android.system.OsConstants
import android.system.StructStat
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.file.domain.services.FileSystemService
import io.github.juevigrace.diva.lib.file.domain.services.withRequester
import io.github.juevigrace.diva.lib.file.models.DeviceFile
import io.github.juevigrace.diva.lib.file.models.Directory
import io.github.juevigrace.diva.lib.file.models.EntryMeta
import io.github.juevigrace.diva.lib.file.models.MimeType
import java.io.File

actual class FileSystemServiceImpl(private val context: Context) : FileSystemService {
    private val storageManager: StorageManager? = context.getSystemService(StorageManager::class.java)

    private val roots: List<Root> by lazy {
        buildList {
            add(Root(Environment.getExternalStorageDirectory(), "Internal storage"))
            context.getExternalFilesDirs(null).orEmpty().filterNotNull().forEach { dir ->
                val volume = dir.absolutePath.substringBefore("/Android/data/", "")
                if (volume.isEmpty()) return@forEach
                val file = File(volume)
                if (none { it.canonicalPath == file.canonicalPath }) {
                    val name = storageManager?.getStorageVolume(file)?.getDescription(context) ?: file.name
                    add(Root(file, name))
                }
            }
        }
    }

    actual override suspend fun roots(): Result<List<Directory>> {
        return withRequester {
            roots.map { Directory(meta = it.meta()) }
        }
    }

    actual override suspend fun read(path: String): Result<Directory> {
        return withRequester {
            val base = File(path)
            require(!escapesRoots(base)) { "Path $path is outside the known roots" }

            val names = base.list().orEmpty()
            val prefix = base.path.let { if (it.endsWith(File.separator)) it else it + File.separator }

            val directories = ArrayList<Directory>(names.size)
            val files = ArrayList<DeviceFile>(names.size)

            names.forEach { name ->
                val childPath = prefix + name
                val stat = try {
                    Os.lstat(childPath)
                } catch (_: ErrnoException) {
                    return@forEach
                }
                when {
                    OsConstants.S_ISDIR(stat.st_mode) -> {
                        directories += Directory(
                            meta = metaFrom(
                                name = name,
                                path = childPath,
                                lastModified = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                                    val t = stat.st_mtim
                                    t.tv_sec * 1000 + t.tv_nsec / 1_000_000
                                } else {
                                    stat.st_mtime * 1000
                                },
                            )
                        )
                    }
                    OsConstants.S_ISREG(stat.st_mode) -> {
                        files += fileFrom(name, childPath, stat)
                    }
                }
            }

            directories.sortBy { it.name }
            files.sortBy { it.name }
            Directory(
                meta = metaFrom(base.name, base.path, base.lastModified()),
                entries = directories + files,
            )
        }
    }

    /** One `realpath` for the requested directory, compared against the cached canonical roots. */
    private fun escapesRoots(file: File): Boolean {
        val canonical = file.canonicalPath
        return roots.none { canonical == it.canonicalPath || canonical.startsWith(it.canonicalPath + File.separator) }
    }

    private fun metaFrom(name: String, path: String, lastModified: Long): EntryMeta = EntryMeta(
        id = "",
        name = name,
        path = path,
        lastModified = lastModified,
        hidden = name.startsWith("."),
    )

    private fun fileFrom(name: String, path: String, stat: StructStat): DeviceFile = DeviceFile(
        meta = metaFrom(
            name = name,
            path = path,
            lastModified = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                val t = stat.st_mtim
                t.tv_sec * 1000 + t.tv_nsec / 1_000_000
            } else {
                stat.st_mtime * 1000
            },
        ),
        sizeBytes = stat.st_size,
        mimeType = Option.of(MimeType.fromFileName(name)),
    )

    private class Root(private val file: File, private val name: String) {
        val canonicalPath: String = file.canonicalPath

        fun meta(): EntryMeta = EntryMeta(
            id = "",
            name = name,
            path = file.absolutePath,
            lastModified = file.lastModified(),
        )
    }
}
