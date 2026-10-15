package io.github.juevigrace.diva.ui.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import io.github.juevigrace.diva.core.permissions.PermissionRequester
import io.github.juevigrace.diva.core.permissions.PermissionState

/**
 * Drives a [PermissionRequester] from composition: whenever the required permission set
 * changes the request is issued once and the outcome reported through [onPermissionResult].
 */
@Composable
fun RequestPermission(
    requester: PermissionRequester,
    onPermissionResult: (PermissionState) -> Unit,
) {
    LaunchedEffect(requester.requiredPermissions) {
        onPermissionResult(requester.request())
    }
}
