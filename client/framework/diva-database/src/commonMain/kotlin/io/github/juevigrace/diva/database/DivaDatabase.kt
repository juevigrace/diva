package io.github.juevigrace.diva.database

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterBase
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.ioDispatcher
import io.github.juevigrace.diva.core.toOption
import io.github.juevigrace.diva.database.driver.DriverProvider
import io.github.juevigrace.diva.database.exception.DatabaseExceptionTransformer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

interface DivaDatabase<S : TransacterBase> {
    val scope: CoroutineScope

    suspend fun <T : Any> getOne(
        context: CoroutineContext = ioDispatcher,
        block: S.() -> Query<T>,
    ): Result<Option<T>>

    fun <T : Any> getOneAsFlow(
        context: CoroutineContext = ioDispatcher,
        block: S.() -> Query<T>,
    ): Flow<Result<Option<T>>>

    suspend fun <T : Any> getList(
        context: CoroutineContext = ioDispatcher,
        block: S.() -> Query<T>,
    ): Result<List<T>>

    fun <T : Any> getListAsFlow(
        context: CoroutineContext = ioDispatcher,
        block: S.() -> Query<T>,
    ): Flow<Result<List<T>>>

    suspend fun <T : Any> use(
        context: CoroutineContext = ioDispatcher,
        block: suspend S.() -> T,
    ): Result<T>

    suspend fun <T : Any> withDriver(
        context: CoroutineContext = ioDispatcher,
        block: suspend SqlDriver.() -> T,
    ): Result<T>

    suspend fun close(): Result<Unit>

    companion object {
        fun <S : TransacterBase> create(
            provider: DriverProvider<*>,
            schema: SqlSchema<QueryResult.Value<Unit>>,
            db: (SqlDriver) -> S,
        ): Result<DivaDatabase<S>> {
            return provider.createSyncDriver(schema).map { driver ->
                DivaDatabaseImpl(driver, db(driver), provider.transformer)
            }
        }

        suspend fun <S : TransacterBase> createAsync(
            provider: DriverProvider<*>,
            schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
            db: (SqlDriver) -> S,
        ): Result<DivaDatabase<S>> {
            return provider.createAsyncDriver(schema).map { driver ->
                DivaDatabaseImpl(driver, db(driver), provider.transformer)
            }
        }
    }
}

internal class DivaDatabaseImpl<S : TransacterBase>(
    private val driver: SqlDriver,
    private val db: S,
    private val transformer: DatabaseExceptionTransformer,
) : DivaDatabase<S> {
    override val scope: CoroutineScope = CoroutineScope(ioDispatcher)

    override suspend fun <T : Any> getOne(
        context: CoroutineContext,
        block: S.() -> Query<T>,
    ): Result<Option<T>> {
        return withContext(context) {
            runCatching {
                block(db).executeAsOneOrNull().toOption()
            }.recoverCatching { throw transformer.transform(it) }
        }
    }

    override fun <T : Any> getOneAsFlow(
        context: CoroutineContext,
        block: S.() -> Query<T>,
    ): Flow<Result<Option<T>>> {
        return block(db).asFlow()
            .mapToOneOrNull(context)
            .map { entity ->
                Result.success(entity.toOption())
            }
            .catch { e ->
                emit(Result.failure(transformer.transform(e)))
            }
    }

    override suspend fun <T : Any> getList(
        context: CoroutineContext,
        block: S.() -> Query<T>,
    ): Result<List<T>> {
        return withContext(context) {
            runCatching {
                block(db).executeAsList()
            }.recoverCatching { throw transformer.transform(it) }
        }
    }

    override fun <T : Any> getListAsFlow(
        context: CoroutineContext,
        block: S.() -> Query<T>,
    ): Flow<Result<List<T>>> {
        return block(db).asFlow()
            .mapToList(context)
            .map { list ->
                Result.success(list)
            }
            .catch { e ->
                emit(Result.failure(transformer.transform(e)))
            }
    }

    override suspend fun <T : Any> use(
        context: CoroutineContext,
        block: suspend S.() -> T,
    ): Result<T> {
        return withContext(context) {
            runCatching {
                block(db)
            }.recoverCatching { throw transformer.transform(it) }
        }
    }

    override suspend fun <T : Any> withDriver(
        context: CoroutineContext,
        block: suspend SqlDriver.() -> T,
    ): Result<T> {
        return withContext(context) {
            runCatching {
                block(driver)
            }.recoverCatching { throw transformer.transform(it) }
        }
    }

    override suspend fun close(): Result<Unit> {
        return runCatching {
            driver.close()
        }.recoverCatching { throw transformer.transform(it) }
    }
}
