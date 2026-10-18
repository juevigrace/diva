package io.github.juevigrace.diva.lib.system.domain

import io.github.juevigrace.diva.native.api.NativeSystemInfo

interface SystemRepository {
    fun version(): String

    fun info(): NativeSystemInfo

    fun runDatabaseProbe(): String

    fun fetch(url: String): String
}
