@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.verification

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.models.api.verification.VerifyActionDto
import kotlin.js.ExperimentalJsExport

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
