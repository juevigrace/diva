@file:DivaJsExport

package io.github.juevigrace.diva.lib.verification.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.verification.models.api.VerifyActionDto

data class VerificationForm(
    val actionId: String = "",
    val token: String = "",
) {
    fun toVerifyActionDto(): VerifyActionDto {
        return VerifyActionDto(
            actionId = actionId,
            token = token,
        )
    }
}
