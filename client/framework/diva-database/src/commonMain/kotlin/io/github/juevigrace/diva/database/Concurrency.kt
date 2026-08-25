package io.github.juevigrace.diva.database

import app.cash.sqldelight.TransacterBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun <S : TransacterBase> DivaDatabase<S>.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return scope.launch(context = context, block = block)
}

fun <S : TransacterBase, T> DivaDatabase<S>.async(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> T,
): Deferred<T> {
    return scope.async(context = context, block = block)
}
