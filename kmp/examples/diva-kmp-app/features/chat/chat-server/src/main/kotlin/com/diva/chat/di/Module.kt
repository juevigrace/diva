package com.diva.chat.di

import com.diva.chat.data.ChatService
import com.diva.chat.data.ChatServiceImpl
import com.diva.chat.database.ChatStorageImpl
import com.diva.database.chat.ChatStorage
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun chatModule(): Module {
    return module {
        singleOf(::ChatStorageImpl) { bind<ChatStorage>() }
        singleOf(::ChatServiceImpl) { bind<ChatService>() }
    }
}
