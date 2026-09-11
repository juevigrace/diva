package io.github.juevigrace.diva.lib.user.data.api.client

import io.github.juevigrace.diva.lib.models.api.ApiResponse
import io.github.juevigrace.diva.lib.models.api.user.profile.CreateProfileDto
import io.github.juevigrace.diva.lib.models.api.user.profile.UpdateProfileDto
import io.github.juevigrace.diva.lib.models.api.user.profile.UserProfileResponse
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.FormDataContent
import io.github.juevigrace.diva.network.client.getAs
import io.github.juevigrace.diva.network.client.multipartPatch
import io.github.juevigrace.diva.network.client.post
import io.github.juevigrace.diva.network.client.put
import io.ktor.http.ContentType

interface UserProfileApi {
    suspend fun get(uid: String, token: String): Result<UserProfileResponse?>
    suspend fun create(uid: String, dto: CreateProfileDto, token: String): Result<Unit>
    suspend fun update(uid: String, dto: UpdateProfileDto, token: String): Result<Unit>
    suspend fun updateAvatar(
        uid: String,
        bytes: ByteArray,
        fileName: String,
        contentType: ContentType,
        token: String,
    ): Result<Unit>
}

class UserProfileApiImpl(
    private val client: DivaClient,
) : UserProfileApi {
    override suspend fun get(uid: String, token: String): Result<UserProfileResponse?> {
        return client.getAs<ApiResponse<UserProfileResponse?>>(
            path = "/api/user/$uid/profile",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data }
    }

    override suspend fun create(uid: String, dto: CreateProfileDto, token: String): Result<Unit> {
        return client.post(
            path = "/api/user/$uid/profile",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map {}
    }

    override suspend fun update(uid: String, dto: UpdateProfileDto, token: String): Result<Unit> {
        return client.put(
            path = "/api/user/$uid/profile",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map {}
    }

    override suspend fun updateAvatar(
        uid: String,
        bytes: ByteArray,
        fileName: String,
        contentType: ContentType,
        token: String,
    ): Result<Unit> {
        return client.multipartPatch(
            path = "/api/user/$uid/profile/avatar",
            formData = listOf(
                FormDataContent.FileItem(
                    key = "avatar",
                    bytes = bytes,
                    fileName = fileName,
                    contentType = contentType,
                ),
            ),
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map {}
    }
}
