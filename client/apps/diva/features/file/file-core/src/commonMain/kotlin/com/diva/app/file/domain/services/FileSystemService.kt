package com.diva.app.file.domain.services

import com.diva.app.file.domain.services.permissions.StoragePermissionRequester
import com.diva.app.file.models.Directory
import io.github.juevigrace.diva.core.permissions.aggregate
import io.github.juevigrace.diva.core.permissions.toException

interface FileSystemService {
    val requester: StoragePermissionRequester
        get() = StoragePermissionRequester

    suspend fun roots(): Result<List<Directory>>

    /**
     * Reads the directory at [path] (which must be one of the roots or a
     * descendant of one of them) and returns it with its immediate children in
     * [Directory.entries]. Never recursive. Fails when [path] escapes every root
     * or cannot be read.
     */
    suspend fun read(path: String): Result<Directory>
}

inline fun <reified T> FileSystemService.withRequester(block: () -> T): Result<T> {
    return with(requester) {
        if (hasPermission) return runCatching(block)

        Result.failure(
            requiredPermissions
                .flatMap { checkPermission(it).permissions.values }
                .aggregate()
                .toException(),
        )
    }
}
