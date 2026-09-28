package io.github.juevigrace.diva.lib.core

import io.github.juevigrace.diva.core.ioDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

interface Repository {
    val scope: CoroutineScope
        get() = CoroutineScope(SupervisorJob() + ioDispatcher)
}
