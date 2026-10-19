package io.github.juevigrace.diva.lib.file.presentation.viewmodel

import io.github.juevigrace.diva.core.permissions.PermissionException
import io.github.juevigrace.diva.core.permissions.PermissionStatus
import io.github.juevigrace.diva.core.permissions.aggregate
import io.github.juevigrace.diva.lib.file.domain.FileRepository
import io.github.juevigrace.diva.lib.file.domain.services.permissions.StoragePermissionRequester
import io.github.juevigrace.diva.lib.file.models.Directory
import io.github.juevigrace.diva.lib.file.models.FileCategory
import io.github.juevigrace.diva.lib.file.presentation.events.DirectoryEvents
import io.github.juevigrace.diva.lib.file.presentation.state.DirectoryState
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FilesViewModel(
    private val repository: FileRepository,
) : DivaViewModel() {
    val state: StateFlow<DirectoryState>
        field = MutableStateFlow(DirectoryState())

    private val permissionRetry = MutableStateFlow(0)

    init {
        scope.launch {
            combine(
                state.map { it.breadcrumbs.lastOrNull()?.id },
                permissionRetry,
            ) { id, _ -> id }
                .distinctUntilChanged()
                .collectLatest { id ->
                    val flow = if (id == null) repository.observeRoot() else repository.observeDir(id)
                    flow.collect { res ->
                        res.fold(
                            onFailure = { error ->
                                if (error is PermissionException) {
                                    onPermissionFailure()
                                }
                            },
                            onSuccess = { list ->
                                state.update { state ->
                                    state.copy(entries = list)
                                }
                            },
                        )
                    }
                }
        }
    }

    fun onEvent(event: DirectoryEvents) {
        when (event) {
            is DirectoryEvents.OnOpenDirectory -> openDirectory(event.directoryId)
            is DirectoryEvents.OnNavigateToBreadcrumb -> navigateTo(event.directoryId)
            DirectoryEvents.OnNavigateUp -> navigateUp()
            is DirectoryEvents.OnSetCategory -> setCategory(event.category)
            DirectoryEvents.OnPermissionConfirm -> confirmPermission()
            DirectoryEvents.OnPermissionDismiss -> state.update { it.copy(showPermissionDialog = false) }
        }
    }

    private suspend fun onPermissionFailure() {
        val status = StoragePermissionRequester.requiredPermissions.flatMap {
            StoragePermissionRequester.checkPermission(it).permissions.values
        }
            .aggregate()
        when (status) {
            PermissionStatus.Granted -> permissionRetry.update { it + 1 }
            PermissionStatus.Denied,
            PermissionStatus.DeniedForever,
            -> state.update { it.copy(showPermissionDialog = false, permissionMissing = true) }
            else -> state.update { it.copy(showPermissionDialog = true, permissionMissing = true) }
        }
    }

    /** Reroutes to the runtime prompt first; falls back to system settings when the permission has no dialog. */
    private fun confirmPermission() {
        state.update { it.copy(showPermissionDialog = false) }
        scope.launch {
            val result = StoragePermissionRequester.request()
            if (!result.hasPermissions && result.requiresSettings) {
                StoragePermissionRequester.openAppSettings()
            }
            if (StoragePermissionRequester.hasPermission) {
                state.update { it.copy(permissionMissing = false) }
                permissionRetry.update { it + 1 }
            }
        }
    }

    private fun setCategory(category: FileCategory?) {
        state.update { state ->
            state.copy(category = category)
        }
    }

    private fun openDirectory(directoryId: String) {
        val directory = state.value.entries
            .filterIsInstance<Directory>()
            .firstOrNull { it.id == directoryId } ?: return
        state.update { state ->
            state.copy(breadcrumbs = state.breadcrumbs + directory)
        }
    }

    private fun navigateTo(directoryId: String) {
        state.update { state ->
            val index = state.breadcrumbs.indexOfFirst { it.id == directoryId }
            if (index < 0) state else state.copy(breadcrumbs = state.breadcrumbs.take(index + 1))
        }
    }

    private fun navigateUp() {
        state.update { state ->
            if (state.breadcrumbs.isEmpty()) state else state.copy(breadcrumbs = state.breadcrumbs.dropLast(1))
        }
    }
}
