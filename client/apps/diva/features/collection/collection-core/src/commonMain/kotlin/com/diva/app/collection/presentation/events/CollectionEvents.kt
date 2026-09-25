package com.diva.app.collection.presentation.events

sealed interface CollectionEvents {
    data object OnBack : CollectionEvents
}