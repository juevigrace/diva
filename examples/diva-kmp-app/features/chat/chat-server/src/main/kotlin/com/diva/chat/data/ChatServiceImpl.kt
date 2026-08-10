package com.diva.chat.data

import com.diva.database.chat.ChatStorage

class ChatServiceImpl(
    private val storage: ChatStorage,
) : ChatService