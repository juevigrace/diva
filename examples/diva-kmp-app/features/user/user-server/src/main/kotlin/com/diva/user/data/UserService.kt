package com.diva.user.data

import com.diva.database.user.UserStorage
import com.diva.models.api.ApiResponse
import com.diva.models.api.PaginationResponse
import com.diva.models.api.user.dtos.CreateUserDto
import com.diva.models.api.user.dtos.UpdateUserDto
import com.diva.models.api.user.dtos.UserEmailDto
import com.diva.models.api.user.response.UserResponse
import com.diva.models.user.User
import com.diva.util.Encryption
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.errors.DivaError
import io.github.juevigrace.diva.core.errors.ErrorCause
import io.github.juevigrace.diva.core.errors.toDivaError
import io.github.juevigrace.diva.core.flatMap
import io.github.juevigrace.diva.core.fold
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.core.network.HttpStatusCodes
import io.github.juevigrace.diva.core.tryResult
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface UserService {
    suspend fun getUsers(
        page: Int,
        pageSize: Int,
    ): DivaResult<ApiResponse<PaginationResponse<UserResponse>>, DivaError>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getUser(id: Uuid): DivaResult<ApiResponse<UserResponse>, DivaError>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun updateUser(
        dto: UpdateUserDto,
        id: Uuid,
    ): DivaResult<ApiResponse<Unit>, DivaError>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun updateEmail(
        dto: UserEmailDto,
        userId: Uuid,
    ): DivaResult<ApiResponse<Unit>, DivaError>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun deleteUser(id: Uuid): DivaResult<ApiResponse<Unit>, DivaError>

    suspend fun getUserByUsername(username: String): DivaResult<User, DivaError>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun createUser(dto: CreateUserDto): DivaResult<Uuid, DivaError>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun updatePassword(id: Uuid, passwordHash: String): DivaResult<Unit, DivaError>
}

class UserServiceImpl(
    private val storage: UserStorage,
) : UserService {
    override suspend fun getUsers(
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<UserResponse>>, DivaError> {
        return storage
            .getAll(pageSize, (page - 1) * pageSize)
            .flatMap { users ->
                storage.count()
                    .map { count ->
                        ApiResponse(
                            data = PaginationResponse(
                                items = users.map { u -> u.toResponse() },
                                totalItems = count.toInt(),
                                totalPages = ((count / pageSize) + 1).toInt(),
                                currentPage = page,
                                pageSize = pageSize,
                            ),
                            message = "Users retrieved"
                        )
                    }
            }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getUser(id: Uuid): DivaResult<ApiResponse<UserResponse>, DivaError> {
        return storage
            .getById(id)
            .map { option ->
                option.fold(
                    onNone = {
                        ApiResponse(
                            statusCode = HttpStatusCodes.NotFound.code,
                            message = "User not found",
                        )
                    },
                    onSome = { value -> ApiResponse(data = value.toResponse(), message = "User retrieved") }
                )
            }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updateUser(
        dto: UpdateUserDto,
        id: Uuid,
    ): DivaResult<ApiResponse<Unit>, DivaError> {
        val user = User(
            id = id,
            username = dto.username,
            alias = dto.alias,
            avatar = dto.avatar,
            bio = dto.bio,
        )
        return storage
            .update(user)
            .map { ApiResponse(message = "User updated") }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updateEmail(
        dto: UserEmailDto,
        userId: Uuid,
    ): DivaResult<ApiResponse<Unit>, DivaError> {
        return storage
            .updateEmail(userId, dto.email)
            .map { ApiResponse(message = "User updated") }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun deleteUser(id: Uuid): DivaResult<ApiResponse<Unit>, DivaError> {
        return storage
            .delete(id)
            .map { ApiResponse(message = "User deleted") }
    }

    override suspend fun getUserByUsername(username: String): DivaResult<User, DivaError> {
        return storage
            .getByUsername(username)
            .flatMap { option ->
                option.fold(
                    onNone = {
                        DivaResult.failure(
                            DivaError(
                                cause = ErrorCause.Validation.MissingValue(
                                    "user",
                                    Option.Some("user not found")
                                )
                            )
                        )
                    },
                    onSome = { value -> DivaResult.success(value) }
                )
            }
    }

    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    override suspend fun createUser(
        dto: CreateUserDto,
    ): DivaResult<Uuid, DivaError> {
        return tryResult(
            onError = { e -> e.toDivaError() }
        ) {
            val id: Uuid = Uuid.random()
            val hash: String = Encryption.hashPassword(dto.password)
            val user = User(
                id = id,
                email = dto.email,
                username = dto.username,
                passwordHash = Option.of(hash),
                alias = dto.alias,
                avatar = dto.avatar,
                bio = dto.bio,
                userVerified = false,
            )
            storage
                .insert(user)
                .map { _ -> id }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updatePassword(
        id: Uuid,
        passwordHash: String
    ): DivaResult<Unit, DivaError> {
        return storage.updatePassword(id, passwordHash)
    }
}
