package com.diva.app.di

import com.diva.app.database.di.databaseModule
import com.diva.app.features.collection.di.collectionModule
import com.diva.app.features.folder.di.folderModule
import com.diva.app.features.home.di.homeModule
import com.diva.app.features.library.di.libraryModule
import com.diva.app.features.media.di.mediaModule
import com.diva.app.features.mix.di.mixModule
import com.diva.app.features.player.di.playerModule
import com.diva.app.features.playlist.di.playlistModule
import com.diva.app.features.profile.di.profileModule
import com.diva.app.features.search.di.searchModule
import com.diva.app.features.server.di.serverModule
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
