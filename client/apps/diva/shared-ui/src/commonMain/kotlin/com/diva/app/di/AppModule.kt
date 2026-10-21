package com.diva.app.di

import com.diva.app.database.di.databaseModule
import com.diva.app.home.di.homeModule
import com.diva.app.home.presentation.ui.components.navigation.HomeRoute
import com.diva.app.player.di.playerModule
import com.diva.app.presentation.viewmodel.AppViewModel
import com.diva.app.settings.models.AppSettings
import com.diva.app.ui.di.uiModule
import io.github.juevigrace.diva.lib.auth.di.authModule
import io.github.juevigrace.diva.lib.onboarding.di.onboardingModule
import io.github.juevigrace.diva.lib.verification.di.verificationModule
import io.github.juevigrace.diva.ui.navigation.Navigator
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Modules for features rendered *outside* the home tab host. Everything the tab host
 * hosts — including the [io.github.juevigrace.diva.ui.navigation.TabNavigator] — is
 * owned by [homeModule] instead, so each feature is declared exactly once.
 */
fun appModule(settings: AppSettings): Module {
    return module {
        viewModel {
            AppViewModel(
                defaults = settings,
                userRepository = get(),
                sessionRepository = get(),
                settingsRepository = get(),
                devicesRepository = get(),
                client = get(),
            )
        }

        single<Navigator> { Navigator.create(startDestination = HomeRoute) }

        includes(
            uiModule(),
            networkModule(),
            databaseModule(),
        )
        includes(
            homeModule(),
            playerModule(),
            authModule(),
            onboardingModule(),
            verificationModule(),
        )
    }
}
