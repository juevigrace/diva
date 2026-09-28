package com.diva.app.search.models

import com.diva.app.collection.models.Collection
import com.diva.app.folder.models.Folder
import com.diva.app.media.models.Media

data class SearchResults(
    val media: List<Media> = emptyList(),
    val folders: List<Folder> = emptyList(),
    val collections: List<Collection> = emptyList(),
) {
    val isEmpty: Boolean
        get() = media.isEmpty() && folders.isEmpty() && collections.isEmpty()
}