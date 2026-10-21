package com.diva.app.home.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.diva.app.ui.components.Artwork
import com.diva.app.ui.components.ArtworkPlaceholder
import io.github.juevigrace.diva.core.getOrDefault
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.lib.user.models.User

/**
 * Account-picker dialog, shaped after the Google Play account switcher: the current
 * account is a card that expands into the other accounts stored on device (the users
 * behind the saved sessions), with account management cards below. Presentation only —
 * every action leaves through the callbacks, owned by the caller's view model.
 *
 * The account card and its dropdown sit on a raised surface so they read as one block
 * against the dialog background; the actions below are grouped into two separate cards
 * (profile/settings, then sign out) separated by dividers.
 *
 * @param displayName display name of the current account, alias falling back to username.
 * @param username username of the current account, shown below the display name.
 * @param otherAccounts the other logged-in accounts; shown while [accountsExpanded].
 * @param accountsExpanded whether the account card is expanded into [otherAccounts].
 */
@Composable
fun ProfileMenuDialog(
    displayName: String,
    username: String,
    otherAccounts: List<User>,
    accountsExpanded: Boolean,
    /** Account highlighted in the picker; switching is still a TODO in the view model. */
    selectedAccountId: String?,
    onToggleAccounts: () -> Unit,
    onSelectAccount: (User) -> Unit,
    onAddAccount: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenSettings: () -> Unit,
    onSignOut: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .widthIn(max = 360.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Account",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                AccountPickerCard(
                    displayName = displayName,
                    username = username,
                    otherAccounts = otherAccounts,
                    expanded = accountsExpanded,
                    selectedAccountId = selectedAccountId,
                    onToggle = onToggleAccounts,
                    onSelectAccount = onSelectAccount,
                    onAddAccount = onAddAccount,
                )

                Spacer(modifier = Modifier.height(12.dp))

                MenuCard {
                    DialogActionRow(
                        icon = Icons.Filled.Person,
                        title = "Profile",
                        onClick = onOpenProfile,
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                    DialogActionRow(
                        icon = Icons.Filled.Settings,
                        title = "Settings",
                        onClick = onOpenSettings,
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                MenuCard {
                    DialogActionRow(
                        icon = Icons.AutoMirrored.Filled.Logout,
                        title = "Sign out",
                        tint = MaterialTheme.colorScheme.error,
                        textColor = MaterialTheme.colorScheme.error,
                        onClick = onSignOut,
                    )
                }
            }
        }
    }
}

/** One grouped block of actions on the raised surface, distinct from the dialog background. */
@Composable
private fun MenuCard(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        Column { content() }
    }
}

@Composable
private fun AccountPickerCard(
    displayName: String,
    username: String,
    otherAccounts: List<User>,
    expanded: Boolean,
    selectedAccountId: String?,
    onToggle: () -> Unit,
    onSelectAccount: (User) -> Unit,
    onAddAccount: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        Column {
            AccountCard(
                displayName = displayName,
                username = username,
                expanded = expanded,
                onToggle = onToggle,
            )

            AnimatedVisibility(visible = expanded) {
                // Deepest surface level: keeps the dropdown visually attached to the card
                // while still separating it from the card header.
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    otherAccounts.forEachIndexed { index, account ->
                        if (index > 0) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.outlineVariant,
                            )
                        }
                        AccountRow(
                            displayName = account.profile
                                .map { it.alias }
                                .getOrDefault("")
                                .ifBlank { account.username },
                            username = account.username,
                            selected = account.id == selectedAccountId,
                            onClick = { onSelectAccount(account) },
                        )
                    }
                    if (otherAccounts.isNotEmpty()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant,
                        )
                    }
                    DialogActionRow(
                        icon = Icons.Filled.Add,
                        title = "Add account",
                        onClick = onAddAccount,
                    )
                }
            }
        }
    }
}

@Composable
private fun AccountCard(
    displayName: String,
    username: String,
    expanded: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .clickable { onToggle() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Artwork(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
        ) {
            ArtworkPlaceholder(alt = username, modifier = Modifier.fillMaxSize())
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
        ) {
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
            )
            Text(
                text = username,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
        }
        Icon(
            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = if (expanded) "Collapse accounts" else "Expand accounts",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun AccountRow(
    displayName: String,
    username: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Artwork(
            modifier = Modifier.size(36.dp),
            shape = CircleShape,
        ) {
            ArtworkPlaceholder(alt = username, modifier = Modifier.fillMaxSize())
        }
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(
                text = displayName,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
            )
            Text(
                text = username,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun DialogActionRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor,
        )
    }
}
