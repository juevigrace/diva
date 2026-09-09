package com.diva.app.models.server

import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Server(
    val id: Uuid,
    val name: String,
    val baseUrl: String,
    val protocol: String = "http",
    val port: Option<Int> = None,
    val enabled: Boolean = false,
    val lastConnectedAt: Option<Instant> = None,
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = Clock.System.now(),
)
