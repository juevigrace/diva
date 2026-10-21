package com.diva.app.search.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.ic_search
import com.diva.app.generated.resources.search
import io.github.juevigrace.diva.ui.navigation.Tab
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * Search is a tab rather than a pushed destination: on mobile it lives in the bottom
 * bar. On desktop the tab is hidden from the rail/drawer and the top-bar search bar
 * pushes [SearchResults] linearly onto the selected tab's stack instead, so searching
 * never lands on an invisible tab.
 *
 * The query lives in [com.diva.app.search.presentation.state.SearchState], which is
 * hoisted by the tab host so the desktop bar and both screens share one view model.
 */
@Serializable
data object Search : NavKey, Tab {
    override val route: NavKey
        get() = this

    override val icon: DrawableResource = Res.drawable.ic_search
    override val title: StringResource = Res.string.search
}

/**
 * The desktop results destination, pushed on the currently selected tab's stack when a
 * result is chosen in the top-bar search preview. It renders the same screen as the
 * mobile [Search] tab, minus its own field (the top bar owns the query).
 */
@Serializable
data object SearchResults : NavKey

typealias SearchRoute = Search

typealias SearchResultsRoute = SearchResults
