package com.diva.app.file.di

import com.diva.app.file.data.services.FileSystemServiceImpl
import com.diva.app.file.domain.services.FileSystemService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformFileSystemServiceModule(): Module = module {
    singleOf(::FileSystemServiceImpl) bind FileSystemService::class
}
