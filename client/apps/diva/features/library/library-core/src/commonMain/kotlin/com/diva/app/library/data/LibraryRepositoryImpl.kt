package com.diva.app.library.data

import com.diva.app.library.database.FavoriteStorage
import com.diva.app.library.domain.LibraryRepository
import com.diva.app.media.models.Media
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.domain.observeSession
import io.github.juevigrace.diva.lib.session.domain.withSession
import kotlinx.coroutines.flow.Flow

class LibraryRepositoryImpl(
    private val favoriteStorage: FavoriteStorage,
    private val sessionRepository: SessionRepository,
) : LibraryRepository {

    override fun observe(): Flow<Result<List<Media>>> = observeSession(sessionRepository::observe) { session ->
        favoriteStorage.getFavoritesByUserFlow(session.userId)
    }

    override suspend fun toggleFavorite(mediaId: String): Result<Unit> = withSession(sessionRepository::get) { session ->
        favoriteStorage.toggle(session.userId, mediaId)
    }

    override suspend fun removeFavorite(mediaId: String): Result<Unit> = withSession(sessionRepository::get) { session ->
        favoriteStorage.remove(session.userId, mediaId)
    }

    override suspend fun isFavorite(mediaId: String): Result<Boolean> = withSession(sessionRepository::get) { session ->
        favoriteStorage.isFavorite(session.userId, mediaId)
    }
}
