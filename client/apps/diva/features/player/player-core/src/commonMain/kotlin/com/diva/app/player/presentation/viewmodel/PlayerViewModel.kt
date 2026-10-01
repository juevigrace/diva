package com.diva.app.player.presentation.viewmodel

import com.diva.app.player.domain.PlayerRepository
import com.diva.app.player.models.RepeatMode
import com.diva.app.player.presentation.events.PlayerEvents
import com.diva.app.player.presentation.state.PlayerState
import com.diva.app.player.presentation.state.mockPlayerState
import com.diva.app.player.presentation.ui.components.navigation.PlayerRoute
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PlayerViewModel(
    private val repository: PlayerRepository,
    private val navigator: Navigator,
) : DivaViewModel() {

    // TODO(ui-pass): restore repository wiring
    val state: StateFlow<PlayerState>
        field = MutableStateFlow(mockPlayerState())

    fun onEvent(event: PlayerEvents) {
        when (event) {
            PlayerEvents.OnBack -> onBack()
            PlayerEvents.OnExpand -> onExpand()
            PlayerEvents.OnTogglePlayPause -> state.update { it.copy(isPlaying = !it.isPlaying) }
            PlayerEvents.OnNext -> skip(1)
            PlayerEvents.OnPrevious -> skip(-1)
            PlayerEvents.OnToggleShuffle -> state.update {
                it.copy(settings = it.settings.copy(shuffle = !it.settings.shuffle))
            }
            PlayerEvents.OnCycleRepeatMode -> state.update {
                it.copy(settings = it.settings.copy(repeatMode = it.settings.repeatMode.next()))
            }
            PlayerEvents.OnToggleLyrics -> state.update { it.copy(showLyrics = !it.showLyrics) }
            is PlayerEvents.OnScrub -> state.update { current ->
                current.copy(positionMs = (current.durationMs * event.fraction).toLong())
            }
            is PlayerEvents.OnOpenQueueItem -> state.update { current ->
                current.copy(media = event.media, positionMs = 0L, isPlaying = true)
            }
        }
    }

    private fun skip(offset: Int) {
        state.update { current ->
            if (current.queue.isEmpty()) return@update current
            val index = current.queue.indexOfFirst { it.id == current.media.id }
                .let { if (it < 0) 0 else it }
            val next = (index + offset).floorMod(current.queue.size)
            current.copy(
                media = current.queue[next],
                positionMs = 0L,
                isPlaying = true,
            )
        }
    }

    private fun onBack() {
        navigator.pop()
    }

    private fun onExpand() {
        navigator.navigate(PlayerRoute(mediaType = state.value.media.mediaType))
    }
}

private fun RepeatMode.next(): RepeatMode = when (this) {
    RepeatMode.NONE -> RepeatMode.ALL
    RepeatMode.ALL -> RepeatMode.ONE
    RepeatMode.ONE -> RepeatMode.NONE
    RepeatMode.UNSPECIFIED -> RepeatMode.NONE
}

private fun Int.floorMod(divisor: Int): Int = ((this % divisor) + divisor) % divisor