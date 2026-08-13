package io.github.juevigrace.diva.network.client

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory

internal class JvmDivaClient<C : HttpClientEngineConfig>(
    engineFactory: HttpClientEngineFactory<C>,
    httpClientConfig: HttpClientConfig<C>.() -> Unit = { defaultConfig() }
) : DivaClientBase<C>(engineFactory, httpClientConfig)
