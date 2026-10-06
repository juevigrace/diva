@file:DivaJsExport

package io.github.juevigrace.diva.lib.user.actions.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.user.action.models.api.UserActionResponse

data class UserAction(
    val id: String,
    val action: Actions,
) {
    companion object {
        fun fromResponse(response: UserActionResponse): UserAction {
            return UserAction(
                id = response.id,
                action = safeActionsValueOf(response.actionName),
            )
        }
    }
}

data class UserActionVerification(
    val action: UserAction,
    val token: String,
    val expiresAt: Long,
    val usedAt: Option<Long> = None,
    val verified: Boolean = false,
)
