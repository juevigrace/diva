@file:Suppress("TooManyFunctions", "unused")

package io.github.juevigrace.diva.core

inline fun <T : Any, R : Any> Option<T>.map(transform: (T) -> R): Option<R> {
    return when (this) {
        is Some -> Some(transform(value))
        is None -> None
    }
}

inline fun <T : Any, R : Any> Option<T>.flatMap(transform: (T) -> Option<R>): Option<R> {
    return when (this) {
        is Some -> transform(value)
        is None -> None
    }
}

inline fun <T : Any, R : Any> Option<T>.mapNotNull(transform: (T) -> R?): Option<R> {
    return when (this) {
        is Some -> transform(value).toOption()
        is None -> None
    }
}

inline fun <T : Any> Option<T>.getOrElse(default: () -> T): T {
    return when (this) {
        is Some -> value
        is None -> default()
    }
}

fun <T : Any> Option<T>.getOrDefault(default: T): T = getOrElse { default }

fun <T : Any> Option<T>.getOrNull(): T? {
    return when (this) {
        is Some -> value
        is None -> null
    }
}

fun <T : Any> Option<T>.getOrThrow(): T {
    return when (this) {
        is Some -> value
        is None -> throw NoSuchElementException("No value present in Some")
    }
}

inline fun <T : Any> Option<T>.getOrThrow(exception: () -> Throwable): T {
    return when (this) {
        is Some -> value
        is None -> throw exception()
    }
}

inline fun <T : Any> Option<T>.filter(predicate: (T) -> Boolean): Option<T> {
    return when (this) {
        is Some -> if (predicate(value)) this else None
        is None -> None
    }
}

inline fun <T : Any> Option<T>.filterNot(predicate: (T) -> Boolean): Option<T> = filter { !predicate(it) }

operator fun <T : Any> Option<T>.contains(value: T): Boolean {
    return (this is Some) && (this.value == value)
}

inline fun <T : Any> Option<T>.exists(predicate: (T) -> Boolean): Boolean {
    return this is Some && predicate(value)
}

fun <T : Any> Option<T>.orElse(default: Option<T>): Option<T> {
    return (this as? Some) ?: default
}

inline fun <T : Any> Option<T>.orElseGet(default: () -> Option<T>): Option<T> {
    return this as? Some ?: default()
}

fun <T : Any, R : Any> Option<T>.zip(other: Option<R>): Option<Pair<T, R>> {
    return if (this is Some && other is Some) {
        Some(value to other.value)
    } else {
        None
    }
}

fun <T : Any> Option<T>.toList(): List<T> {
    return when (this) {
        is Some -> listOf(value)
        is None -> emptyList()
    }
}

fun <T : Any> Option<T>.toSet(): Set<T> {
    return when (this) {
        is Some -> setOf(value)
        is None -> emptySet()
    }
}

fun <T : Any> Option<T>.asSequence(): Sequence<T> {
    return when (this) {
        is Some -> sequenceOf(value)
        is None -> emptySequence()
    }
}

inline fun <T : Any> Option<T>.onSome(action: (T) -> Unit): Option<T> {
    if (this is Some) action(value)
    return this
}

inline fun <T : Any> Option<T>.onNone(action: () -> Unit): Option<T> {
    if (this is None) action()
    return this
}

inline fun <T : Any, R> Option<T>.fold(onSome: (T) -> R, onNone: () -> R): R {
    return when (this) {
        is Some -> onSome(value)
        is None -> onNone()
    }
}
