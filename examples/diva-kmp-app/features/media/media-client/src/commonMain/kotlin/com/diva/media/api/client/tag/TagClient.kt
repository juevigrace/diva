package com.diva.media.api.client.tag

import io.github.juevigrace.diva.network.client.DivaClient

interface TagClient

class TagClientImpl(
    private val client: DivaClient
) : TagClient
