package com.diva.app.di

import com.diva.app.database.di.databaseModule
import com.diva.app.collection.di.collectionModule
import com.diva.app.folder.di.folderModule
import com.diva.app.home.di.homeModule
import com.diva.app.library.di.libraryModule
import com.diva.app.media.di.mediaModule
import com.diva.app.mix.di.mixModule
import com.diva.app.player.di.playerModule
import com.diva.app.playlist.di.playlistModule
import com.diva.app.profile.di.profileModule
import com.diva.app.search.di.searchModule
import com.diva.app.server.di.serverModule
import com.diva.app.ui.di.uiModule
import io.github.juevigrace.diva.lib.auth.di.authModule
import io.github.juevigrace.diva.lib.session.di.sessionModule
import io.github.juevigrace.diva.lib.settings.di.settingsModule
import io.github.juevigrace.diva.lib.user.di.userModule
import org.koin.core.module.Module
import org.koin.dsl.module

fun appModule(): Module {
    return module {
        includes(
            uiModule(),
            navigationModule(),
            networkModule(),
            databaseModule(),
        )
        includes(
            homeModule(),
            serverModule(),
            mediaModule(),
            folderModule(),
            collectionModule(),
            playlistModule(),
            mixModule(),
            playerModule(),
            libraryModule(),
            searchModule(),
            profileModule(),
            userModule(),
            sessionModule(),
            authModule(),
            settingsModule(),
        )
    }
}
