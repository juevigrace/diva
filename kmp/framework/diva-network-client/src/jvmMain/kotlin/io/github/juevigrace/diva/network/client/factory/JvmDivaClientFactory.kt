package io.github.juevigrace.diva.network.client.factory

import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.JvmDivaClient
import io.github.juevigrace.diva.network.client.defaultConfig
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory

class JvmDivaClientFactory<C : HttpClientEngineConfig>(
    private val engineFactory: HttpClientEngineFactory<C>,
    private val httpClientConfig: HttpClientConfig<C>.() -> Unit = { defaultConfig() }
) : DivaClientFactory {
    override fun create(): DivaClient {
        return JvmDivaClient(engineFactory, httpClientConfig)
    }
}
