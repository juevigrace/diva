package com.diva.collection.data.playlist

import com.diva.database.collection.playlist.PlaylistStorage

class PlaylistServiceImpl(
    private val storage: PlaylistStorage,
) : PlaylistService