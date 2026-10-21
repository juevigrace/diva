package io.github.juevigrace.diva.lib.file.data.services

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.os.storage.StorageManager
import android.system.ErrnoException
import android.system.Os
import android.system.OsConstants
import android.system.StructStat
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.toOption
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

    private fun fileFrom(name: String, path: String, stat: StructStat): DeviceFile {
        val lastModified = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            val t = stat.st_mtim
            t.tv_sec * 1000 + t.tv_nsec / 1_000_000
        } else {
            stat.st_mtime * 1000
        }
        val contentUri = resolveContentUri(path, name)
        return DeviceFile(
            meta = metaFrom(name = name, path = path, lastModified = lastModified),
            sizeBytes = stat.st_size,
            mimeType = Option.of(MimeType.fromFileName(name)),
            contentUri = contentUri.toOption(),
        )
    }

    private fun resolveContentUri(path: String, name: String): String? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                resolveContentUriApi29(path, name)
            } else {
                resolveContentUriLegacy(path, name)
            }
        } catch (_: Throwable) {
            null
        }
    }

    private fun resolveContentUriApi29(path: String, name: String): String? {
        val resolver = context.contentResolver
        val collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val projection = arrayOf(MediaStore.Files.FileColumns._ID)
        val selection = "${MediaStore.Files.FileColumns.RELATIVE_PATH} = ? AND ${MediaStore.Files.FileColumns.DISPLAY_NAME} = ?"
        val relativePath = getRelativePath(path)
        val args = arrayOf(relativePath, name)
        resolver.query(collection, projection, selection, args, null)?.use { cursor: Cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
                return ContentUris.withAppendedId(collection, id).toString()
            }
        }
        return null
    }

    private fun resolveContentUriLegacy(path: String, name: String): String? {
        val resolver = context.contentResolver
        val collection = MediaStore.Files.getContentUri("external")
        val projection = arrayOf(MediaStore.Files.FileColumns._ID)
        val selection = "${MediaStore.MediaColumns.DATA} = ?"
        val args = arrayOf(path)
        resolver.query(collection, projection, selection, args, null)?.use { cursor: Cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
                return ContentUris.withAppendedId(collection, id).toString()
            }
        }
        return null
    }

    private fun getRelativePath(path: String): String {
        val external = Environment.getExternalStorageDirectory().absolutePath
        return if (path.startsWith(external)) {
            val relative = path.substring(external.length)
            val trimmed = relative.trimStart(File.separator)
            if (trimmed.isEmpty()) "" else "$trimmed${File.separator}"
        } else {
            val file = File(path)
            "${file.parentFile?.name.orEmpty()}${if (file.parentFile?.name.isNullOrEmpty()) "" else File.separator}"
        }
    }

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
