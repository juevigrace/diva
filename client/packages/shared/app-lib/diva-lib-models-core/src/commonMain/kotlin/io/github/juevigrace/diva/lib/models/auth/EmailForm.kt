@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.auth

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport

data class EmailForm(
    val email: String = "",
)
