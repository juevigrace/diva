package com.diva.app.features.profile.presentation.viewmodel

import com.diva.app.features.profile.domain.ProfileRepository
import com.diva.app.features.profile.presentation.events.ProfileEvents
import com.diva.app.features.profile.presentation.state.ProfileState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository,
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<ProfileState>
        field = MutableStateFlow(ProfileState())

    init {
        scope.launch {
            repository.observeProfile().collect { result ->
                result.getOrDefault(null)?.let { profile ->
                    state.update { it.copy(profile = profile) }
                }
            }
        }
    }

    fun onEvent(event: ProfileEvents) {
        when (event) {
            ProfileEvents.OnBack -> onBack()
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}