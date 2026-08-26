package io.github.juevigrace.diva.ui.dialog

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

internal class DefaultDialogController : DialogController {
    private val _events: Channel<DialogEvent> = Channel(capacity = Channel.UNLIMITED)

    override val events: Flow<DialogEvent>
        get() = _events.receiveAsFlow()

    override fun show(request: DialogRequest) {
        _events.trySend(DialogEvent.Show(request))
    }

    override fun dismiss() {
        _events.trySend(DialogEvent.Dismiss)
    }
}
