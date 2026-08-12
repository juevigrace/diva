package com.diva.collection.api.client

import io.github.juevigrace.diva.network.client.DivaClient

class CollectionClientImpl(
    private val client: DivaClient,
) : CollectionClient
