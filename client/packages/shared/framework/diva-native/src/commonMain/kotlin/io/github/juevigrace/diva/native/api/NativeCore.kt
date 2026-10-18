package io.github.juevigrace.diva.native.api

/**
 * Stable, multiplatform-facing facade over the Rust core.
 *
 * Implemented through UniFFI on Android, JVM and Apple targets, and by a pure-Kotlin
 * stub on the web targets where the native library is not available.
 */
interface NativeCoreApi {
    fun version(): String

    fun info(): NativeSystemInfo

    fun dbProbe(): String

    fun fetch(url: String): String
}

data class NativeSystemInfo(
    val os: String,
    val arch: String,
    val rustVersion: String,
    val coreVersion: String,
)

class NativeCoreException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause)

expect fun createNativeCore(): NativeCoreApi

private class WebNativeCore : NativeCoreApi {
    private fun unsupported(): Nothing =
        throw NativeCoreException("The Rust core is not available on this target")

    override fun version(): String = unsupported()

    override fun info(): NativeSystemInfo = unsupported()

    override fun dbProbe(): String = unsupported()

    override fun fetch(url: String): String = unsupported()
}

internal fun webNativeCore(): NativeCoreApi = WebNativeCore()
