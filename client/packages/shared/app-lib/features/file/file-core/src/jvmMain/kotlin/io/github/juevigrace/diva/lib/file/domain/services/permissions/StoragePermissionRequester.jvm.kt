package io.github.juevigrace.diva.lib.file.domain.services.permissions

import io.github.juevigrace.diva.core.permissions.JvmPermissionRequester

actual object StoragePermissionRequester : JvmPermissionRequester() {
    actual override val requiredPermissions: List<String>
        get() = emptyList()
}
