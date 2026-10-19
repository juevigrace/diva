package io.github.juevigrace.diva.lib.file.data.services

import io.github.juevigrace.diva.lib.file.domain.services.FileSystemService
import io.github.juevigrace.diva.lib.file.models.Directory

actual class FileSystemServiceImpl : FileSystemService {
    actual override suspend fun roots(): Result<List<Directory>> =
        Result.success(emptyList())

    actual override suspend fun read(path: String): Result<Directory> =
        Result.failure(IllegalStateException("Local file access is not available on the web"))
}
