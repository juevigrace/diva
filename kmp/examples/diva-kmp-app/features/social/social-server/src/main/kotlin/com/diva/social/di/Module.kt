package com.diva.social.di

import com.diva.database.social.post.PostStorage
import com.diva.social.api.handler.PostHandler
import com.diva.social.api.handler.PostHandlerImpl
import com.diva.social.data.post.PostService
import com.diva.social.data.post.PostServiceImpl
import com.diva.social.database.post.PostStorageImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun socialModule(): Module {
    return module {
        singleOf(::PostStorageImpl) { bind<PostStorage>() }
        singleOf(::PostServiceImpl) { bind<PostService>() }
        singleOf(::PostHandlerImpl) { bind<PostHandler>() }
    }
}
