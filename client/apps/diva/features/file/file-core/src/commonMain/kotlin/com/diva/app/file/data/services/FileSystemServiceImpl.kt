package com.diva.app.file.data.services

import com.diva.app.file.domain.services.FileSystemService
import com.diva.app.file.models.Directory

expect class FileSystemServiceImpl : FileSystemService {
    override suspend fun roots(): Result<List<Directory>>
    override suspend fun read(path: String): Result<Directory>
}
