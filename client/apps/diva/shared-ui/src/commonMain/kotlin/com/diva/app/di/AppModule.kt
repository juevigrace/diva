package com.diva.app.di

import com.diva.app.database.di.databaseModule
import com.diva.app.features.home.di.homeModule
import com.diva.app.navigation.routes.uiModule
import org.koin.core.module.Module
import org.koin.dsl.module

fun appModule(): Module {
    return module {
        includes(
            uiModule(),
            networkModule(),
            databaseModule(),
            homeModule(),
        )
    }
}
