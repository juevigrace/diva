package com.diva.app.media.database

import com.diva.app.database.media.DivaDB
import com.diva.app.models.media.MediaMetadata
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.database.DivaDatabase
import kotlinx.coroutines.flow.Flow

interface MediaMetadataStorage {
    suspend fun getAll(): Result<List<MediaMetadata>>

    fun getAllFlow(): Flow<Result<List<MediaMetadata>>>

    suspend fun getByMedia(mediaId: String): Result<Option<MediaMetadata>>

    fun getByMediaFlow(mediaId: String): Flow<Result<Option<MediaMetadata>>>

    suspend fun upsert(item: MediaMetadata): Result<Unit>

    suspend fun delete(mediaId: String): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}

class MediaMetadataStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : MediaMetadataStorage {

    override suspend fun getAll(): Result<List<MediaMetadata>> {
        return db.getList { metadataQueries.findAll(::mapToMediaMetadata) }
    }

    override fun getAllFlow(): Flow<Result<List<MediaMetadata>>> {
        return db.getListAsFlow { metadataQueries.findAll(::mapToMediaMetadata) }
    }

    override suspend fun getByMedia(mediaId: String): Result<Option<MediaMetadata>> {
        return db.getOne { metadataQueries.findOneByMedia(mediaId, ::mapToMediaMetadata) }
    }

    override fun getByMediaFlow(mediaId: String): Flow<Result<Option<MediaMetadata>>> {
        return db.getOneAsFlow { metadataQueries.findOneByMedia(mediaId, ::mapToMediaMetadata) }
    }

    override suspend fun upsert(item: MediaMetadata): Result<Unit> {
        return db.use {
            transaction {
                metadataQueries.upsert(
                    media_id = item.mediaId,
                    album = item.album,
                    artist = item.artist,
                    genre = item.genre,
                    year = item.year.map { it.toLong() }.getOrNull(),
                    track_number = item.trackNumber.map { it.toLong() }.getOrNull(),
                    disc_number = item.discNumber.map { it.toLong() }.getOrNull(),
                    cover_uri = item.coverUri,
                    lyrics = item.lyrics,
                    updated_at = item.updatedAt,
                )
            }
        }
    }

    override suspend fun delete(mediaId: String): Result<Unit> {
        return db.use {
            transaction {
                metadataQueries.delete(mediaId)
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                metadataQueries.findAll(::mapToMediaMetadata)
                    .executeAsList()
                    .forEach { metadata ->
                        metadataQueries.delete(metadata.mediaId)
                    }
            }
        }
    }

    private fun mapToMediaMetadata(
        mediaId: String,
        album: String,
        artist: String,
        genre: String,
        year: Long?,
        trackNumber: Long?,
        discNumber: Long?,
        coverUri: String,
        lyrics: String,
        updatedAt: Long,
    ): MediaMetadata = MediaMetadata(
        mediaId = mediaId,
        album = album,
        artist = artist,
        genre = genre,
        year = Option.of(year?.toInt()),
        trackNumber = Option.of(trackNumber?.toInt()),
        discNumber = Option.of(discNumber?.toInt()),
        coverUri = coverUri,
        lyrics = lyrics,
        updatedAt = updatedAt,
    )
}
