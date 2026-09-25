package com.diva.app.folder.di

import com.diva.app.core.AppDatabase
import com.diva.app.folder.data.FolderRepositoryImpl
import com.diva.app.folder.database.FolderStorage
import com.diva.app.folder.database.FolderStorageImpl
import com.diva.app.folder.database.MediaFolderLinkStorage
import com.diva.app.folder.database.MediaFolderLinkStorageImpl
import com.diva.app.folder.domain.FolderRepository
import com.diva.app.folder.presentation.viewmodel.FolderViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun folderModule(): Module {
    return module {
        single<FolderStorage> { FolderStorageImpl(get(qualifier = AppDatabase)) }
        single<MediaFolderLinkStorage> { MediaFolderLinkStorageImpl(get(qualifier = AppDatabase)) }

        singleOf(::FolderRepositoryImpl) bind FolderRepository::class

        viewModelOf(::FolderViewModel)
    }
}
