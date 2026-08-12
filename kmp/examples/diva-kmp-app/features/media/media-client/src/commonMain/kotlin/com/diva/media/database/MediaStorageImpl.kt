package com.diva.media.database

import com.diva.database.DivaDB
import com.diva.database.media.MediaStorage
import com.diva.models.VisibilityType
import com.diva.models.media.Media
import com.diva.models.media.MediaType
import com.diva.models.user.User
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.database.DatabaseAction
import io.github.juevigrace.diva.core.errors.DivaError
import io.github.juevigrace.diva.core.errors.ErrorCause
import io.github.juevigrace.diva.database.DivaDatabase
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MediaStorageImpl(
    private val db: DivaDatabase<DivaDB>
) : MediaStorage {
    override suspend fun count(): DivaResult<Long, DivaError> {
        return db.use {
            val value: Long = mediaQueries.count().executeAsOne()
            DivaResult.success(value)
        }
    }

    override suspend fun getAll(
        limit: Int,
        offset: Int
    ): DivaResult<List<Media>, DivaError> {
        return db.getList { mediaQueries.findAll(limit.toLong(), offset.toLong(), mapper = ::mapToEntity) }
    }

    override fun getAllFlow(
        limit: Int,
        offset: Int
    ): Flow<DivaResult<List<Media>, DivaError>> {
        return db.getListAsFlow { mediaQueries.findAll(limit.toLong(), offset.toLong(), mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getById(id: Uuid): DivaResult<Option<Media>, DivaError> {
        return db.getOne { mediaQueries.findOneById(id.toString(), mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getByIdFlow(id: Uuid): Flow<DivaResult<Option<Media>, DivaError>> {
        return db.getOneAsFlow { mediaQueries.findOneById(id.toString(), mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    override suspend fun insert(item: Media): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                mediaQueries.insert(
                    id = item.id.toString(),
                    submitted_by = item.submittedBy.id.toString(),
                    url = item.url,
                    alt_text = item.altText,
                    media_type = item.mediaType,
                    file_size = item.fileSize,
                    width = item.width.toLong(),
                    height = item.height.toLong(),
                    duration = item.duration.toLong(),
                    visibility = item.visibility,
                    sensitive_content = item.sensitiveContent,
                    adult_content = item.adultContent,
                )
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.INSERT,
                            table = Option.Some("diva_media"),
                            details = Option.Some("Failed to insert")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    override suspend fun update(item: Media): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                mediaQueries.update(
                    url = item.url,
                    alt_text = item.altText,
                    media_type = item.mediaType,
                    file_size = item.fileSize,
                    width = item.width.toLong(),
                    height = item.height.toLong(),
                    duration = item.duration.toLong(),
                    visibility = item.visibility,
                    sensitive_content = item.sensitiveContent,
                    adult_content = item.adultContent,
                    id = item.id.toString()
                )
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.UPDATE,
                            table = Option.Some("diva_media"),
                            details = Option.Some("Failed to update")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun delete(id: Uuid): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                mediaQueries.delete(id.toString())
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.DELETE,
                            table = Option.Some("diva_media"),
                            details = Option.Some("Failed to delete")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    private fun mapToEntity(
        id: String,
    submittedBy: String,
    url: String,
    altText: String,
    mediaType: MediaType,
    fileSize: Long,
    width: Long,
    height: Long,
    duration: Long,
    visibility: VisibilityType,
    sensitiveContent: Boolean,
    adultContent: Boolean,
    publishedAt: Long,
    createdAt: Long,
    updatedAt: Long,
    deletedAt: Long?,
    ): Media {
        return Media(
            id = Uuid.parse(id),
            submittedBy = User(id = Uuid.parse(submittedBy)),
            url = url,
            altText = altText,
            mediaType = mediaType,
            fileSize = fileSize,
            width = width.toInt(),
            height = height.toInt(),
            duration = duration.toInt(),
            visibility = visibility,
            sensitiveContent = sensitiveContent,
            adultContent = adultContent,
            publishedAt = Instant.fromEpochMilliseconds(publishedAt),
            createdAt = Instant.fromEpochMilliseconds(createdAt),
            updatedAt = Instant.fromEpochMilliseconds(updatedAt),
            deletedAt = Option.of(deletedAt?.let { Instant.fromEpochMilliseconds(it) })
        )
    }
}
