package io.github.juevigrace.diva.database

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterBase
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import io.github.juevigrace.diva.database.driver.DriverProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface DivaDatabase<S : TransacterBase> {
    suspend fun <T : Any> getOne(block: S.() -> Query<T>): Result<T>

    fun <T : Any> getOneAsFlow(
        ctx: CoroutineContext = EmptyCoroutineContext,
        block: S.() -> Query<T>,
    ): Flow<Result<T>>

    suspend fun <T : Any> getList(block: S.() -> Query<T>): Result<List<T>>

    fun <T : Any> getListAsFlow(
        ctx: CoroutineContext = EmptyCoroutineContext,
        block: S.() -> Query<T>,
    ): Flow<Result<List<T>>>

    suspend fun <T : Any> use(block: suspend S.() -> T): Result<T>

    suspend fun <T : Any> withDriver(block: suspend SqlDriver.() -> T): Result<T>

    suspend fun checkHealth(): Result<Boolean>

    suspend fun close(): Result<Unit>

    companion object {
        fun <S : TransacterBase> create(
            provider: DriverProvider<*>,
            schema: SqlSchema<QueryResult.Value<Unit>>,
            db: (SqlDriver) -> S,
        ): Result<DivaDatabase<S>> {
            return provider.createSyncDriver(schema).map { driver ->
                DivaDatabaseImpl(driver, db(driver))
            }
        }

        suspend fun <S : TransacterBase> createAsync(
            provider: DriverProvider<*>,
            schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
            db: (SqlDriver) -> S,
        ): Result<DivaDatabase<S>> {
            return provider.createAsyncDriver(schema).map { driver ->
                DivaDatabaseImpl(driver, db(driver))
            }
        }
    }
}

internal class DivaDatabaseImpl<S : TransacterBase>(
    private val driver: SqlDriver,
    private val db: S
) : DivaDatabase<S> {
    override suspend fun <T : Any> getOne(block: S.() -> Query<T>): Result<T> {
        return runCatching {
            block(db).executeAsOneOrNull()
                ?: throw NoSuchElementException("no rows returned")
        }
    }

    override fun <T : Any> getOneAsFlow(
        ctx: CoroutineContext,
        block: S.() -> Query<T>
    ): Flow<Result<T>> {
        return block(db).asFlow()
            .mapToOneOrNull(ctx)
            .catch { e ->
                Result.failure<T>(e)
            }
            .map { entity ->
                if (entity == null) {
                    Result.failure(NoSuchElementException("no rows returned"))
                } else {
                    Result.success(entity)
                }
            }
    }

    override suspend fun <T : Any> getList(
        block: S.() -> Query<T>
    ): Result<List<T>> {
        return runCatching { block(db).executeAsList() }
    }

    override fun <T : Any> getListAsFlow(
        ctx: CoroutineContext,
        block: S.() -> Query<T>
    ): Flow<Result<List<T>>> {
        return block(db).asFlow()
            .mapToList(ctx)
            .catch { e ->
                Result.failure<List<T>>(e)
            }
            .map { list ->
                Result.success(list)
            }
    }

    override suspend fun <T : Any> use(
        block: suspend S.() -> T
    ): Result<T> {
        return runCatching {
            block(db)
        }
    }

    override suspend fun <T : Any> withDriver(
        block: suspend SqlDriver.() -> T
    ): Result<T> {
        return runCatching {
            block(driver)
        }
    }

    override suspend fun checkHealth(): Result<Boolean> {
        return runCatching {
            driver.execute(null, "SELECT 1", 0).value
            true
        }
    }

    override suspend fun close(): Result<Unit> {
        return runCatching {
            driver.close()
            Result.success(Unit)
        }
    }
}
