package com.diva.app.home.presentation.events

import io.github.juevigrace.diva.lib.user.models.User

sealed interface HomeEvents {
    data object OnBack : HomeEvents
    data object OnForward : HomeEvents

    data object OnOpenSearch : HomeEvents
    /** Pushes the desktop results destination on the selected tab's stack. */
    data object OnOpenSearchResults : HomeEvents

    /** Opens the account-picker dialog from any profile button. */
    data object OnProfileClick : HomeEvents
    data object OnDismissProfile : HomeEvents
    /** Expands/collapses the account card into the list of other logged-in accounts. */
    data object OnToggleAccounts : HomeEvents
    data class OnSelectAccount(val user: User) : HomeEvents
    // TODO: add account flow
    data object OnAddAccount : HomeEvents
    data object OnOpenProfile : HomeEvents
    data object OnOpenSettings : HomeEvents
    // TODO: sign out flow
    data object OnSignOut : HomeEvents
}
