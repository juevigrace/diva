package com.diva.app.features.collection.presentation.events

sealed interface CollectionEvents {
    data object OnBack : CollectionEvents
}