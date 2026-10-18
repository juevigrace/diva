package com.diva.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import io.github.juevigrace.diva.lib.system.domain.SystemRepository
import io.github.juevigrace.diva.native.api.NativeSystemInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SystemUiState(
    val version: String = "",
    val info: NativeSystemInfo? = null,
    val databaseProbe: String = "",
    val httpResult: String = "",
    val error: String? = null,
)

class SystemViewModel(
    private val repository: SystemRepository,
) : ViewModel() {
    private val mutableState = MutableStateFlow(SystemUiState())
    val state: StateFlow<SystemUiState> = mutableState

    init {
        refresh()
    }

    fun refresh() {
        mutableState.value =
            try {
                SystemUiState(
                    version = repository.version(),
                    info = repository.info(),
                    databaseProbe = repository.runDatabaseProbe(),
                )
            } catch (throwable: Throwable) {
                mutableState.value.copy(error = throwable.message)
            }
    }

    fun fetch(url: String) {
        mutableState.value =
            try {
                mutableState.value.copy(httpResult = repository.fetch(url))
            } catch (throwable: Throwable) {
                mutableState.value.copy(error = throwable.message)
            }
    }
}
