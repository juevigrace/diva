package com.diva.app.features.folder.data

import com.diva.app.features.folder.database.FolderStorage
import com.diva.app.features.folder.database.MediaFolderLinkStorage
import com.diva.app.features.folder.domain.FolderRepository
import com.diva.app.models.folder.Folder
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow

class FolderRepositoryImpl(
    private val storage: FolderStorage,
    private val linkStorage: MediaFolderLinkStorage,
) : FolderRepository {

    override fun getFolders(userId: String): Flow<Result<List<Folder>>> {
        return storage.getByUserFlow(userId)
    }

    override fun getFolder(id: String): Flow<Result<Option<Folder>>> = storage.getByIdFlow(id)

    override fun getRoots(userId: String): Flow<Result<List<Folder>>> {
        return storage.getRootsFlow(userId)
    }

    override suspend fun getChildren(userId: String, parentId: String): Result<List<Folder>> {
        return storage.getChildren(userId, parentId)
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

    override suspend fun save(folder: Folder): Result<Unit> = storage.upsert(folder)

    override suspend fun delete(id: String): Result<Unit> = storage.delete(id)

    override suspend fun deleteAllByUser(userId: String): Result<Unit> {
        return storage.deleteAllByUser(userId)
    }
}
