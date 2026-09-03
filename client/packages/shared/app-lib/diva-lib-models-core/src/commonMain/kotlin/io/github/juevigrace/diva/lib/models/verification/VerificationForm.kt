package io.github.juevigrace.diva.lib.models.verification

import io.github.juevigrace.diva.lib.models.api.verification.VerifyActionDto
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
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
