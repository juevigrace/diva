package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.user.actions.models.UserAction

interface UserActionsRepository : Repository {
    suspend fun get(id: String): Result<List<UserAction>>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(action: UserAction): Result<Unit>

    suspend fun delete(id: String): Result<Unit>
}
