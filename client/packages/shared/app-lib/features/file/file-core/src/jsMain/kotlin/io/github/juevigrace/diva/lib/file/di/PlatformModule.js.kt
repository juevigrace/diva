package io.github.juevigrace.diva.lib.file.di

import io.github.juevigrace.diva.lib.file.data.services.FileSystemServiceImpl
import io.github.juevigrace.diva.lib.file.domain.services.FileSystemService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformFileSystemServiceModule(): Module = module {
    singleOf(::FileSystemServiceImpl) bind FileSystemService::class
}
