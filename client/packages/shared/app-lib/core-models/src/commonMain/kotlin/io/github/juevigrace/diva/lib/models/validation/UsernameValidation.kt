@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.validation

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport

object UsernameValidation {
    const val MIN_LENGTH = 3

    fun isValid(username: String): Boolean {
        return username.length >= MIN_LENGTH
    }
}
