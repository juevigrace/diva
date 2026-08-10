package com.diva.chat.data

import com.diva.chat.api.client.ChatClient
import com.diva.database.chat.ChatStorage

class ChatRepositoryImpl(
    private val client: ChatClient,
    private val storage: ChatStorage,
) : ChatRepository
