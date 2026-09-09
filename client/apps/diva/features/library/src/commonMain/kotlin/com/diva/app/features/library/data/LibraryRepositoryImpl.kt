package com.diva.app.features.library.data

import com.diva.app.database.playback.FavoriteStorage
import com.diva.app.features.folder.domain.FolderRepository
import com.diva.app.features.media.domain.MediaRepository
import com.diva.app.features.player.domain.PlayerRepository
import com.diva.app.features.server.domain.ServerRepository
import com.diva.app.features.library.domain.LibraryRepository
import com.diva.app.models.folder.Folder
import com.diva.app.models.media.Media
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class LibraryRepositoryImpl(
    private val favoriteStorage: FavoriteStorage,
    private val mediaRepository: MediaRepository,
    private val playerRepository: PlayerRepository,
    private val folderRepository: FolderRepository,
    private val serverRepository: ServerRepository,
) : LibraryRepository {

    override fun getFavorites(userId: Uuid): Flow<Result<List<Media>>> {
        return favoriteStorage.getFavoritesByUserFlow(userId)
    }

    override suspend fun toggleFavorite(userId: Uuid, mediaId: Uuid): Result<Unit> {
        return favoriteStorage.toggle(userId, mediaId)
    }

    override suspend fun removeFavorite(userId: Uuid, mediaId: Uuid): Result<Unit> {
        return favoriteStorage.remove(userId, mediaId)
    }

    override suspend fun isFavorite(userId: Uuid, mediaId: Uuid): Result<Boolean> {
        return favoriteStorage.isFavorite(userId, mediaId)
    }

    override suspend fun getRecent(userId: Uuid, limit: Long): Result<List<Media>> {
        return playerRepository.getRecentByUser(userId, limit)
    }

    override suspend fun getResumable(userId: Uuid): Result<List<Media>> {
        return playerRepository.getResumableByUser(userId)
    }

    override suspend fun getFolders(userId: Uuid): Result<List<Folder>> {
        return folderRepository.getFolders(userId).firstOrNull() ?: Result.success(emptyList())
    }

    override suspend fun sync(): Result<Unit> {
        return serverRepository.sync()
    }
}