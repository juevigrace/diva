package com.diva.app.file.di

import com.diva.app.file.data.AndroidFileSystemService
import com.diva.app.file.data.StoragePermissionRequester
import com.diva.app.file.domain.services.FileSystemService
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformFileSystemServiceModule(): Module = module {
    single<FileSystemService> { AndroidFileSystemService(get(), StoragePermissionRequester()) }
}
