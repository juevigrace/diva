package com.diva.media.data.tag

import com.diva.database.media.tag.TagStorage
import com.diva.media.api.client.tag.TagClient

interface TagRepository

class TagRepositoryImpl(
    private val tagClient: TagClient,
    private val tagStorage: TagStorage
) : TagRepository
