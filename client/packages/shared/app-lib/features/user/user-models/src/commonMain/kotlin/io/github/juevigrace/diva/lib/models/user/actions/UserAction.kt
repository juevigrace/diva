@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.user.actions

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.actions.Actions
import io.github.juevigrace.diva.lib.models.actions.safeActionsValueOf
import io.github.juevigrace.diva.lib.models.api.user.action.UserActionResponse
import kotlin.js.ExperimentalJsExport

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
