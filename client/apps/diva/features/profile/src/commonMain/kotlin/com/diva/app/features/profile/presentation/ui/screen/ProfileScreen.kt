package com.diva.app.features.profile.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diva.app.features.profile.domain.Profile
import com.diva.app.features.profile.presentation.events.ProfileEvents
import com.diva.app.features.profile.presentation.viewmodel.ProfileViewModel
import io.github.juevigrace.diva.ui.layout.Screen
import io.github.juevigrace.diva.ui.navigation.BackHandler
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    BackHandler { viewModel.onEvent(ProfileEvents.OnBack) }

    Screen { innerPadding ->
        val profile = state.value.profile ?: return@Screen
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            ProfileHeader(profile = profile)
            Spacer(modifier = Modifier.height(24.dp))
            InfoSection(
                title = "Account",
                rows = listOf(
                    "Email" to (profile.email ?: ""),
                    "Phone" to (profile.phoneNumber ?: ""),
                    "Role" to profile.role.name,
                    "Status" to profile.status.name,
                    "Verified" to if (profile.verified) "Yes" else "No",
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
            InfoSection(
                title = "Preferences",
                rows = listOf(
                    "Theme" to profile.theme.name,
                    "Language" to profile.language,
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
            InfoSection(
                title = "Devices",
                rows = profile.devices.ifEmpty { listOf("No devices linked") }.map { it to "" },
            )
        }
    }
}

@Composable
private fun ProfileHeader(profile: Profile) {
    val initial = profile.username.firstOrNull()?.uppercase() ?: "?"
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.large,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = initial,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = profile.username,
                style = MaterialTheme.typography.headlineSmall,
            )
            profile.alias.takeIf { it.isNotBlank() }?.let { alias ->
                Text(
                    text = alias,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            profile.bio.takeIf { it.isNotBlank() }?.let { bio ->
                Text(
                    text = bio,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun InfoSection(title: String, rows: List<Pair<String, String>>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        rows.forEach { (label, value) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                value.takeIf { it.isNotBlank() }?.let {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}