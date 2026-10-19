package io.github.juevigrace.diva.lib.file.data.services

import io.github.juevigrace.diva.lib.file.domain.services.FileSystemService
import io.github.juevigrace.diva.lib.file.models.Directory

expect class FileSystemServiceImpl : FileSystemService {
    override suspend fun roots(): Result<List<Directory>>
    override suspend fun read(path: String): Result<Directory>
}
