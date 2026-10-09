package com.diva.app.profile.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.profile
import com.diva.app.profile.presentation.events.ProfileEvents
import com.diva.app.profile.presentation.ui.components.InfoSection
import com.diva.app.profile.presentation.ui.components.NavigationRow
import com.diva.app.profile.presentation.ui.components.SectionTitle
import com.diva.app.profile.presentation.viewmodel.ProfileViewModel
import com.diva.app.ui.components.Artwork
import com.diva.app.ui.components.ArtworkPlaceholder
import io.github.juevigrace.diva.core.getOrDefault
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.lib.user.models.UserStatus
import io.github.juevigrace.diva.lib.user.preferences.models.Theme
import io.github.juevigrace.diva.ui.layout.Screen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * The profile tab root. Nothing else reads the profile state, so the view model is
 * resolved here rather than hoisted by the tab host.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    Screen(
        topBar = { TopAppBar(title = { Text(stringResource(Res.string.profile)) }) },
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->
        val user = state.user
        if (user.id.isNotBlank()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 32.dp),
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Artwork(
                            modifier = Modifier.size(72.dp),
                            shape = MaterialTheme.shapes.large,
                        ) {
                            ArtworkPlaceholder(alt = user.username, modifier = Modifier.fillMaxSize())
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = user.username,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                if (user.state.map { it.verified }.getOrDefault(false)) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircle,
                                        contentDescription = "Verified",
                                        modifier = Modifier.size(18.dp),
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            }
                            if (user.profile.map { it.alias }.getOrDefault("").isNotBlank()) {
                                Text(
                                    text = user.profile.map { it.alias }.getOrDefault(""),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                    if (user.profile.map { it.bio }.getOrDefault("").isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = user.profile.map { it.bio }.getOrDefault(""), style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    state.stats.forEach { stat ->
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = stat.value,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = stat.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))

                SectionTitle(text = "Manage")

                NavigationRow(
                    icon = Icons.Filled.Settings,
                    title = "Settings",
                    onClick = { onEvent(ProfileEvents.OnOpenSettings) },
                )
                NavigationRow(
                    icon = Icons.Filled.ManageAccounts,
                    title = "Account",
                    onClick = { onEvent(ProfileEvents.OnOpenAccount) },
                )
                NavigationRow(
                    icon = Icons.Filled.Devices,
                    title = "Devices",
                    onClick = { onEvent(ProfileEvents.OnOpenDevices) },
                )
                NavigationRow(
                    icon = Icons.Filled.Key,
                    title = "Permissions",
                    onClick = { onEvent(ProfileEvents.OnOpenPermissions) },
                )
                NavigationRow(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    title = "Sign out",
                    onClick = { onEvent(ProfileEvents.OnSignOut) },
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))

                InfoSection(
                    title = "Account details",
                    rows = listOf(
                        "Email" to user.email.getOrDefault(""),
                        "Phone" to (user.profile.map { it.phoneNumber }.getOrDefault("").takeIf { it.isNotBlank() } ?: user.phoneNumber.getOrDefault("")),
                        "Role" to user.role.name,
                        "Status" to user.state.map { it.status.name }.getOrDefault(UserStatus.ACTIVE.name),
                        "Verified" to user.state.map { it.verified }.getOrDefault(false).toString(),
                        "Theme" to user.preferences.map { it.theme.name }.getOrDefault(Theme.SYSTEM.name),
                        "Language" to user.preferences.map { it.language }.getOrDefault("en"),
                    ),
                )

                InfoSection(
                    title = "Linked devices",
                    rows = user.devices.map { it.device.name to "" },
                    emptyText = "No devices linked yet.",
                )
            }
        }
    }
}
