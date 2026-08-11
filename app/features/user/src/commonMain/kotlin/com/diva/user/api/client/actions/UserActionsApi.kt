package com.diva.user.api.client.actions

import com.diva.models.api.ApiResponse
import com.diva.models.api.user.action.response.ActionResponse
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.errors.ConstraintException
import io.github.juevigrace.diva.core.errors.HttpException
import io.github.juevigrace.diva.core.tryResult
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.toDivaNetworkException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

interface UserActionsApi {
    suspend fun getActions(uid: String, token: String): Result<List<ActionResponse>>
    suspend fun getAction(aid: String, token: String): Result<ActionResponse>
    suspend fun deleteAction(aid: String, token: String): Result<Unit>
}

class UserActionsApiImpl(
    private val client: DivaClient
) : UserActionsApi {
    override suspend fun getActions(uid: String, token: String): Result<List<ActionResponse>> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.get(
                path = "/api/user/$uid/actions",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ApiResponse<List<ActionResponse>> = response.body()
                    body.data ?: throw ConstraintException(
                        field = "data",
                        constraint = "missing",
                        value = body.message
                    )
                }
                else -> {
                    val body: ApiResponse<Unit> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/{uid}/actions"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun getAction(aid: String, token: String): Result<ActionResponse> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.get(
                path = "/api/user/actions/$aid",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ApiResponse<ActionResponse> = response.body()
                    body.data ?: throw ConstraintException(
                        field = "data",
                        constraint = "missing",
                        value = body.message
                    )
                }
                else -> {
                    val body: ApiResponse<Unit> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/actions/{aid}"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun deleteAction(aid: String, token: String): Result<Unit> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.delete(
                path = "/api/user/actions/$aid",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.NoContent -> return@tryResult
                else -> {
                    val body: ApiResponse<Unit> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/actions/{aid}"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }
}
