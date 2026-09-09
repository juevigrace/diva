package com.diva.app.database.collection.playlist

import io.github.juevigrace.diva.lib.models.user.User
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface PlaylistContributorStorage {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getByCollection(collectionId: Uuid): Result<List<User>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun add(collectionId: Uuid, contributorId: Uuid): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun remove(collectionId: Uuid, contributorId: Uuid): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun removeAllByCollection(collectionId: Uuid): Result<Unit>
}
