package com.diva.user.data

import com.diva.auth.session.data.SessionRepository
import com.diva.database.user.UserStorage
import com.diva.models.Repository
import com.diva.models.auth.SignUpForm
import com.diva.models.user.User
import com.diva.user.api.client.UserApi
import io.github.juevigrace.diva.core.fold
import io.github.juevigrace.diva.core.pagination.Pagination
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlin.fold
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface UserRepository : Repository {
    fun getUsers(page: Int, pageSize: Int): Flow<Result<Pagination<User>>>

    @OptIn(ExperimentalUuidApi::class)
    fun getUserById(id: Uuid): Flow<Result<User>>

    @OptIn(ExperimentalUuidApi::class)
    fun getCurrentUser(): Flow<Result<User>>

    suspend fun checkEmail(email: String): Result<Boolean>

    suspend fun checkUsername(username: String): Result<Boolean>

    suspend fun createUser(form: SignUpForm): Result<String>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun deleteUser(id: Uuid): Result<Unit>
}

class UserRepositoryImpl(
    private val sessionRepository: SessionRepository,
    private val userStorage: UserStorage,
    private val userClient: UserApi,
) : UserRepository {
    @OptIn(ExperimentalUuidApi::class)
    override fun getUsers(
        page: Int,
        pageSize: Int,
    ): Flow<Result<Pagination<User>>> {
        return callbackFlow {
            withSessionFlow(sessionRepository::getCurrent) { session ->
                userClient.getAll(page, pageSize, session.accessToken).fold(
                    onFailure = { err -> emit(Result.failure(err)) },
                    onSuccess = { res ->
                        userStorage.upsertAll(res.items.map { User.fromResponse(it) }).onFailure { err ->
                            return@onFailure
                        }
                        val pagination: Pagination<User> = Pagination(
                            items = res.items.map { User.fromResponse(it) },
                            totalItems = res.pagination.totalItems.toInt(),
                            totalPages = res.pagination.totalPages,
                            currentPage = res.pagination.page,
                            pageSize = res.pagination.limit
                        )
                        emit(Result.success(pagination))
                    }
                )
            }.collect { res -> trySend(res) }

            awaitClose {}
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getUserById(id: Uuid): Flow<Result<User>> {
        return flow {
            userStorage.getByIdFlow(id).collect { result ->
                result.fold(
                    onFailure = { err -> emit(Result.failure(err)) },
                    onSuccess = { option ->
                        option.fold(
                            onNone = {
                                withSession(sessionRepository::getCurrent) { session ->
                                    userClient.getById(id.toString(), session.accessToken).fold(
                                        onFailure = { err -> Result.failure(err) },
                                        onSuccess = { res ->
                                            val user = User.fromResponse(res)
                                            userStorage.upsert(user).onFailure { err ->
                                                return@fold Result.failure(err)
                                            }
                                            Result.success(user)
                                        }
                                    )
                                }.fold(
                                    onFailure = { err -> emit(Result.failure(err)) },
                                    onSuccess = { user -> emit(Result.success(user)) }
                                )
                            },
                            onSome = { user ->
                                emit(Result.success(user))
                            }
                        )
                    }
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getCurrentUser(): Flow<Result<User>> {
        return callbackFlow {
            val fetchJob = scope.launch(start = CoroutineStart.LAZY) {
                fetchCurrentUser().onFailure { err ->
                    trySend(Result.failure(err))
                }
            }

            val dbJob = scope.launch {
                withSessionFlow(sessionRepository::getCurrent) { s ->
                    userStorage.getByIdFlow(s.user.id).collect { res ->
                        res.fold(
                            onFailure = { err -> emit(Result.failure(err)) },
                            onSuccess = { opt ->
                                opt.fold(
                                    onNone = { fetchJob.join() },
                                    onSome = { user -> emit(Result.success(user)) }
                                )
                            }
                        )
                    }
                }.collect { res -> trySend(res) }
            }

            if (!fetchJob.isCompleted) fetchJob.start()

            awaitClose {
                dbJob.cancel()
                fetchJob.cancel()
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun fetchCurrentUser(): Result<Unit> {
        return withSession(sessionRepository::getCurrent) { s ->
            userClient.getById(s.user.id.toString(), s.accessToken).fold(
                onFailure = { err -> Result.failure(err) },
                onSuccess = { res -> userStorage.upsert(User.fromResponse(res)) }
            )
        }
    }

    override suspend fun checkEmail(email: String): Result<Boolean> {
        return userClient.checkEmail(email)
    }

    override suspend fun checkUsername(username: String): Result<Boolean> {
        return userClient.checkUsername(username)
    }

    override suspend fun createUser(form: SignUpForm): Result<String> {
        return withSession(sessionRepository::getCurrent) { value ->
            userClient.create(form.toSignUpDto().user, value.accessToken)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun deleteUser(id: Uuid): Result<Unit> {
        return withSession(sessionRepository::getCurrent) { value ->
            userClient.delete(id.toString(), value.accessToken).onFailure { err ->
                return@withSession Result.failure(err)
            }
            userStorage.delete(id)
        }
    }
}
