package com.diva.user.data.status

import com.diva.auth.session.data.SessionRepository
import com.diva.models.Repository
import com.diva.models.api.user.state.dtos.UpdateUserStatusDto
import com.diva.models.api.user.state.dtos.UpdateVerifiedDto
import com.diva.models.user.state.UserState
import com.diva.user.api.client.status.UserStatusApi
import io.github.juevigrace.diva.core.fold
import kotlin.fold
import kotlin.uuid.ExperimentalUuidApi

interface UserStatusRepository : Repository {
    suspend fun getState(): Result<UserState?>
    suspend fun ping(): Result<Unit>
    suspend fun updateVerified(verified: Boolean): Result<Unit>
    suspend fun updateStatus(status: String): Result<Unit>
}

class UserStatusRepositoryImpl(
    private val sessionRepository: SessionRepository,
    private val client: UserStatusApi,
) : UserStatusRepository {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getState(): Result<UserState?> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.getState(session.user.id.toString(), session.accessToken).fold(
                onFailure = { err -> Result.failure(err) },
                onSuccess = { res -> Result.success(res?.let { UserState.fromResponse(it) }) }
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun ping(): Result<Unit> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.ping(session.user.id.toString(), session.accessToken)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updateVerified(verified: Boolean): Result<Unit> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.updateVerified(
                uid = session.user.id.toString(),
                dto = UpdateVerifiedDto(verified = verified),
                token = session.accessToken
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updateStatus(status: String): Result<Unit> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.updateStatus(
                uid = session.user.id.toString(),
                dto = UpdateUserStatusDto(status = status),
                token = session.accessToken
            )
        }
    }
}
