package com.diva.app.search.presentation.events

sealed interface SearchEvents {
    data class OnQueryChange(val query: String) : SearchEvents

    data object OnClear : SearchEvents

    data object OnBack : SearchEvents
}