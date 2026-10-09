package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.getOrThrow
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.domain.observeSession
import io.github.juevigrace.diva.lib.session.domain.withSession
import io.github.juevigrace.diva.lib.user.data.api.client.UserApi
import io.github.juevigrace.diva.lib.user.database.UserStorage
import io.github.juevigrace.diva.lib.user.domain.UserRepository
import io.github.juevigrace.diva.lib.user.models.User
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.mapCatching

class UserRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserApi,
) : UserRepository {
    override fun observe(): Flow<Result<List<User>>> = storage.findAllFlow()

    override fun observeCurrent(): Flow<Result<User>> {
        return observeSession(sessionRepository::observe) { session ->
            storage.findOneFlow(session.userId).map { result ->
                result.mapCatching { option ->
                    option.getOrThrow { IllegalStateException("No user '${session.userId}'") }
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
