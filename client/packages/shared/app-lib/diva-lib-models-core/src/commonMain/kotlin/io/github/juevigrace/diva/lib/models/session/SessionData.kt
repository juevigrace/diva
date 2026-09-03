package io.github.juevigrace.diva.lib.models.session

import io.github.juevigrace.diva.lib.models.api.auth.session.SessionDataDto
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class SessionData(
    val device: String = "",
    val agent: String = "",
    val ip: String = "",
) {
    fun toSessionDataDto(): SessionDataDto {
        return SessionDataDto(
            device = device,
            userAgent = agent,
        )
    }
}
