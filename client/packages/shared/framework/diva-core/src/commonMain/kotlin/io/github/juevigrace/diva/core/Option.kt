package io.github.juevigrace.diva.core

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
sealed class Option<out T : Any> {
    val isSome: Boolean
        get() = this is Some

    val isNone: Boolean
        get() = this is None

    companion object {
        fun <T : Any> of(value: T?): Option<T> = if (value != null) Some(value) else None
    }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
class Some<out T : Any> @PublishedApi internal constructor(val value: T) : Option<T>() {
    override fun equals(other: Any?): Boolean =
        other is Some<*> && value == other.value

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = "Some($value)"
}

@OptIn(ExperimentalJsExport::class)
@JsExport
data object None : Option<Nothing>()

fun <T : Any> T?.toOption(): Option<T> {
    return Option.of(this)
}
