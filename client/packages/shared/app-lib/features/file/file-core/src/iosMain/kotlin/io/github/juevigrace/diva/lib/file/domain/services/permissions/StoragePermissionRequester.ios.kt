package io.github.juevigrace.diva.lib.file.domain.services.permissions

import io.github.juevigrace.diva.core.permissions.IosPermissionRequester

actual object StoragePermissionRequester : IosPermissionRequester() {
    actual override val requiredPermissions: List<String>
        get() = emptyList()
}
