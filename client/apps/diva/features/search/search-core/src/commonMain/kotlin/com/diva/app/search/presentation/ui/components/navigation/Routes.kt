package com.diva.app.search.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Search is not a tab, it is a destination pushed on top of whichever tab is
 * currently active, so the back stack returns the user to the tab they came from.
 * The query lives in [com.diva.app.search.presentation.state.SearchState], which is
 * owned by the view model hoisted above the tab host.
 */
@Serializable
data object SearchResults : NavKey

typealias SearchResultsRoute = SearchResults