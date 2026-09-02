package io.github.juevigrace.diva.lib.permissions.presentation.events

sealed interface PermissionsEvents {
    data object OnBack : PermissionsEvents
}
