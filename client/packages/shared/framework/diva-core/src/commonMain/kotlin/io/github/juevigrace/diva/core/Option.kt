package io.github.juevigrace.diva.core

sealed interface Option<out T> {
    val isSome: Boolean
        get() = this is Some

    val isNone: Boolean
        get() = this is None

    class Some<T> @PublishedApi internal constructor(val value: T) : Option<T> {
        override fun equals(other: Any?): Boolean = other is Some<*> && value == other.value

        override fun hashCode(): Int = value?.hashCode() ?: 0

        override fun toString(): String = "Some($value)"
    }

    data object None : Option<Nothing>

    companion object {
        fun <T> of(value: T?): Option<T> = if (value != null) Some(value) else None
    }
}

inline fun <T, R> Option<T>.map(transform: (T) -> R): Option<R> {
    return when (this) {
        is Option.Some -> Option.Some(transform(value))
        is Option.None -> Option.None
    }
}

inline fun <T, R> Option<T>.flatMap(transform: (T) -> Option<R>): Option<R> {
    return when (this) {
        is Option.Some -> transform(value)
        is Option.None -> Option.None
    }
}

inline fun <T, R> Option<T>.mapNotNull(transform: (T) -> R?): Option<R> {
    return when (this) {
        is Option.Some -> Option.of(transform(value))
        is Option.None -> Option.None
    }
}

inline fun <T> Option<T>.getOrElse(default: () -> T): T {
    return when (this) {
        is Option.Some -> value
        is Option.None -> default()
    }
}

fun <T> Option<T>.getOrDefault(default: T): T = getOrElse { default }

fun <T> Option<T>.getOrNull(): T? {
    return when (this) {
        is Option.Some -> value
        is Option.None -> null
    }
}

fun <T> Option<T>.getOrThrow(): T {
    return when (this) {
        is Option.Some -> value
        is Option.None -> throw NoSuchElementException("No value present in Option.Some")
    }
}

inline fun <T> Option<T>.getOrThrow(exception: () -> Throwable): T {
    return when (this) {
        is Option.Some -> value
        is Option.None -> throw exception()
    }
}

inline fun <T> Option<T>.filter(predicate: (T) -> Boolean): Option<T> {
    return when (this) {
        is Option.Some -> if (predicate(value)) this else Option.None
        is Option.None -> Option.None
    }
}

inline fun <T> Option<T>.filterNot(predicate: (T) -> Boolean): Option<T> = filter { !predicate(it) }

operator fun <T> Option<T>.contains(value: T): Boolean {
    return this is Option.Some && this.value == value
}

inline fun <T> Option<T>.exists(predicate: (T) -> Boolean): Boolean {
    return this is Option.Some && predicate(value)
}

fun <T> Option<T>.orElse(default: Option<T>): Option<T> {
    return this as? Option.Some ?: default
}

inline fun <T> Option<T>.orElseGet(default: () -> Option<T>): Option<T> {
    return this as? Option.Some ?: default()
}

fun <T, R> Option<T>.zip(other: Option<R>): Option<Pair<T, R>> {
    return when {
        this is Option.Some && other is Option.Some -> Option.Some(value to other.value)
        else -> Option.None
    }
}

fun <T> Option<T>.toList(): List<T> {
    return when (this) {
        is Option.Some -> listOf(value)
        is Option.None -> emptyList()
    }
}

fun <T> Option<T>.toSet(): Set<T> {
    return when (this) {
        is Option.Some -> setOf(value)
        is Option.None -> emptySet()
    }
}

fun <T> Option<T>.asSequence(): Sequence<T> {
    return when (this) {
        is Option.Some -> sequenceOf(value)
        is Option.None -> emptySequence()
    }
}

inline fun <T> Option<T>.onSome(action: (T) -> Unit): Option<T> {
    if (this is Option.Some) action(value)
    return this
}

inline fun <T> Option<T>.onNone(action: () -> Unit): Option<T> {
    if (this is Option.None) action()
    return this
}

inline fun <T, R> Option<T>.fold(onSome: (T) -> R, onNone: () -> R): R {
    return when (this) {
        is Option.Some -> onSome(value)
        is Option.None -> onNone()
    }
}

fun <T> T?.toOption(): Option<T> {
    return Option.of(this)
}
