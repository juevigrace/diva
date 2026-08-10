package com.diva.chat.database

import com.diva.database.DivaDB
import com.diva.database.chat.ChatStorage
import com.diva.models.chat.Chat
import com.diva.models.chat.ChatParticipant
import com.diva.models.media.Media
import com.diva.models.social.ChatType
import com.diva.models.user.User
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.database.DatabaseAction
import io.github.juevigrace.diva.core.errors.DivaError
import io.github.juevigrace.diva.core.errors.ErrorCause
import io.github.juevigrace.diva.core.fold
import io.github.juevigrace.diva.core.getOrElse
import io.github.juevigrace.diva.database.DivaDatabase
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid

class ChatStorageImpl(
    private val db: DivaDatabase<DivaDB>
) : ChatStorage {
    override suspend fun count(): DivaResult<Long, DivaError> {
        return db.use {
            val value: Long = chatQueries.count().executeAsOne()
            DivaResult.success(value)
        }
    }

    override suspend fun getAll(
        limit: Int,
        offset: Int
    ): DivaResult<List<Chat>, DivaError> {
        return db.getList { chatQueries.findAll(limit.toLong(), offset.toLong(), mapper = ::mapToEntity) }
    }

    override fun getAllFlow(
        limit: Int,
        offset: Int
    ): Flow<DivaResult<List<Chat>, DivaError>> {
        return db.getListAsFlow { chatQueries.findAll(limit.toLong(), offset.toLong(), mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getById(id: Uuid): DivaResult<Option<Chat>, DivaError> {
        return db.getOne { chatQueries.findOneById(id.toJavaUuid(), mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getByIdFlow(id: Uuid): Flow<DivaResult<Option<Chat>, DivaError>> {
        return db.getOneAsFlow { chatQueries.findOneById(id.toJavaUuid(), mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    override suspend fun insert(item: Chat): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                chatQueries.insert(
                    id = item.id.toJavaUuid(),
                    created_by = item.createdBy.id.toJavaUuid(),
                    chat_type = item.type,
                    name = item.name,
                    description = item.description,
                    avatar = item.avatar.getOrElse { null }?.id?.toJavaUuid()
                ).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.INSERT,
                            table = Option.Some("diva_chat"),
                            details = Option.Some("Failed to insert")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    override suspend fun update(item: Chat): DivaResult<Unit, DivaError> {
        return db.use {
            val currentChat = getById(item.id)
                .fold(
                    onFailure = { err ->
                        return@use DivaResult.failure(err)
                    },
                    onSuccess = { opt ->
                        opt.fold(
                            onNone = {
                                return@use DivaResult.failure(
                                    DivaError(
                                        ErrorCause.Validation.MissingValue("chat", Option.Some("chat doesn't exists"))
                                    )
                                )
                            },
                            onSome = { it }
                        )
                    }
                )

            val rows: Long = transactionWithResult {
                chatQueries.update(
                    name = item.name,
                    description = item.description,
                    avatar = item.avatar.getOrElse { currentChat.avatar.getOrElse { null } }?.id?.toJavaUuid(),
                    id = item.id.toJavaUuid()
                ).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.UPDATE,
                            table = Option.Some("diva_chat"),
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
                chatQueries.delete(id.toJavaUuid()).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.DELETE,
                            table = Option.Some("diva_chat"),
                            details = Option.Some("Failed to delete")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun insertParticipant(chatId: Uuid, item: ChatParticipant): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                chatParticipantQueries.insert(
                    chat_id = chatId.toJavaUuid(),
                    user_id = item.user.id.toJavaUuid(),
                    role = item.role,
                    added_by = item.addedBy.id.toJavaUuid()
                ).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.INSERT,
                            table = Option.Some("diva_chat_participant"),
                            details = Option.Some("Failed to insert")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updateParticipant(chatId: Uuid, item: ChatParticipant): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                chatParticipantQueries.update(
                    role = item.role,
                    last_read_at = OffsetDateTime.ofInstant(
                        item.lastReadAt.getOrElse { Clock.System.now() }.toJavaInstant(),
                        ZoneOffset.UTC
                    ),
                    chat_id = chatId.toJavaUuid(),
                    user_id = item.user.id.toJavaUuid()
                ).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.UPDATE,
                            table = Option.Some("diva_chat_participant"),
                            details = Option.Some("Failed to update")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun deleteParticipant(
        chatId: Uuid,
        userId: Uuid
    ): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                chatParticipantQueries.delete(chatId.toJavaUuid(), userId.toJavaUuid()).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.DELETE,
                            table = Option.Some("diva_chat"),
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
        chatType: ChatType,
        name: String,
        description: String,
        avatar: UUID?,
        createdBy: UUID,
        createdAt: OffsetDateTime,
        updatedAt: OffsetDateTime,
        deletedAt: OffsetDateTime?,
    ): Chat {
        return Chat(
            id = id.toKotlinUuid(),
            name = name,
            description = description,
            type = chatType,
            createdBy = User(
                id = createdBy.toKotlinUuid(),
            ),
            avatar = avatar?.let { Option.Some(Media(id = it.toKotlinUuid())) } ?: Option.None,
            createdAt = createdAt.toInstant().toKotlinInstant(),
            updatedAt = updatedAt.toInstant().toKotlinInstant(),
            deletedAt = Option.of(deletedAt?.toInstant()?.toKotlinInstant()),
        )
    }
}
