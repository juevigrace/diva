package io.github.juevigrace.diva.lib.file.domain.services

import io.github.juevigrace.diva.core.ioDispatcher
import io.github.juevigrace.diva.core.permissions.PermissionStatus
import io.github.juevigrace.diva.core.permissions.aggregate
import io.github.juevigrace.diva.core.permissions.toException
import io.github.juevigrace.diva.lib.file.domain.services.permissions.StoragePermissionRequester
import io.github.juevigrace.diva.lib.file.models.Directory
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext

interface FileSystemService {
    val requester: StoragePermissionRequester
        get() = StoragePermissionRequester

    suspend fun roots(): Result<List<Directory>>

    /**
     * Absolute path the browser should open on, or null when the platform has
     * no preferred starting point and the roots list should be shown first.
     * Only desktop overrides this today; Android browses its storage volumes,
     * which are all worth listing up front.
     */
    suspend fun defaultPath(): Result<String?> = Result.success(null)

    /**
     * Reads the directory at [path] (which must be one of the roots or a
     * descendant of one of them) and returns it with its immediate children in
     * [Directory.entries]. Never recursive. Fails when [path] escapes every root
     * or cannot be read.
     */
    suspend fun read(path: String): Result<Directory>
}

/**
 * Runs [block] off the main thread once the storage permission is confirmed.
 * The permission is inspected exactly once and, on failure, the most severe
 * [PermissionStatus] is surfaced as a
 * [io.github.juevigrace.diva.core.permissions.PermissionException].
 * Cancellation is never swallowed.
 */
suspend inline fun <T> FileSystemService.withRequester(crossinline block: () -> T): Result<T> {
    val status = requester.requiredPermissions
        .flatMap { requester.checkPermission(it).permissions.values }
        .aggregate()
    if (status != PermissionStatus.Granted) return Result.failure(status.toException())

    return runCatching { withContext(ioDispatcher) { block() } }
        .onFailure { if (it is CancellationException) throw it }
}
