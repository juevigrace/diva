package io.github.juevigrace.diva.ui.toast

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

internal class DefaultToaster : Toaster {
    private val _events: Channel<ToastEvent> = Channel(capacity = Channel.UNLIMITED)

    override val events: Flow<ToastEvent>
        get() = _events.receiveAsFlow()

    override fun show(request: ToastRequest) {
        _events.trySend(ToastEvent.Show(request))
    }
}
