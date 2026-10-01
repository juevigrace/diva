package com.diva.app.search.presentation.state

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.diva.app.collection.models.CollectionType
import com.diva.app.media.models.MediaType

data class TypeTag(
    val label: String,
    val icon: ImageVector,
)

fun mediaTypeTag(mediaType: MediaType): TypeTag = when (mediaType) {
    MediaType.AUDIO -> TypeTag("Audio", Icons.Filled.AudioFile)
    MediaType.VIDEO -> TypeTag("Video", Icons.Filled.Movie)
    MediaType.IMAGE -> TypeTag("Image", Icons.Filled.Image)
    MediaType.UNSPECIFIED -> TypeTag("File", Icons.Filled.QuestionMark)
}

fun collectionTypeTag(collectionType: CollectionType): TypeTag = when (collectionType) {
    CollectionType.ALBUM -> TypeTag("Album", Icons.Filled.Album)
    CollectionType.PLAYLIST -> TypeTag("Playlist", Icons.AutoMirrored.Filled.PlaylistPlay)
    CollectionType.MIX -> TypeTag("Mix", Icons.Filled.Star)
    CollectionType.FAVORITES -> TypeTag("Favourites", Icons.Filled.Favorite)
    CollectionType.FEATURED -> TypeTag("Featured", Icons.AutoMirrored.Filled.TrendingUp)
    CollectionType.TRENDING -> TypeTag("Trending", Icons.AutoMirrored.Filled.TrendingUp)
    CollectionType.UNSPECIFIED -> TypeTag("Collection", Icons.Filled.QuestionMark)
}

val folderTag = TypeTag("Folder", Icons.Filled.Folder)