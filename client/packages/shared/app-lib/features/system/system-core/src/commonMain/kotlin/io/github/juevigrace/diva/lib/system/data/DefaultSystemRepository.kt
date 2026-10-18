package io.github.juevigrace.diva.lib.system.data

import io.github.juevigrace.diva.lib.system.domain.SystemRepository
import io.github.juevigrace.diva.native.api.NativeCoreApi
import io.github.juevigrace.diva.native.api.NativeSystemInfo

class DefaultSystemRepository(
    private val core: NativeCoreApi,
) : SystemRepository {
    override fun version(): String = core.version()

    override fun info(): NativeSystemInfo = core.info()

    override fun runDatabaseProbe(): String = core.dbProbe()

    override fun fetch(url: String): String = core.fetch(url)
}
