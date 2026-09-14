@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.server

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import kotlin.js.ExperimentalJsExport
import kotlin.time.Clock

data class Server(
    val id: String,
    val name: String,
    val baseUrl: String,
    val protocol: String = "http",
    val port: Option<Int> = None,
    val enabled: Boolean = false,
    val lastConnectedAt: Option<Long> = None,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
)
