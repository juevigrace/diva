package com.diva.social.data.post

import com.diva.database.social.post.PostStorage
import com.diva.social.api.client.post.PostClient

class PostRepositoryImpl(
    private val client: PostClient,
    private val storage: PostStorage,
) : PostRepository
