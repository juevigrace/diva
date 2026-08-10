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
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.time.ExperimentalTime
import kotlin.time.toKotlinInstant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid

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
        return db.getOne { mediaQueries.findOneById(id.toJavaUuid(), mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getByIdFlow(id: Uuid): Flow<DivaResult<Option<Media>, DivaError>> {
        return db.getOneAsFlow { mediaQueries.findOneById(id.toJavaUuid(), mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    override suspend fun insert(item: Media): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                mediaQueries.insert(
                    id = item.id.toJavaUuid(),
                    submitted_by = item.submittedBy.id.toJavaUuid(),
                    url = item.url,
                    alt_text = item.altText,
                    media_type = item.mediaType,
                    file_size = item.fileSize,
                    width = item.width,
                    height = item.height,
                    duration = item.duration,
                    visibility = item.visibility,
                    sensitive_content = item.sensitiveContent,
                    adult_content = item.adultContent,
                ).value
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
                    width = item.width,
                    height = item.height,
                    duration = item.duration,
                    visibility = item.visibility,
                    sensitive_content = item.sensitiveContent,
                    adult_content = item.adultContent,
                    id = item.id.toJavaUuid()
                ).value
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
                mediaQueries.delete(id.toJavaUuid()).value
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
        id: UUID,
        submittedBy: UUID,
        url: String,
        altText: String,
        mediaType: MediaType,
        fileSize: Long,
        width: Int,
        height: Int,
        duration: Int,
        visibility: VisibilityType,
        sensitiveContent: Boolean,
        adultContent: Boolean,
        publishedAt: OffsetDateTime,
        createdAt: OffsetDateTime,
        updatedAt: OffsetDateTime,
        deletedAt: OffsetDateTime?,
    ): Media {
        return Media(
            id = id.toKotlinUuid(),
            submittedBy = User(id = submittedBy.toKotlinUuid()),
            url = url,
            altText = altText,
            mediaType = mediaType,
            fileSize = fileSize,
            width = width,
            height = height,
            duration = duration,
            visibility = visibility,
            sensitiveContent = sensitiveContent,
            adultContent = adultContent,
            publishedAt = publishedAt.toInstant().toKotlinInstant(),
            createdAt = createdAt.toInstant().toKotlinInstant(),
            updatedAt = updatedAt.toInstant().toKotlinInstant(),
            deletedAt = Option.of(deletedAt?.toInstant()?.toKotlinInstant())
        )
    }
}
