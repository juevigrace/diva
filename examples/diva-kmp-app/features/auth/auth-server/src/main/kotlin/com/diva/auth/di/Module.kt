package com.diva.auth.di

import com.diva.auth.api.handler.AuthHandler
import com.diva.auth.api.handler.AuthHandlerImpl
import com.diva.auth.data.AuthService
import com.diva.auth.data.AuthServiceImpl
import com.diva.database.session.SessionStorage
import com.diva.session.database.SessionStorageImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun authModule(): Module {
    return module {
        singleOf(::SessionStorageImpl) { bind<SessionStorage>() }

        singleOf(::AuthServiceImpl) { bind<AuthService>() }

        singleOf(::AuthHandlerImpl) { bind<AuthHandler>() }
    }
}
