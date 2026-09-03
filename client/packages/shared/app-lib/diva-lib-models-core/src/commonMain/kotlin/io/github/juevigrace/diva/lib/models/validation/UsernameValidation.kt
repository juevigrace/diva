package io.github.juevigrace.diva.lib.models.validation

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
object UsernameValidation {
    const val MIN_LENGTH = 3

    fun isValid(username: String): Boolean {
        return username.length >= MIN_LENGTH
    }
}
