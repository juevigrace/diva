package com.diva.app.file.domain

import com.diva.app.file.models.DeviceFile
import com.diva.app.file.models.Directory
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow

/**
 * Device-global filesystem cache: reads go through the platform
 * [com.diva.app.file.domain.services.FileSystemService], everything the UI sees
 * comes back from the database.
 */
interface FileRepository : Repository {

    fun observe(): Flow<Result<List<Directory>>>

    fun observe(id: String): Flow<Result<Option<Directory>>>

    fun observeRoots(): Flow<Result<List<Directory>>>

    /** Reactive children of [directoryId]; `null` observes the roots. */
    fun observeChildren(directoryId: String?): Flow<Result<List<Directory>>>

    suspend fun getChildren(parentId: String): Result<List<Directory>>

    /** Root-first parent trail for the breadcrumb bar; empty at the root. */
    suspend fun breadcrumbs(directoryId: String?): List<Directory>

    suspend fun getFiles(parentId: String): Result<List<DeviceFile>>

    /**
     * Rescans [parentId] (or the roots when null) through the platform file
     * system, upserts the fresh listing into the cache, and drops cached
     * children of that directory that no longer exist on disk.
     */
    suspend fun refresh(parentId: String?): Result<Unit>

    suspend fun upsert(directory: Directory): Result<Unit>

    suspend fun delete(id: String): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
