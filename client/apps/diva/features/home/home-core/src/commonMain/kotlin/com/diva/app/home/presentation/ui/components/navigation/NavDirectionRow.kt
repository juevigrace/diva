package com.diva.app.home.presentation.ui.components.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.juevigrace.diva.ui.layout.buttons.CompactIconButton

/**
 * Compact row of backwards and forwards navigation controls, built on
 * [CompactIconButton] so each control takes only the space its icon needs
 * instead of the 48dp minimum [androidx.compose.material3.IconButton] enforces.
 *
 * Icon size is set by the caller on the icons they pass; the row adds no
 * spacing of its own beyond [horizontalArrangement].
 *
 * The component is presentation only: wiring pop/forward history is up to the
 * caller, and so is showing or hiding it.
 *
 * @param onBack invoked when the backwards control is tapped.
 * @param onForward invoked when the forwards control is tapped.
 * @param backIcon slot rendering the backwards icon.
 * @param forwardIcon slot rendering the forwards icon.
 * @param backEnabled whether the backwards control responds to taps.
 * @param forwardEnabled whether the forwards control responds to taps.
 * @param horizontalArrangement arrangement between the two controls.
 */
@Composable
fun NavDirectionRow(
    onBack: () -> Unit,
    onForward: () -> Unit,
    modifier: Modifier = Modifier,
    backIcon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
        )
    },
    forwardIcon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Forward",
        )
    },
    backEnabled: Boolean = true,
    forwardEnabled: Boolean = true,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceAround,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompactIconButton(
            onClick = onBack,
            enabled = backEnabled,
            icon = backIcon,
        )
        CompactIconButton(
            onClick = onForward,
            enabled = forwardEnabled,
            icon = forwardIcon,
        )
    }
}
