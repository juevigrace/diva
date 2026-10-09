package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.getOrThrow
import io.github.juevigrace.diva.core.toOption
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.domain.observeSession
import io.github.juevigrace.diva.lib.session.domain.withSession
import io.github.juevigrace.diva.lib.user.data.api.client.UserApi
import io.github.juevigrace.diva.lib.user.database.UserStorage
import io.github.juevigrace.diva.lib.user.domain.UserActionsRepository
import io.github.juevigrace.diva.lib.user.domain.UserDevicesRepository
import io.github.juevigrace.diva.lib.user.domain.UserPermissionsRepository
import io.github.juevigrace.diva.lib.user.domain.UserPreferencesRepository
import io.github.juevigrace.diva.lib.user.domain.UserProfileRepository
import io.github.juevigrace.diva.lib.user.domain.UserRepository
import io.github.juevigrace.diva.lib.user.domain.UserStateRepository
import io.github.juevigrace.diva.lib.user.models.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImpl(
    private val api: UserApi,
    private val storage: UserStorage,
    private val sessionRepository: SessionRepository,
    private val userProfileRepository: UserProfileRepository,
    private val userStateRepository: UserStateRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userDevicesRepository: UserDevicesRepository,
    private val userActionsRepository: UserActionsRepository,
    private val userPermissionsRepository: UserPermissionsRepository,
) : UserRepository {

    private suspend fun User.withRelations(): User {
        return this.copy(
            profile = userProfileRepository.get(id).getOrNull().toOption(),
            state = userStateRepository.get(id).getOrNull().toOption(),
            preferences = userPreferencesRepository.get(id).getOrNull().toOption(),
            devices = userDevicesRepository.get(id).getOrNull().orEmpty(),
            actions = userActionsRepository.get(id).getOrNull().orEmpty(),
            permissions = userPermissionsRepository.get(id).getOrNull().orEmpty(),
        )
    }

    override fun observe(): Flow<Result<List<User>>> = storage.findAllFlow()
        .flatMapLatest { result ->
            flow {
                emit(
                    result.fold(
                        onSuccess = { users ->
                            val filled = users.map { it.withRelations() }
                            Result.success(filled)
                        },
                        onFailure = { throwable -> Result.failure(throwable) },
                    ),
                )
            }
        }

    override fun observeCurrent(): Flow<Result<User>> {
        return observeSession(sessionRepository::observe) { session ->
            storage.findOneFlow(session.userId).flatMapLatest { result ->
                flow {
                    emit(
                        result.mapCatching { option ->
                            val user = option.getOrThrow { IllegalStateException("No user '${session.userId}'") }
                            user.withRelations()
                        },
                    )
                }
            }
        }
    }

    override suspend fun sync(): Result<Unit> {
        return withSession(
            sessionCall = sessionRepository::get,
        ) { session ->
            api.getByID(
                uid = session.userId,
                token = session.accessToken
            ).mapCatching { storage.upsert(User.fromResponse(it)).getOrThrow() }
        }
    }

    override suspend fun upsert(user: User): Result<Unit> = storage.upsert(user)

    override suspend fun delete(id: String): Result<Unit> = storage.deleteOne(id)
}
