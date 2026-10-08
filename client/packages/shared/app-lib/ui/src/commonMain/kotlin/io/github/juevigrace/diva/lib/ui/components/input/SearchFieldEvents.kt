package io.github.juevigrace.diva.lib.ui.components.input

sealed interface SearchFieldEvents {
    data class OnQueryChange(val query: String) : SearchFieldEvents

    data object OnSearch : SearchFieldEvents

    data object OnClear : SearchFieldEvents
}
