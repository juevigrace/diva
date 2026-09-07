package com.diva.app.di

import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.defaultConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.sse.SSE
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

fun networkModule(): Module {
    return module {
        single<DivaClient> {
            DivaClient.create {
                defaultConfig()
                install(ContentNegotiation) {
                    json(
                        Json {
                            encodeDefaults = true
                            prettyPrint = true
                        }
                    )
                }
                install(SSE)
                installOrReplace(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.ALL
                }
                defaultRequest {
                    url("http://localhost:8080/")
                }
                // TODO: if intervals need to be set split module in platform-specific modules
                install(WebSockets)
            }
        }
    }
}
