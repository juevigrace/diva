package com.diva.media.data

import com.diva.database.media.MediaStorage

interface MediaService

class MediaServiceImpl(
    private val storage: MediaStorage,
) : MediaService