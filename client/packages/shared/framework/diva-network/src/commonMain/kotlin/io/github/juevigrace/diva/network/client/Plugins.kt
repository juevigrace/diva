package io.github.juevigrace.diva.network.client

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

fun <C : HttpClientEngineConfig> HttpClientConfig<C>.defaultConfig() {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.INFO
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 30.seconds.toLong(DurationUnit.SECONDS)
        connectTimeoutMillis = 10.seconds.toLong(DurationUnit.SECONDS)
        socketTimeoutMillis = 10.seconds.toLong(DurationUnit.SECONDS)
    }
}
