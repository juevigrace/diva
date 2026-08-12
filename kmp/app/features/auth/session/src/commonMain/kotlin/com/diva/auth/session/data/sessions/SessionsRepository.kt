package com.diva.auth.session.data.sessions

import com.diva.auth.session.api.client.SessionsApi
import com.diva.auth.session.data.SessionRepository
import com.diva.models.Repository
import com.diva.models.api.auth.session.response.SessionResponse
import kotlin.uuid.ExperimentalUuidApi

interface SessionsRepository : Repository {
    suspend fun listSessions(): Result<List<SessionResponse>>
    suspend fun getSession(sid: String): Result<SessionResponse>
    suspend fun close(sid: String): Result<Unit>
    suspend fun clearHistory(): Result<Unit>
    suspend fun closeAll(): Result<Unit>
    suspend fun listAll(): Result<List<SessionResponse>>
    suspend fun closeExpired(): Result<Unit>
    suspend fun deleteAllForever(): Result<Unit>
}

class SessionsRepositoryImpl(
    private val sessionRepository: SessionRepository,
    private val client: SessionsApi,
) : SessionsRepository {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun listSessions(): Result<List<SessionResponse>> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.listByUser(session.user.id.toString(), session.accessToken)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getSession(sid: String): Result<SessionResponse> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.getByID(sid, session.accessToken)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun close(sid: String): Result<Unit> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.close(sid, session.accessToken)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun clearHistory(): Result<Unit> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.clearHistoryByUser(session.user.id.toString(), session.accessToken)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun closeAll(): Result<Unit> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.closeAllByUser(session.user.id.toString(), session.accessToken)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun listAll(): Result<List<SessionResponse>> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.listAll(session.accessToken)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun closeExpired(): Result<Unit> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.closeExpired(session.accessToken)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun deleteAllForever(): Result<Unit> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.deleteAllForever(session.accessToken)
        }
    }
}
