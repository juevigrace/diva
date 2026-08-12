package com.diva.media.api.client

import io.github.juevigrace.diva.network.client.DivaClient

interface MediaClient


class MediaClientImpl(
    private val client: DivaClient
) : MediaClient
