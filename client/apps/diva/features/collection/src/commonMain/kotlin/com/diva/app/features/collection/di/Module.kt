package com.diva.app.features.collection.di

import com.diva.app.database.collection.CollectionMediaStorage
import com.diva.app.database.collection.CollectionStorage
import com.diva.app.features.collection.data.CollectionRepositoryImpl
import com.diva.app.features.collection.database.CollectionMediaStorageImpl
import com.diva.app.features.collection.database.CollectionStorageImpl
import com.diva.app.features.collection.domain.CollectionRepository
import com.diva.app.features.collection.presentation.viewmodel.CollectionViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun collectionModule(): Module {
    return module {
        singleOf(::CollectionStorageImpl) bind CollectionStorage::class
        singleOf(::CollectionMediaStorageImpl) bind CollectionMediaStorage::class

        singleOf(::CollectionRepositoryImpl) bind CollectionRepository::class

        viewModelOf(::CollectionViewModel)
    }
}