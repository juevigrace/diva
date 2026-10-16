package com.diva.app.file.domain

import com.diva.app.file.models.FsEntry
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow

/**
 * Device-global filesystem cache. Listings are pulled lazily, one level at a
 * time: collecting [observeRoot] or [observeDir] rescans just that level from
 * the platform [com.diva.app.file.domain.services.FileSystemService] and writes
 * it to the cache through [refresh]. [observe] exposes the whole cached tree for
 * cross-level consumers such as search.
 */
interface FileRepository : Repository {

    /** Every cached entry, directories and files alike. Used by search. */
    fun observe(): Flow<Result<List<FsEntry>>>

    /** Rescans the roots and emits the first-level entries, minus hidden roots. */
    fun observeRoot(): Flow<Result<List<FsEntry>>>

    /**
     * Rescans [id] and emits its immediate children (directories and files).
     * The scan is driven by collection, so only levels the UI navigates into
     * are read. Emits a failure and then the cached children when the rescan
     * fails.
     */
    fun observeDir(id: String): Flow<Result<List<FsEntry>>>

    /**
     * Rescans [parentId] (or the roots when null) through the platform file
     * system, upserts the fresh listing into the cache, and drops cached
     * children of that directory that no longer exist on disk.
     */
    suspend fun refresh(parentId: String?): Result<Unit>
}
