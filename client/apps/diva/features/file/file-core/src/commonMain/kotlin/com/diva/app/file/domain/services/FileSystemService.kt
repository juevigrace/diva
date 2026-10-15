package com.diva.app.file.domain.services

import com.diva.app.file.models.Directory
import io.github.juevigrace.diva.core.permissions.PermissionRequester
import io.github.juevigrace.diva.core.permissions.aggregate
import io.github.juevigrace.diva.core.permissions.toException

interface FileSystemService {
    suspend fun roots(): Result<List<Directory>>

    /**
     * Reads the directory at [path] (which must be one of the roots or a
     * descendant of one of them) and returns it with its immediate children in
     * [Directory.entries]. Never recursive. Fails when [path] escapes every root
     * or cannot be read.
     */
    suspend fun read(path: String): Result<Directory>
}

abstract class BaseFileSystemService(
    protected open val permissions: PermissionRequester
) : FileSystemService {
    protected inline fun <T> withPermission(block: () -> T): Result<T> {
        if (permissions.hasPermission) return runCatching(block)

        return Result.failure(
            permissions.requiredPermissions
                .flatMap { permissions.checkPermission(it).permissions.values }
                .aggregate()
                .toException(),
        )
    }
}
