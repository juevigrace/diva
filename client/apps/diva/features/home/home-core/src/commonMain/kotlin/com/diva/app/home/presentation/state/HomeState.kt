package com.diva.app.home.presentation.state

import com.diva.app.collection.models.Collection
import com.diva.app.media.models.Media
import com.diva.app.settings.models.AppSettings
import io.github.juevigrace.diva.core.getOrDefault
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.lib.user.models.User

data class HomeState(
    val settings: AppSettings = AppSettings(),
    val user: User = User(id = ""),

    /** Account-picker dialog state, mirroring the Google Play account switcher. */
    val showProfileDialog: Boolean = false,
    /** Whether the account card in the dialog lists the other logged-in accounts. */
    val accountsExpanded: Boolean = false,
    /** Users behind the stored sessions; the current one is [user], the rest are switch candidates. */
    val accounts: List<User> = emptyList(),
    /** Account highlighted in the picker. Session switching is still a TODO. */
    val selectedAccountId: String? = null,

    /** Whether the desktop arrows can walk back: linear stack or tab history left. */
    val canGoBack: Boolean = false,
    /** Whether the desktop arrows can walk forward: redo steps recorded by the view model. */
    val canGoForward: Boolean = false,

    val showRecommended: Boolean = true,
    val recommendedCollections: List<Collection> = emptyList(),
    val recommendedMedia: List<Media> = emptyList(),

    val showRecent: Boolean = true,
    val recentCollections: List<Collection> = emptyList(),
    val recentMedia: List<Media> = emptyList(),
) {
    /** Display name for the current account: profile alias when set, username otherwise. */
    val displayName: String
        get() = user.profile.map { it.alias }.getOrDefault("").ifBlank { user.username }
}
