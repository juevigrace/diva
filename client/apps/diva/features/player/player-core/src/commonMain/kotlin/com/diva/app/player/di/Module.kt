package com.diva.app.player.di

import com.diva.app.core.AppDatabase
import com.diva.app.player.data.PlayerRepositoryImpl
import com.diva.app.player.database.PlaybackHistoryStorage
import com.diva.app.player.database.PlaybackHistoryStorageImpl
import com.diva.app.player.database.PlayerSettingStorage
import com.diva.app.player.database.PlayerSettingStorageImpl
import com.diva.app.player.database.ResumePointStorage
import com.diva.app.player.database.ResumePointStorageImpl
import com.diva.app.player.domain.PlayerRepository
import com.diva.app.player.presentation.viewmodel.PlayerViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun playerModule(): Module {
    return module {
        single<ResumePointStorage> { ResumePointStorageImpl(get(qualifier = AppDatabase)) }
        single<PlaybackHistoryStorage> { PlaybackHistoryStorageImpl(get(qualifier = AppDatabase)) }
        single<PlayerSettingStorage> { PlayerSettingStorageImpl(get(qualifier = AppDatabase)) }

        singleOf(::PlayerRepositoryImpl) bind PlayerRepository::class

        viewModelOf(::PlayerViewModel)
    }
}
