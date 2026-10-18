package io.github.juevigrace.diva.lib.system.di

import io.github.juevigrace.diva.lib.system.data.DefaultSystemRepository
import io.github.juevigrace.diva.lib.system.domain.SystemRepository
import io.github.juevigrace.diva.native.api.NativeCoreApi
import io.github.juevigrace.diva.native.api.createNativeCore
import org.koin.core.module.Module
import org.koin.dsl.module

fun systemModule(): Module = module {
    single<NativeCoreApi> { createNativeCore() }
    single<SystemRepository> { DefaultSystemRepository(get()) }
}
