package com.diva.user.database

import com.diva.database.DivaDB
import com.diva.database.user.UserStorage
import com.diva.models.roles.Role
import com.diva.models.user.User
import com.diva.models.user.permissions.UserPermission
import com.diva.models.user.preferences.UserPreferences
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
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid

class UserStorageImpl(
    private val db: DivaDatabase<DivaDB>
) : UserStorage {
    override suspend fun count(): DivaResult<Long, DivaError> {
        return db.use {
            val value: Long = userQueries.count().executeAsOne()
            DivaResult.success(value)
        }
    }

    override suspend fun getAll(limit: Int, offset: Int): DivaResult<List<User>, DivaError> {
        return db.getList { userQueries.findAll(limit.toLong(), offset.toLong(), mapper = ::mapToEntity) }
    }

    override fun getAllFlow(limit: Int, offset: Int): Flow<DivaResult<List<User>, DivaError>> {
        return db.getListAsFlow { userQueries.findAll(limit.toLong(), offset.toLong(), mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getById(id: Uuid): DivaResult<Option<User>, DivaError> {
        return db.getOne { userQueries.findOneById(id.toJavaUuid(), mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getByIdFlow(id: Uuid): Flow<DivaResult<Option<User>, DivaError>> {
        return db.getOneAsFlow { userQueries.findOneById(id.toJavaUuid(), mapper = ::mapToEntity) }
    }

    override suspend fun getByUsername(username: String): DivaResult<Option<User>, DivaError> {
        return db.getOne { userQueries.findOneByUsername(username, username, mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun insert(item: User): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                val password: String = item.passwordHash.fold(
                    onNone = {
                        rollback(-1)
                    },
                    onSome = { value -> value }
                )
                userQueries.insert(
                    id = item.id.toJavaUuid(),
                    email = item.email,
                    username = item.username,
                    password_hash = password,
                    birth_date = OffsetDateTime.ofInstant(
                        item.birthDate.toJavaInstant(),
                        ZoneOffset.UTC
                    ),
                    phone_number = item.phoneNumber,
                    alias = item.alias,
                    avatar = item.avatar,
                    bio = item.bio,
                    user_verified = false,
                    role = Role.USER
                ).value
            }
            if (rows.toInt() == -1 || rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        cause = ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.INSERT,
                            table = Option.Some("diva_user"),
                            details = Option.Some("No rows affected")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun update(item: User): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                userQueries.update(
                    alias = item.alias,
                    avatar = item.avatar,
                    bio = item.bio,
                    id = item.id.toJavaUuid()
                ).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        cause = ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.UPDATE,
                            table = Option.Some("diva_user"),
                            details = Option.Some("No rows affected")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updateEmail(
        id: Uuid,
        email: String
    ): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                userQueries.updateEmail(
                    email = email,
                    id = id.toJavaUuid()
                ).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        cause = ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.UPDATE,
                            table = Option.Some("diva_user"),
                            details = Option.Some("No rows affected")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updatePassword(
        id: Uuid,
        passwordHash: String
    ): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                userQueries.updatePassword(
                    password_hash = passwordHash,
                    id = id.toJavaUuid()
                ).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        cause = ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.UPDATE,
                            table = Option.Some("diva_user"),
                            details = Option.Some("No rows affected")
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
                userQueries.delete(id.toJavaUuid()).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        cause = ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.DELETE,
                            table = Option.Some("diva_user"),
                            details = Option.Some("No rows affected")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun insertPermissions(
        userId: Uuid,
        perm: UserPermission
    ): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                userPermissionsQueries.insert(
                    user_id = userId.toJavaUuid(),
                    permission_id = perm.permission.id.toJavaUuid(),
                    granted_by = perm.grantedBy.id.toJavaUuid(),
                    granted_at = OffsetDateTime.ofInstant(perm.grantedAt.toJavaInstant(), ZoneOffset.UTC),
                    expires_at = OffsetDateTime.ofInstant(
                        perm.expiresAt.getOrElse {
                            Clock.System.now().plus(10.minutes)
                        }.toJavaInstant(),
                        ZoneOffset.UTC
                    ),
                    granted = perm.granted

                ).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.INSERT,
                            table = Option.Some("diva_user_permissions"),
                            details = Option.Some("Failed to insert")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updatePermissions(
        userId: Uuid,
        perm: UserPermission
    ): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                userPermissionsQueries.update(
                    granted = perm.granted,
                    expires_at = OffsetDateTime.ofInstant(
                        perm.expiresAt.getOrElse {
                            Clock.System.now().plus(10.minutes)
                        }.toJavaInstant(),
                        ZoneOffset.UTC
                    ),
                    user_id = userId.toJavaUuid(),
                    permission_id = perm.permission.id.toJavaUuid(),
                ).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.UPDATE,
                            table = Option.Some("diva_user_permissions"),
                            details = Option.Some("Failed to update")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun deletePermissions(
        userId: Uuid,
        permId: Uuid
    ): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                userPermissionsQueries.delete(permId.toJavaUuid(), userId.toJavaUuid()).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.DELETE,
                            table = Option.Some("diva_user_permissions"),
                            details = Option.Some("Failed to delete")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun insertCloudPreferences(
        userId: Uuid,
        prefs: UserPreferences
    ): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                userPreferencesQueries.insert(
                    id = prefs.id.toJavaUuid(),
                    user_id = userId.toJavaUuid(),
                    theme = prefs.theme,
                    onboarding_completed = prefs.onboardingCompleted,
                    language = prefs.language,
                    created_at = OffsetDateTime.ofInstant(
                        prefs.createdAt.toJavaInstant(),
                        ZoneOffset.UTC
                    ),
                    updated_at = OffsetDateTime.ofInstant(
                        prefs.updatedAt.toJavaInstant(),
                        ZoneOffset.UTC
                    ),
                ).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.INSERT,
                            table = Option.Some("diva_user_preferences"),
                            details = Option.Some("Failed to insert")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updatePreferences(
        prefs: UserPreferences
    ): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                userPreferencesQueries.update(
                    id = prefs.id.toJavaUuid(),
                    theme = prefs.theme,
                    onboarding_completed = prefs.onboardingCompleted,
                    language = prefs.language,
                    updated_at = OffsetDateTime.ofInstant(
                        prefs.updatedAt.toJavaInstant(),
                        ZoneOffset.UTC
                    ),
                ).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.UPDATE,
                            table = Option.Some("diva_user_preferences"),
                            details = Option.Some("Failed to update")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    private fun mapToEntity(
        id: UUID,
        email: String,
        username: String,
        passwordHash: String?,
        alias: String,
        avatar: String,
        bio: String,
        userVerified: Boolean,
        role: Role,
        createdAt: OffsetDateTime,
        updatedAt: OffsetDateTime,
        deletedAt: OffsetDateTime?,
    ): User {
        return User(
            id = id.toKotlinUuid(),
            email = email,
            username = username,
            passwordHash = Option.of(passwordHash),
            alias = alias,
            avatar = avatar,
            bio = bio,
            userVerified = userVerified,
            role = role,
            createdAt = createdAt.toInstant().toKotlinInstant(),
            updatedAt = updatedAt.toInstant().toKotlinInstant(),
            deletedAt = Option.of(deletedAt?.toInstant()?.toKotlinInstant()),
        )
    }
}
