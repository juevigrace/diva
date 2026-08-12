package com.diva.server.di

import com.diva.auth.di.authModule
import com.diva.chat.di.chatModule
import com.diva.collection.di.collectionModule
import com.diva.media.di.mediaModule
import com.diva.permissions.di.permissionsModule
import com.diva.server.database.Seed
import com.diva.server.di.database.databaseModule
import com.diva.social.di.socialModule
import com.diva.user.di.userModule
import com.diva.verification.di.verificationModule
import io.ktor.server.config.ApplicationConfig
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun appModule(config: ApplicationConfig): Module {
    return module {
        includes(
            databaseModule(config),
            utilModule(config),
        )
        includes(
            authModule(),
            chatModule(),
            collectionModule(),
            mediaModule(),
            permissionsModule(),
            socialModule(),
            userModule(),
            verificationModule(),
        )
        singleOf(::Seed)
    }
}
