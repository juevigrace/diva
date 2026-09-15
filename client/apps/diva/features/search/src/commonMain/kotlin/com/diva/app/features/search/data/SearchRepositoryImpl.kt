package com.diva.app.features.search.data

import com.diva.app.features.collection.domain.CollectionRepository
import com.diva.app.features.folder.domain.FolderRepository
import com.diva.app.features.media.domain.MediaMetadataRepository
import com.diva.app.features.media.domain.MediaRepository
import com.diva.app.features.search.domain.SearchRepository
import com.diva.app.features.search.domain.SearchResults
import com.diva.app.models.media.Media
import com.diva.app.models.media.MediaMetadata
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import kotlinx.coroutines.flow.first
import kotlin.uuid.Uuid

class SearchRepositoryImpl(
    private val mediaRepository: MediaRepository,
    private val mediaMetadataRepository: MediaMetadataRepository,
    private val folderRepository: FolderRepository,
    private val collectionRepository: CollectionRepository,
    private val sessionRepository: SessionRepository,
) : SearchRepository {

    override suspend fun search(query: String): Result<SearchResults> {
        val term = query.trim()
        if (term.isEmpty()) {
            return Result.success(SearchResults())
        }

        val session = sessionRepository.getCurrentSession().first()
            .orNull()?.getOrNull()
            ?: return Result.failure(IllegalStateException("No active session"))

        val media = mediaRepository.getMedia().first().getOrDefault(emptyList())
        val folders = folderRepository.getFolders(Uuid.parse(session.user.id)).first().getOrDefault(emptyList())
        val collections = collectionRepository.getCollections().first().getOrDefault(emptyList())

        val metadataByMediaId = media.mapNotNull { item ->
            val metadata = mediaMetadataRepository.getMetadata(Uuid.parse(item.id)).first()
                .orNull()?.getOrNull()
                ?: return@mapNotNull null
            item.id to metadata
        }.toMap()

        val matchedMedia = media.filter { item ->
            mediaMatches(item, metadataByMediaId[item.id], term)
        }
        val matchedFolders = folders.filter { folder ->
            folder.name.contains(term, ignoreCase = true) ||
                folder.path.contains(term, ignoreCase = true)
        }
        val matchedCollections = collections.filter { collection ->
            collection.name.contains(term, ignoreCase = true) ||
                collection.description.contains(term, ignoreCase = true)
        }

        return Result.success(
            SearchResults(
                media = matchedMedia,
                folders = matchedFolders,
                collections = matchedCollections,
            )
        )
    }

    private fun mediaMatches(media: Media, metadata: MediaMetadata?, term: String): Boolean {
        return media.title.contains(term, ignoreCase = true) ||
            media.altText.contains(term, ignoreCase = true) ||
            metadata?.album?.contains(term, ignoreCase = true) == true ||
            metadata?.artist?.contains(term, ignoreCase = true) == true ||
            metadata?.genre?.contains(term, ignoreCase = true) == true
    }
}

private fun <T> Result<T>.orNull(): T? = fold(onSuccess = { it }, onFailure = { null })