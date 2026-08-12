package com.diva.chat.di

import com.diva.chat.api.client.ChatClient
import com.diva.chat.api.client.ChatClientImpl
import com.diva.chat.data.ChatRepository
import com.diva.chat.data.ChatRepositoryImpl
import com.diva.chat.database.ChatStorageImpl
import com.diva.database.chat.ChatStorage
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun chatModule(): Module {
    return module {
        singleOf(::ChatClientImpl) { bind<ChatClient>() }
        singleOf(::ChatStorageImpl) { bind<ChatStorage>() }
        singleOf(::ChatRepositoryImpl) { bind<ChatRepository>() }
    }
}
