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
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
        return db.getOne { chatQueries.findOneById(id.toString(), mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getByIdFlow(id: Uuid): Flow<DivaResult<Option<Chat>, DivaError>> {
        return db.getOneAsFlow { chatQueries.findOneById(id.toString(), mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    override suspend fun insert(item: Chat): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                chatQueries.insert(
                    id = item.id.toString(),
                    created_by = item.createdBy.id.toString(),
                    chat_type = item.type,
                    name = item.name,
                    description = item.description,
                    avatar = item.avatar.getOrElse { null }?.id?.toString()
                )
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
                    avatar = item.avatar.getOrElse { currentChat.avatar.getOrElse { null } }?.id?.toString(),
                    id = item.id.toString()
                )
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
                chatQueries.delete(id.toString())
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
                    chat_id = chatId.toString(),
                    user_id = item.user.id.toString(),
                    role = item.role,
                    added_by = item.addedBy.id.toString()
                )
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

    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    override suspend fun updateParticipant(chatId: Uuid, item: ChatParticipant): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                chatParticipantQueries.update(
                    role = item.role,
                    last_read_at = item.lastReadAt.getOrElse { Clock.System.now() }.toEpochMilliseconds(),
                    chat_id = chatId.toString(),
                    user_id = item.user.id.toString()
                )
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
                chatParticipantQueries.delete(chatId.toString(), userId.toString())
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
        id: String,
        createdBy: String,
        chatType: ChatType,
        name: String,
        description: String,
        avatar: String?,
        createdAt: Long,
        updatedAt: Long,
        deletedAt: Long?,
    ): Chat {
        return Chat(
            id = Uuid.parse(id),
            name = name,
            description = description,
            type = chatType,
            createdBy = User(
                id = Uuid.parse(createdBy),
            ),
            avatar = Option.of(avatar?.let { Media(id = Uuid.parse(it)) }),
            createdAt = Instant.fromEpochMilliseconds(createdAt),
            updatedAt = Instant.fromEpochMilliseconds(updatedAt),
            deletedAt = Option.of(deletedAt?.let { Instant.fromEpochMilliseconds(it) }),
        )
    }
}
