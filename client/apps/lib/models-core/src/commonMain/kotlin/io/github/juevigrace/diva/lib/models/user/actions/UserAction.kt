package io.github.juevigrace.diva.lib.models.user.actions

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.actions.Actions
import io.github.juevigrace.diva.lib.models.actions.safeActionsValueOf
import io.github.juevigrace.diva.lib.models.api.user.action.UserActionResponse
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class UserAction(
    val id: Uuid,
    val action: Actions,
    val userId: Uuid = Uuid.NIL,
) {
    companion object {
        fun fromResponse(response: UserActionResponse): UserAction {
            return UserAction(
                id = Uuid.parse(response.id),
                action = safeActionsValueOf(response.actionName),
            )
        }
    }
}

data class UserActionVerification(
    val action: UserAction,
    val token: String,
    val expiresAt: Instant,
    val usedAt: Option<Instant> = Option.None,
    val verified: Boolean = false,
)
