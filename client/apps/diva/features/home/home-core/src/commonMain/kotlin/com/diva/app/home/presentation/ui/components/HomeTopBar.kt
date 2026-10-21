package com.diva.app.home.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.app_name
import com.diva.app.generated.resources.puerro
import com.diva.app.home.presentation.events.HomeEvents
import io.github.juevigrace.diva.lib.ui.components.buttons.NotificationsIconButton
import io.github.juevigrace.diva.lib.ui.components.buttons.ProfileIconButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onEvent: (HomeEvents) -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        navigationIcon = navigationIcon ?: {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(Res.drawable.puerro),
                    contentDescription = null
                )
                Text(
                    text = stringResource(Res.string.app_name),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        title = title ?: {},
        actions = {
            // TODO: navigate to notifications or make a dropbox
            NotificationsIconButton(onClick = {})
            ProfileIconButton(onClick = { onEvent(HomeEvents.OnProfileClick) })
        },
        scrollBehavior = scrollBehavior,
    )
}
