package com.diva.models.server

import io.github.juevigrace.diva.core.Option
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
data class UserVerification(
    val userId: Uuid,
    val token: String,
    val expiresAt: Instant,
    val createdAt: Instant,
    val usedAt: Option<Instant> = Option.None,
)
