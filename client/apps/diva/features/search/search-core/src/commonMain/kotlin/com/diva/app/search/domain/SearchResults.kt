package com.diva.app.search.domain

import com.diva.app.models.collection.Collection
import com.diva.app.models.folder.Folder
import com.diva.app.models.media.Media

data class SearchResults(
    val media: List<Media> = emptyList(),
    val folders: List<Folder> = emptyList(),
    val collections: List<Collection> = emptyList(),
) {
    val isEmpty: Boolean
        get() = media.isEmpty() && folders.isEmpty() && collections.isEmpty()
}