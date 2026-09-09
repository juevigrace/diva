package com.diva.app.features.player.di

import com.diva.app.database.player.PlayerSettingStorage
import com.diva.app.database.playback.PlaybackHistoryStorage
import com.diva.app.database.playback.ResumePointStorage
import com.diva.app.features.player.data.PlayerRepositoryImpl
import com.diva.app.features.player.database.PlaybackHistoryStorageImpl
import com.diva.app.features.player.database.PlayerSettingStorageImpl
import com.diva.app.features.player.database.ResumePointStorageImpl
import com.diva.app.features.player.domain.PlayerRepository
import com.diva.app.features.player.presentation.viewmodel.PlayerViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun playerModule(): Module {
    return module {
        singleOf(::ResumePointStorageImpl) bind ResumePointStorage::class
        singleOf(::PlaybackHistoryStorageImpl) bind PlaybackHistoryStorage::class
        singleOf(::PlayerSettingStorageImpl) bind PlayerSettingStorage::class

        singleOf(::PlayerRepositoryImpl) bind PlayerRepository::class

        viewModelOf(::PlayerViewModel)
    }
}