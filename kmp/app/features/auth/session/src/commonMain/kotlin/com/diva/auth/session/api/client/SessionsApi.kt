package com.diva.auth.session.api.client

import com.diva.models.api.ApiResponse
import com.diva.models.api.auth.session.response.SessionResponse
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.errors.ConstraintException
import io.github.juevigrace.diva.core.errors.HttpException
import io.github.juevigrace.diva.core.tryResult
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.toDivaNetworkException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

interface SessionsApi {
    suspend fun listByUser(uid: String, token: String): Result<List<SessionResponse>>
    suspend fun getByID(sid: String, token: String): Result<SessionResponse>
    suspend fun close(sid: String, token: String): Result<Unit>
    suspend fun clearHistoryByUser(uid: String, token: String): Result<Unit>
    suspend fun closeAllByUser(uid: String, token: String): Result<Unit>
    suspend fun listAll(token: String): Result<List<SessionResponse>>
    suspend fun closeExpired(token: String): Result<Unit>
    suspend fun deleteAllForever(token: String): Result<Unit>
}

class SessionsApiImpl(
    private val client: DivaClient
) : SessionsApi {
    override suspend fun listByUser(uid: String, token: String): Result<List<SessionResponse>> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.get(
                path = "/api/user/$uid/sessions",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ApiResponse<List<SessionResponse>> = response.body()
                    body.data ?: emptyList()
                }
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/{uid}/sessions"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun getByID(sid: String, token: String): Result<SessionResponse> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.get(
                path = "/api/sessions/$sid",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ApiResponse<SessionResponse> = response.body()
                    body.data ?: throw ConstraintException(
                        field = "data",
                        constraint = "missing",
                        value = body.message
                    )
                }
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/sessions/{sid}"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun close(sid: String, token: String): Result<Unit> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.delete(
                path = "/api/sessions/$sid/close",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> return@tryResult
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/sessions/{sid}/close"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun clearHistoryByUser(uid: String, token: String): Result<Unit> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.delete(
                path = "/api/user/$uid/sessions",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> return@tryResult
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/{uid}/sessions"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun closeAllByUser(uid: String, token: String): Result<Unit> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.delete(
                path = "/api/user/$uid/sessions/close",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> return@tryResult
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/{uid}/sessions/close"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun listAll(token: String): Result<List<SessionResponse>> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.get(
                path = "/api/sessions",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ApiResponse<List<SessionResponse>> = response.body()
                    body.data ?: emptyList()
                }
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/sessions"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun closeExpired(token: String): Result<Unit> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.delete(
                path = "/api/sessions/close",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> return@tryResult
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/sessions/close"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun deleteAllForever(token: String): Result<Unit> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.delete(
                path = "/api/sessions",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> return@tryResult
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/sessions"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }
}
