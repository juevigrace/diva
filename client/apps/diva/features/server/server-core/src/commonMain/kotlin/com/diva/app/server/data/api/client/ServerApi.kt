package com.diva.app.server.data.api.client

import io.github.juevigrace.diva.network.client.DivaClient

interface ServerApi {
}

class ServerApiImpl(
    private val client: DivaClient,
) : ServerApi {
}