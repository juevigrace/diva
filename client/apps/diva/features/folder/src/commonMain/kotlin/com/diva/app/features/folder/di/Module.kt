package com.diva.app.features.folder.di

import com.diva.app.database.folder.FolderStorage
import com.diva.app.database.folder.MediaFolderLinkStorage
import com.diva.app.features.folder.data.FolderRepositoryImpl
import com.diva.app.features.folder.database.FolderStorageImpl
import com.diva.app.features.folder.database.MediaFolderLinkStorageImpl
import com.diva.app.features.folder.domain.FolderRepository
import com.diva.app.features.folder.presentation.viewmodel.FolderViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun folderModule(): Module {
    return module {
        singleOf(::FolderStorageImpl) bind FolderStorage::class
        singleOf(::MediaFolderLinkStorageImpl) bind MediaFolderLinkStorage::class

        singleOf(::FolderRepositoryImpl) bind FolderRepository::class

        viewModelOf(::FolderViewModel)
    }
}