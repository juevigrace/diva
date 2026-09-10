package com.diva.app.features.folder.data

import com.diva.app.database.folder.FolderStorage
import com.diva.app.database.folder.MediaFolderLinkStorage
import com.diva.app.features.folder.domain.FolderRepository
import com.diva.app.models.folder.Folder
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class FolderRepositoryImpl(
    private val storage: FolderStorage,
    private val linkStorage: MediaFolderLinkStorage,
) : FolderRepository {

    override fun getFolders(userId: Uuid): Flow<Result<List<Folder>>> {
        return storage.getByUserFlow(userId)
    }

    override fun getFolder(id: Uuid): Flow<Result<Option<Folder>>> = storage.getByIdFlow(id)

    override fun getRoots(userId: Uuid): Flow<Result<List<Folder>>> {
        return storage.getRootsFlow(userId)
    }

    override suspend fun getChildren(userId: Uuid, parentId: Uuid): Result<List<Folder>> {
        return storage.getChildren(userId, parentId)
    }

    override suspend fun getMediaByFolder(folderId: Uuid): Result<List<Media>> {
        return storage.getMediaByFolder(folderId)
    }

    override suspend fun linkMedia(mediaId: Uuid, folderId: Uuid): Result<Unit> {
        return linkStorage.link(mediaId, folderId)
    }

    override suspend fun unlinkMedia(mediaId: Uuid, folderId: Uuid): Result<Unit> {
        return linkStorage.unlink(mediaId, folderId)
    }

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun save(folder: Folder): Result<Unit> = storage.upsert(folder)

    override suspend fun delete(id: Uuid): Result<Unit> = storage.delete(id)

    override suspend fun deleteAllByUser(userId: Uuid): Result<Unit> {
        return storage.deleteAllByUser(userId)
    }
}