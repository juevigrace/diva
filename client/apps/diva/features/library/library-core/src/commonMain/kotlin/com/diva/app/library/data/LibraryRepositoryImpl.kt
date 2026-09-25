package com.diva.app.library.data

import com.diva.app.folder.domain.FolderRepository
import com.diva.app.library.database.FavoriteStorage
import com.diva.app.library.domain.LibraryRepository
import com.diva.app.media.domain.MediaRepository
import com.diva.app.player.domain.PlayerRepository
import com.diva.app.server.domain.ServerRepository
import com.diva.app.models.folder.Folder
import com.diva.app.models.media.Media
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class LibraryRepositoryImpl(
    private val favoriteStorage: FavoriteStorage,
    private val mediaRepository: MediaRepository,
    private val playerRepository: PlayerRepository,
    private val folderRepository: FolderRepository,
    private val serverRepository: ServerRepository,
) : LibraryRepository {

    override fun getFavorites(userId: String): Flow<Result<List<Media>>> {
        return favoriteStorage.getFavoritesByUserFlow(userId)
    }

    override suspend fun toggleFavorite(userId: String, mediaId: String): Result<Unit> {
        return favoriteStorage.toggle(userId, mediaId)
    }

    override suspend fun removeFavorite(userId: String, mediaId: String): Result<Unit> {
        return favoriteStorage.remove(userId, mediaId)
    }

    override suspend fun isFavorite(userId: String, mediaId: String): Result<Boolean> {
        return favoriteStorage.isFavorite(userId, mediaId)
    }

    override suspend fun getRecent(userId: String, limit: Long): Result<List<Media>> {
        return playerRepository.getRecentByUser(userId, limit)
    }

    override suspend fun getResumable(userId: String): Result<List<Media>> {
        return playerRepository.getResumableByUser(userId)
    }

    override suspend fun getFolders(userId: String): Result<List<Folder>> {
        return folderRepository.getFolders(userId).firstOrNull() ?: Result.success(emptyList())
    }

    override suspend fun sync(): Result<Unit> {
        return serverRepository.sync()
    }
}
