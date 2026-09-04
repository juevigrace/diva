@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.validation

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport

object EmailValidation {
    // todo: not use this
    private val emailRegex = Regex(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
    )

    fun isValid(email: String): Boolean {
        return emailRegex.matches(email)
    }
}
