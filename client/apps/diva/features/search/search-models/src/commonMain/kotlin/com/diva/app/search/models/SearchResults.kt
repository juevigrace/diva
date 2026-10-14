package com.diva.app.search.models

import com.diva.app.collection.models.Collection
import com.diva.app.file.models.Directory
import com.diva.app.media.models.Media

data class SearchResults(
    val media: List<Media> = emptyList(),
    val folders: List<Directory> = emptyList(),
    val collections: List<Collection> = emptyList(),
) {
    val isEmpty: Boolean
        get() = media.isEmpty() && folders.isEmpty() && collections.isEmpty()
}