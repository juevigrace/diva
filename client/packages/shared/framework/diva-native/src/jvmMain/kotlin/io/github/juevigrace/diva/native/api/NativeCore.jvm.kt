package io.github.juevigrace.diva.native.api

import io.github.juevigrace.diva.native.coreVersion
import io.github.juevigrace.diva.native.httpGet
import io.github.juevigrace.diva.native.sqliteRoundtrip
import io.github.juevigrace.diva.native.systemInfo

actual fun createNativeCore(): NativeCoreApi = JvmNativeCore()

private class JvmNativeCore : NativeCoreApi {
    override fun version(): String = coreVersion()

    override fun info(): NativeSystemInfo {
        val info = systemInfo()
        return NativeSystemInfo(
            os = info.os,
            arch = info.arch,
            rustVersion = info.rustVersion,
            coreVersion = info.coreVersion,
        )
    }

    override fun dbProbe(): String =
        try {
            sqliteRoundtrip()
        } catch (throwable: Exception) {
            throw NativeCoreException(throwable.message ?: "sqlite probe failed", throwable)
        }

    override fun fetch(url: String): String =
        try {
            httpGet(url)
        } catch (throwable: Exception) {
            throw NativeCoreException(throwable.message ?: "http request failed", throwable)
        }
}
