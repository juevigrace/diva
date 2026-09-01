package io.github.juevigrace.diva.lib.database.user.actions.verification

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.user.actions.UserActionVerification
import kotlin.uuid.Uuid

interface ActionVerificationStorage {
    suspend fun getByAction(actionId: Uuid): Result<Option<UserActionVerification>>

    suspend fun upsert(item: UserActionVerification): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
