package com.diva.app.file.data.services

import android.content.Context
import android.os.Environment
import android.os.storage.StorageManager
import com.diva.app.file.domain.services.FileSystemService
import com.diva.app.file.domain.services.withRequester
import com.diva.app.file.models.DeviceFile
import com.diva.app.file.models.Directory
import com.diva.app.file.models.EntryMeta
import com.diva.app.file.models.MimeType
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual class FileSystemServiceImpl(private val context: Context) : FileSystemService {
    private val storageManager: StorageManager? = context.getSystemService(StorageManager::class.java)
    private val roots: List<Pair<File, String>> by lazy {
        buildList {
            add(Environment.getExternalStorageDirectory() to "Internal storage")
            context.getExternalFilesDirs(null).orEmpty().filterNotNull().forEach { dir ->
                val volume = dir.absolutePath.substringBefore("/Android/data/", "")
                val file = File(volume)
                if (volume.isNotEmpty() && none { it.first.canonicalPath == file.canonicalPath }) {
                    val name = storageManager
                        ?.getStorageVolume(file)
                        ?.getDescription(context)
                        ?: file.name
                    add(file to name)
                }
            }
        }
    }

    actual override suspend fun roots(): Result<List<Directory>> {
        return withRequester {
            roots.map { (file, name) -> directoryFrom(file, name) }
        }
    }

    actual override suspend fun read(path: String): Result<Directory> {
        return withRequester {
            withContext(Dispatchers.IO) {
                val base = File(path)
                require(isInsideRoots(base.canonicalPath)) { "Path $path is outside the known roots" }

                val children = base.listFiles().orEmpty()
                val directories = children
                    .filter { it.isDirectory }
                    .map(::directoryFrom)
                    .sortedBy { it.name }
                val files = children
                    .filter { it.isFile }
                    .map(::fileFrom)
                    .sortedBy { it.name }
                Directory(
                    meta = metaFrom(base),
                    entries = directories + files,
                )
            }
        }
    }

    private fun isInsideRoots(canonicalPath: String): Boolean {
        return roots.any { (file, _) ->
            val root = file.canonicalPath
            canonicalPath == root || canonicalPath.startsWith(root + File.separator)
        }
    }

    private fun metaFrom(file: File): EntryMeta = EntryMeta(
        id = "",
        name = file.name,
        path = file.absolutePath,
        lastModified = file.lastModified(),
    )

    private fun directoryFrom(file: File, name: String? = null): Directory {
        val meta = metaFrom(file)
        return Directory(meta = if (name == null) meta else meta.copy(name = name))
    }

    private fun fileFrom(file: File): DeviceFile = DeviceFile(
        meta = metaFrom(file),
        sizeBytes = file.length(),
        mimeType = Option.of(MimeType.fromFileName(file.name)),
    )
}
