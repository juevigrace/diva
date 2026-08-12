package com.diva.chat.api.client

import io.github.juevigrace.diva.network.client.DivaClient

class ChatClientImpl(
    private val client: DivaClient,
) : ChatClient
