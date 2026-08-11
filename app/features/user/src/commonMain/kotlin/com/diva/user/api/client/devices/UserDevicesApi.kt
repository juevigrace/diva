package com.diva.user.api.client.devices

import com.diva.models.api.ApiResponse
import com.diva.models.api.device.response.DeviceResponse
import com.diva.models.api.user.device.response.UserDeviceResponse
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.errors.ConstraintException
import io.github.juevigrace.diva.core.errors.HttpException
import io.github.juevigrace.diva.core.tryResult
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.toDivaNetworkException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

interface UserDevicesApi {
    suspend fun getUserDevices(uid: String, token: String): Result<List<UserDeviceResponse>>
    suspend fun getUserDevice(uid: String, did: String, token: String): Result<UserDeviceResponse>
    suspend fun deleteUserDevice(uid: String, did: String, token: String): Result<Unit>
    suspend fun listAll(token: String): Result<List<DeviceResponse>>
}

class UserDevicesApiImpl(
    private val client: DivaClient
) : UserDevicesApi {
    override suspend fun getUserDevices(uid: String, token: String): Result<List<UserDeviceResponse>> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.get(
                path = "/api/user/$uid/devices",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ApiResponse<List<UserDeviceResponse>> = response.body()
                    body.data ?: emptyList()
                }
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/{uid}/devices"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun getUserDevice(uid: String, did: String, token: String): Result<UserDeviceResponse> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.get(
                path = "/api/user/$uid/devices/$did",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ApiResponse<UserDeviceResponse> = response.body()
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
                        url = Option.of("/api/user/{uid}/devices/{did}"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun deleteUserDevice(uid: String, did: String, token: String): Result<Unit> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.delete(
                path = "/api/user/$uid/devices/$did",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> return@tryResult
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/{uid}/devices/{did}"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun listAll(token: String): Result<List<DeviceResponse>> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.get(
                path = "/api/devices",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ApiResponse<List<DeviceResponse>> = response.body()
                    body.data ?: emptyList()
                }
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/devices"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }
}
