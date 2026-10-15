package com.diva.app.file.domain

import com.diva.app.file.models.Directory
import com.diva.app.file.models.FsEntry
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow

/**
 * Device-global filesystem cache. The whole cached tree is exposed through
 * [observe]; the UI filters it by parent to render a single level. Listings are
 * pulled from the platform [com.diva.app.file.domain.services.FileSystemService]
 * and written to the cache through [refresh].
 */
interface FileRepository : Repository {

    /** Every cached entry, directories and files alike. */
    fun observe(): Flow<Result<List<FsEntry>>>

    /** Root-first parent trail for the breadcrumb bar; empty at the root. */
    suspend fun breadcrumbs(directoryId: String?): List<Directory>

    /**
     * Rescans [parentId] (or the roots when null) through the platform file
     * system, upserts the fresh listing into the cache, and drops cached
     * children of that directory that no longer exist on disk.
     */
    suspend fun refresh(parentId: String?): Result<Unit>
}
