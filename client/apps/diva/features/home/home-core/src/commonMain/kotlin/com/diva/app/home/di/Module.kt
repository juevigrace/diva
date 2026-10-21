package com.diva.app.home.di

import com.diva.app.collection.di.collectionModule
import com.diva.app.home.presentation.viewmodel.HomeViewModel
import com.diva.app.library.di.libraryModule
import com.diva.app.media.di.mediaModule
import com.diva.app.mix.di.mixModule
import com.diva.app.playlist.di.playlistModule
import com.diva.app.profile.di.profileModule
import com.diva.app.search.di.searchModule
import com.diva.app.server.di.serverModule
import com.diva.app.settings.di.settingsModule
import io.github.juevigrace.diva.lib.devices.di.devicesModule
import io.github.juevigrace.diva.lib.file.di.fileModule
import io.github.juevigrace.diva.lib.permissions.di.permissionsModule
import io.github.juevigrace.diva.lib.session.di.sessionModule
import io.github.juevigrace.diva.lib.user.di.userModule
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Owns the DI of every feature whose screens the home tab host renders, plus the
 * [TabNavigator] itself, so shared-ui does not have to declare any of them a second time.
 */
fun homeModule(): Module {
    return module {
        viewModelOf(::HomeViewModel)

        includes(
            navigationModule(),
            collectionModule(),
            devicesModule(),
            fileModule(),
            libraryModule(),
            mediaModule(),
            mixModule(),
            permissionsModule(),
            playlistModule(),
            profileModule(),
            searchModule(),
            serverModule(),
            sessionModule(),
            settingsModule(),
            userModule(),
        )
    }
}
