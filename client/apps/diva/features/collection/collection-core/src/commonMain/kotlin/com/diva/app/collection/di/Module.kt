package com.diva.app.collection.di

import com.diva.app.core.AppDatabase
import com.diva.app.collection.data.CollectionRepositoryImpl
import com.diva.app.collection.database.CollectionMediaStorage
import com.diva.app.collection.database.CollectionMediaStorageImpl
import com.diva.app.collection.database.CollectionStorage
import com.diva.app.collection.database.CollectionStorageImpl
import com.diva.app.collection.domain.CollectionRepository
import com.diva.app.collection.presentation.viewmodel.CollectionViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun collectionModule(): Module {
    return module {
        single<CollectionStorage> { CollectionStorageImpl(get(qualifier = AppDatabase)) }
        single<CollectionMediaStorage> { CollectionMediaStorageImpl(get(qualifier = AppDatabase)) }

        singleOf(::CollectionRepositoryImpl) bind CollectionRepository::class

        viewModelOf(::CollectionViewModel)
    }
}
