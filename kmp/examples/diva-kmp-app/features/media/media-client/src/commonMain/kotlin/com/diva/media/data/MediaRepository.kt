package com.diva.media.data

import com.diva.database.media.MediaStorage
import com.diva.media.api.client.MediaClient

interface MediaRepository

class MediaRepositoryImpl(
    private val mediaClient: MediaClient,
    private val mediaStorage: MediaStorage
) : MediaRepository
