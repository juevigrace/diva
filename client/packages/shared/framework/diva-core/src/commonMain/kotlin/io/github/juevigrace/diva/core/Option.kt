@file:DivaJsExport

package io.github.juevigrace.diva.core

import kotlin.js.JsName

sealed class Option<out T : Any> {
    val isSome: Boolean
        get() = this is Some

    val isNone: Boolean
        get() = this is None

    companion object {
        @JsName("of")
        fun <T : Any> of(value: T?): Option<T> = value?.let { Some(it) } ?: None

        @JsName("some")
        fun <T : Any> some(value: T): Option<T> = Some(value)

        @JsName("none")
        fun <T : Any> none(): Option<T> = None
    }
}

class Some<out T : Any> @PublishedApi internal constructor(val value: T) : Option<T>() {
    override fun equals(other: Any?): Boolean =
        (other is Some<*>) && (value == other.value)

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = "Some($value)"
}

data object None : Option<Nothing>()

fun <T : Any> T?.toOption(): Option<T> {
    return Option.of(this)
}
