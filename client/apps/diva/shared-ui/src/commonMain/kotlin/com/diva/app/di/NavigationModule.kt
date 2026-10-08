package com.diva.app.di

import com.diva.app.folder.presentation.ui.components.navigation.FoldersRoute
import com.diva.app.home.presentation.ui.components.navigation.HomeRoute
import com.diva.app.library.presentation.ui.components.navigation.LibraryRoute
import com.diva.app.profile.presentation.ui.components.navigation.ProfileRoute
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import org.koin.core.module.Module
import org.koin.dsl.module

fun navigationModule(): Module {
    return module {
        single<Navigator> { Navigator.create(startDestination = HomeRoute) }
        single<TabNavigator> {
            TabNavigator.create(
                tabs = listOf(HomeRoute, LibraryRoute, FoldersRoute, ProfileRoute),
                startTab = HomeRoute,
            )
        }
    }
}
