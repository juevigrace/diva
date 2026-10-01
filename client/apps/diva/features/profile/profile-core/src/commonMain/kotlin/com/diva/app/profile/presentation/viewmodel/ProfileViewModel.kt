package com.diva.app.profile.presentation.viewmodel

import com.diva.app.profile.domain.ProfileRepository
import com.diva.app.profile.presentation.events.ProfileEvents
import com.diva.app.profile.presentation.state.ProfileState
import com.diva.app.profile.presentation.state.mockProfile
import com.diva.app.profile.presentation.state.mockRecentMedia
import com.diva.app.profile.presentation.state.mockStats
import io.github.juevigrace.diva.lib.devices.presentation.ui.components.navigation.DevicesRoute
import io.github.juevigrace.diva.lib.permissions.presentation.ui.components.navigation.PermissionsRoute
import io.github.juevigrace.diva.lib.settings.presentation.ui.components.navigation.SettingsRoute
import io.github.juevigrace.diva.lib.user.presentation.ui.components.navigation.AccountRoute
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel(
    private val repository: ProfileRepository,
    private val navigator: Navigator,
) : DivaViewModel() {

    // TODO(ui-pass): restore repository observation
    val state: StateFlow<ProfileState> = MutableStateFlow(
        ProfileState(
            profile = mockProfile(),
            stats = mockStats(),
            recentMedia = mockRecentMedia(),
        )
    )

    fun onEvent(event: ProfileEvents) {
        when (event) {
            ProfileEvents.OnBack -> navigator.pop()
            ProfileEvents.OnOpenSettings -> navigator.navigate(SettingsRoute)
            ProfileEvents.OnOpenAccount -> navigator.navigate(AccountRoute)
            ProfileEvents.OnOpenDevices -> navigator.navigate(DevicesRoute)
            ProfileEvents.OnOpenPermissions -> navigator.navigate(PermissionsRoute)
            // TODO(ui-pass): wire sign out to the auth flow
            ProfileEvents.OnSignOut -> Unit
        }
    }
}