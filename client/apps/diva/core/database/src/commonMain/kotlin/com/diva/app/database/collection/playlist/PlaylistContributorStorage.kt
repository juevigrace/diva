package com.diva.app.database.collection.playlist

import io.github.juevigrace.diva.lib.models.user.User
import kotlin.uuid.Uuid

interface PlaylistContributorStorage {
    suspend fun getByCollection(collectionId: Uuid): Result<List<User>>

    suspend fun add(collectionId: Uuid, contributorId: Uuid): Result<Unit>

    suspend fun remove(collectionId: Uuid, contributorId: Uuid): Result<Unit>

    suspend fun removeAllByCollection(collectionId: Uuid): Result<Unit>
}
