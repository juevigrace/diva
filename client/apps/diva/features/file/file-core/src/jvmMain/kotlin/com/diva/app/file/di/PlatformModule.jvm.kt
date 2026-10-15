package com.diva.app.file.di

import com.diva.app.file.data.JvmFileSystemService
import com.diva.app.file.domain.services.FileSystemService
import io.github.juevigrace.diva.core.permissions.JvmPermissionRequester
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformFileSystemServiceModule(): Module = module {
    single<FileSystemService> { JvmFileSystemService(JvmPermissionRequester()) }
}
