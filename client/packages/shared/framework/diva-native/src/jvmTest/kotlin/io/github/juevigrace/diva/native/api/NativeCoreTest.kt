package io.github.juevigrace.diva.native.api

import kotlin.test.Test
import kotlin.test.assertTrue

class NativeCoreTest {
    @Test
    fun exposesRuntimeInformation() {
        val core = createNativeCore()

        assertTrue(core.version().isNotBlank())
        assertTrue(core.info().os.isNotBlank())
        assertTrue(core.dbProbe().startsWith("sqlite"))
    }
}
