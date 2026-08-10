package com.diva.app.di

import com.diva.app.di.database.databaseModule
import com.diva.app.di.network.networkModule
import com.diva.app.di.ui.uiModule
import com.diva.app.home.di.homeModule
import com.diva.app.library.di.libraryModule
import com.diva.app.presentation.viewmodel.AppViewModel
import com.diva.app.profile.di.profileModule
import com.diva.app.settings.di.settingsModule
import com.diva.auth.di.authModule
import com.diva.chat.di.chatModule
import com.diva.collection.di.collectionModule
import com.diva.media.di.mediaModule
import com.diva.models.config.AppConfig
import com.diva.onboarding.di.onboardingModule
import com.diva.permissions.di.permissionsModule
import com.diva.social.di.socialModule
import com.diva.user.di.userModule
import com.diva.verification.di.verificationModule
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun appModule(config: AppConfig): Module {
    return module {
        single { config }

        includes(
            databaseModule(),
            networkModule(config),
            uiModule(),
        )
        includes(
            homeModule(),
            libraryModule(),
            onboardingModule(),
            profileModule(),
            settingsModule(),
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

        viewModelOf(::AppViewModel)
    }
}
