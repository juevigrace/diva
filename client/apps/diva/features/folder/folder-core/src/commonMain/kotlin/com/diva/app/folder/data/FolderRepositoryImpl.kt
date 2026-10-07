package com.diva.app.folder.data

import com.diva.app.folder.database.FolderStorage
import com.diva.app.folder.database.MediaFolderLinkStorage
import com.diva.app.folder.domain.FolderRepository
import com.diva.app.folder.models.Folder
import com.diva.app.media.models.Media
import io.github.juevigrace.diva.core.getOrThrow
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.domain.observeSession
import io.github.juevigrace.diva.lib.session.domain.withSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FolderRepositoryImpl(
    private val storage: FolderStorage,
    private val linkStorage: MediaFolderLinkStorage,
    private val sessionRepository: SessionRepository,
) : FolderRepository {

    override fun observe(): Flow<Result<List<Folder>>> = observeSession(sessionRepository::observe) { session ->
        storage.getByUserFlow(session.userId)
    }

    override fun observe(id: String): Flow<Result<Folder>> = storage.getByIdFlow(id).map { result ->
        result.mapCatching { option ->
            option.getOrThrow { IllegalStateException("No folder '$id'") }
        }
    }

    override fun observeRoots(): Flow<Result<List<Folder>>> = observeSession(sessionRepository::observe) { session ->
        storage.getRootsFlow(session.userId)
    }

    override suspend fun getChildren(parentId: String): Result<List<Folder>> = withSession(sessionRepository::get) { session ->
        storage.getChildren(session.userId, parentId)
    }

    override suspend fun getMediaByFolder(folderId: String): Result<List<Media>> {
        return storage.getMediaByFolder(folderId)
    }

    override suspend fun linkMedia(mediaId: String, folderId: String): Result<Unit> {
        return linkStorage.link(mediaId, folderId)
    }

    override suspend fun unlinkMedia(mediaId: String, folderId: String): Result<Unit> {
        return linkStorage.unlink(mediaId, folderId)
    }

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun upsert(folder: Folder): Result<Unit> = storage.upsert(folder)

    override suspend fun delete(id: String): Result<Unit> = storage.delete(id)

    override suspend fun deleteAllByUser(userId: String): Result<Unit> {
        return storage.deleteAllByUser(userId)
    }
}
