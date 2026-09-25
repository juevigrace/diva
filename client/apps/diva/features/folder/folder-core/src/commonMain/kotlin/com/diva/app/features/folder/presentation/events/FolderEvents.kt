package com.diva.app.features.folder.presentation.events

sealed interface FolderEvents {
    data object OnBack : FolderEvents
}