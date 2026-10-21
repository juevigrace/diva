package com.diva.app.profile.presentation.viewmodel

import com.diva.app.profile.presentation.events.ProfileEvents
import com.diva.app.profile.presentation.state.ProfileState
import io.github.juevigrace.diva.lib.devices.presentation.ui.components.navigation.DevicesRoute
import io.github.juevigrace.diva.lib.permissions.presentation.ui.components.navigation.PermissionsRoute
import io.github.juevigrace.diva.lib.settings.presentation.ui.components.navigation.SettingsRoute
import io.github.juevigrace.diva.lib.user.domain.UserRepository
import io.github.juevigrace.diva.lib.user.presentation.ui.components.navigation.AccountRoute
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val tabNavigator: TabNavigator,
) : DivaViewModel() {

    val state: StateFlow<ProfileState>
        field = MutableStateFlow(ProfileState())

    init {
        scope.launch {
            userRepository.observeCurrent().collect { result ->
                result.onSuccess { user ->
                    state.update { it.copy(user = user) }
                }
            }
        }
    }

    fun onEvent(event: ProfileEvents) {
        when (event) {
            // Profile lives inside the tab host, so its destinations push on the tab stack.
            ProfileEvents.OnOpenSettings -> tabNavigator.navigate(SettingsRoute)
            ProfileEvents.OnOpenAccount -> tabNavigator.navigate(AccountRoute)
            ProfileEvents.OnOpenDevices -> tabNavigator.navigate(DevicesRoute)
            ProfileEvents.OnOpenPermissions -> tabNavigator.navigate(PermissionsRoute)
            ProfileEvents.OnSignOut -> Unit
        }
    }
}
