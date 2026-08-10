package com.diva.social.di

import com.diva.database.social.post.PostStorage
import com.diva.social.api.client.post.PostClient
import com.diva.social.api.client.post.PostClientImpl
import com.diva.social.data.post.PostRepository
import com.diva.social.data.post.PostRepositoryImpl
import com.diva.social.database.post.PostStorageImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun socialModule(): Module {
    return module {
        singleOf(::PostClientImpl) { bind<PostClient>() }
        singleOf(::PostStorageImpl) { bind<PostStorage>() }
        singleOf(::PostRepositoryImpl) { bind<PostRepository>() }
    }
}
