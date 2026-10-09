package com.diva.app.home.presentation.viewmodel

import com.diva.app.home.presentation.events.HomeEvents
import com.diva.app.home.presentation.state.HomeState
import com.diva.app.search.presentation.ui.components.navigation.SearchResultsRoute
import com.diva.app.settings.domain.SettingsRepository
import io.github.juevigrace.diva.lib.user.domain.UserRepository
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val setRepo: SettingsRepository,
    private val userRepo: UserRepository,
    private val navigator: Navigator,
    private val tabNavigator: TabNavigator,
) : DivaViewModel() {
    val state: StateFlow<HomeState>
        field = MutableStateFlow(HomeState())

    init {
        scope.launch {
            launch {
                setRepo.observe().collect { result ->
                    result.map { settings ->
                        state.update { state ->
                            state.copy(settings = settings)
                        }
                    }
                }
            }
            launch {
                userRepo.observeCurrent().collect { result ->
                    result.map { user ->
                        state.update { state ->
                            state.copy(user = user)
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: HomeEvents) {
        when (event) {
            HomeEvents.OnBack -> onBack()
            HomeEvents.OnOpenSearch -> tabNavigator.navigate(SearchResultsRoute)
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}
