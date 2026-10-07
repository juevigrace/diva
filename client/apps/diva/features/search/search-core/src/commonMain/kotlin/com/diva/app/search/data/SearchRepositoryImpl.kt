package com.diva.app.search.data

import com.diva.app.collection.domain.CollectionRepository
import com.diva.app.folder.domain.FolderRepository
import com.diva.app.media.domain.MediaMetadataRepository
import com.diva.app.media.domain.MediaRepository
import com.diva.app.media.models.Media
import com.diva.app.media.models.MediaMetadata
import com.diva.app.search.domain.SearchRepository
import com.diva.app.search.models.SearchResults
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.domain.withSession
import kotlinx.coroutines.flow.first

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

        return withSession(sessionRepository::get) { session ->
            val media = mediaRepository.observe().first().getOrDefault(emptyList())
            val folders = folderRepository.observe().first().getOrDefault(emptyList())
            val collections = collectionRepository.observe().first().getOrDefault(emptyList())

            val metadataByMediaId = media.associate { item ->
                val metadata = mediaMetadataRepository.observe(item.id).first().getOrNull()
                item.id to metadata
            }

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

            Result.success(
                SearchResults(
                    media = matchedMedia,
                    folders = matchedFolders,
                    collections = matchedCollections,
                )
            )
        }
    }

    private fun mediaMatches(media: Media, metadata: MediaMetadata?, term: String): Boolean {
        return media.title.contains(term, ignoreCase = true) ||
            media.altText.contains(term, ignoreCase = true) ||
            metadata?.album?.contains(term, ignoreCase = true) == true ||
            metadata?.artist?.contains(term, ignoreCase = true) == true ||
            metadata?.genre?.contains(term, ignoreCase = true) == true
    }
}
