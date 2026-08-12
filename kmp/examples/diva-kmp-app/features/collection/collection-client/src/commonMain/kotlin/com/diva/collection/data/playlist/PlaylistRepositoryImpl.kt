package com.diva.collection.data.playlist

import com.diva.collection.api.client.playlist.PlaylistClient
import com.diva.database.collection.playlist.PlaylistStorage

class PlaylistRepositoryImpl(
    private val client: PlaylistClient,
    private val storage: PlaylistStorage,
) : PlaylistRepository
