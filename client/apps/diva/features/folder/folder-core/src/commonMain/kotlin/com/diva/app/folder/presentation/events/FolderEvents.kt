package com.diva.app.folder.presentation.events

sealed interface FolderEvents {
    data object OnBack : FolderEvents
}