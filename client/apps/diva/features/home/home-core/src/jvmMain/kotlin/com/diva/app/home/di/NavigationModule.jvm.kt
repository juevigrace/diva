package com.diva.app.home.di

import com.diva.app.home.presentation.ui.components.navigation.HomeRoute
import com.diva.app.library.presentation.ui.components.navigation.LibraryRoute
import io.github.juevigrace.diva.lib.file.presentation.ui.components.navigation.FilesRoute
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun navigationModule(): Module {
    return module {
        single<TabNavigator> {
            TabNavigator.create(
                tabs = listOf(HomeRoute, LibraryRoute, FilesRoute),
                startTab = HomeRoute,
            )
        }
    }
}
