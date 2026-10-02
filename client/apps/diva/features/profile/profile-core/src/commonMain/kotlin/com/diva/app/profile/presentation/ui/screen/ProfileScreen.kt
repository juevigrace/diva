package com.diva.app.profile.presentation.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diva.app.profile.models.Profile
import com.diva.app.profile.presentation.events.ProfileEvents
import com.diva.app.profile.presentation.state.ProfileQuickStat
import com.diva.app.profile.presentation.viewmodel.ProfileViewModel
import com.diva.app.ui.components.Artwork
import io.github.juevigrace.diva.core.getOrDefault
import io.github.juevigrace.diva.lib.ui.components.carousel.Carousel
import io.github.juevigrace.diva.ui.layout.Screen
import io.github.juevigrace.diva.ui.navigation.BackHandler
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BackHandler { viewModel.onEvent(ProfileEvents.OnBack) }

    Screen(
        topBar = {
            TopAppBar(title = { Text(text = "Profile") })
        },
    ) { innerPadding ->
        val profile = state.profile ?: return@Screen
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp),
        ) {
            ProfileHeader(profile = profile)
            Spacer(modifier = Modifier.height(8.dp))
            StatsRow(stats = state.stats)
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))

            SectionTitle(text = "Manage")

            NavigationRow(
                icon = Icons.Filled.Settings,
                title = "Settings",
                onClick = { viewModel.onEvent(ProfileEvents.OnOpenSettings) },
            )
            NavigationRow(
                icon = Icons.Filled.ManageAccounts,
                title = "Account",
                onClick = { viewModel.onEvent(ProfileEvents.OnOpenAccount) },
            )
            NavigationRow(
                icon = Icons.Filled.Devices,
                title = "Devices",
                onClick = { viewModel.onEvent(ProfileEvents.OnOpenDevices) },
            )
            NavigationRow(
                icon = Icons.Filled.Key,
                title = "Permissions",
                onClick = { viewModel.onEvent(ProfileEvents.OnOpenPermissions) },
            )
            NavigationRow(
                icon = Icons.AutoMirrored.Filled.Logout,
                title = "Sign out",
                onClick = { viewModel.onEvent(ProfileEvents.OnSignOut) },
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))

            InfoSection(
                title = "Account details",
                rows = listOf(
                    "Email" to profile.email.getOrDefault(""),
                    "Phone" to profile.phoneNumber.getOrDefault(""),
                    "Role" to profile.role.name,
                    "Status" to profile.status.name,
                    "Verified" to profile.verified.toString(),
                    "Theme" to profile.theme.name,
                    "Language" to profile.language,
                ),
            )

            InfoSection(
                title = "Linked devices",
                rows = profile.devices.map { device ->
                    device to ""
                },
                emptyText = "No devices linked yet.",
            )

            if (state.recentMedia.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                SectionTitle(text = "Recently played")
                RecentRow(
                    titles = state.recentMedia.map { it.title },
                    onSelect = { },
                )
            }
        }
    }
}

@Composable
private fun ProfileHeader(profile: Profile) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Artwork(
                seed = profile.username,
                modifier = Modifier.size(72.dp),
                shape = MaterialTheme.shapes.large,
                contentDescription = profile.username,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = profile.username,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    if (profile.verified) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Verified",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
                if (profile.alias.isNotBlank()) {
                    Text(
                        text = profile.alias,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        if (profile.bio.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = profile.bio, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun StatsRow(stats: List<ProfileQuickStat>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        stats.forEach { stat ->
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
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 4.dp),
    )
}

@Composable
private fun NavigationRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun InfoSection(
    title: String,
    rows: List<Pair<String, String>>,
    emptyText: String? = null,
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        SectionTitle(text = title)
        if (rows.isEmpty()) {
            emptyText?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            return@Column
        }
        rows.forEach { (label, value) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (value.isNotBlank()) {
                    Text(text = value, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun RecentRow(titles: List<String>, onSelect: (Int) -> Unit) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val spacing = 12.dp
        val gutter = 40.dp
        val visible = maxOf(1, ((maxWidth - gutter + spacing) / (140.dp + spacing)).toInt())
        val cardWidth = ((maxWidth - gutter - spacing * (visible - 1)) / visible)
            .coerceIn(96.dp, 140.dp)

        Carousel(
            pageCount = titles.size,
            visiblePages = visible,
            pageSize = cardWidth,
            pageSpacing = spacing,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            Box(
                modifier = Modifier
                    .width(cardWidth)
                    .aspectRatio(1f)
                    .clickable { onSelect(page) },
            ) {
                Artwork(
                    seed = titles[page],
                    modifier = Modifier.fillMaxSize(),
                    shape = MaterialTheme.shapes.large,
                    contentDescription = titles[page],
                )
            }
        }
    }
}
